package com.projetojava.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.projetojava.brewer.controller.page.PageWrapper;
import com.projetojava.brewer.repository.Estilos;
import com.projetojava.brewer.repository.filter.EstiloFilter;
import com.projetojava.brewer.service.exception.ImpossivelExcluirEntidadeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projetojava.brewer.model.Estilo;
import com.projetojava.brewer.service.CadastroEstiloService;
import com.projetojava.brewer.service.exception.NomeEstiloJaCadastradoException;

@Controller
@RequestMapping("/estilos")
public class EstilosController {

	@Autowired
	private CadastroEstiloService cadastroEstiloService;

	@Autowired
	private Estilos estilos;

	@GetMapping
	public ModelAndView pesquisar(EstiloFilter filtro, @PageableDefault(size = 5) Pageable pageable,
								  HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("estilo/PesquisaEstilos");

		PageWrapper pagina = new PageWrapper(estilos.filtrar(filtro, pageable), httpServletRequest);
		mv.addObject("estilosPagina", pagina);

		return mv;
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvar(@RequestBody @Valid Estilo estilo,
												  BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity
					.badRequest()
					.header("Content-Type", "plain/text; charset=utf-8")
					.body(result.getFieldError("nome").getDefaultMessage());
		}

		estilo = cadastroEstiloService.cadastrar(estilo);
		return ResponseEntity.ok().body(estilo);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public ModelAndView novo(Estilo estilo) {
		ModelAndView mv = new ModelAndView("estilo/CadastroEstilo");

		mv.addObject(estilo);

		return mv;
	}

	@RequestMapping(value = {"/novo", "{\\d+}"}, method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Estilo estilo, BindingResult results,
							   RedirectAttributes attributes) {

		if (results.hasErrors()) {
			return novo(estilo);
		}

		try {
			cadastroEstiloService.cadastrar(estilo);
		} catch (NomeEstiloJaCadastradoException e) {
			results.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(estilo);
		}

		attributes.addFlashAttribute("mensagem", "Estilo salvo com sucesso!");

		return new ModelAndView("redirect:/estilos/novo");
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Estilo estilo = estilos.findByCodigo(codigo);
		ModelAndView mv = novo(estilo);
		mv.addObject(estilo);
		return mv;
	}

	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable Long codigo){
		try {
			cadastroEstiloService.excluir(codigo);
		} catch (ImpossivelExcluirEntidadeException e) {
			return  ResponseEntity.badRequest()
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.body(e.getMessage());
		}

		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body("Estilo exclu??do com sucesso!");
	}
}
