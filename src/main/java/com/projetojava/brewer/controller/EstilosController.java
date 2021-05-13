package com.projetojava.brewer.controller;

import javax.validation.Valid;

import com.projetojava.brewer.repository.Estilos;
import com.projetojava.brewer.repository.filter.EstiloFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
	public ModelAndView pesquisar(EstiloFilter filtro) {
		ModelAndView mv = new ModelAndView("estilo/PesquisaEstilos");
		System.out.println("nome da pesquisa: " + filtro.getNome());
		mv.addObject("estilos", estilos.filtrar(filtro));

		return mv;
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvar(@RequestBody @Valid Estilo estilo, BindingResult result) {
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

	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Estilo estilo, BindingResult results, RedirectAttributes attributes) {

		if (results.hasErrors()) {
			return novo(estilo);
		}

		try {
			cadastroEstiloService.cadastrar(estilo);
		} catch (NomeEstiloJaCadastradoException e) {
			results.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(estilo);
		}

		attributes.addFlashAttribute("mensagem", "Estilo Cadastrado com sucesso!");

		return new ModelAndView("redirect:/estilos/novo");
	}

}
