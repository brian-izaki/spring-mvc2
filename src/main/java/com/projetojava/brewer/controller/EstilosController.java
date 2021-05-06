package com.projetojava.brewer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projetojava.brewer.model.Estilo;
import com.projetojava.brewer.service.CadastroEstiloService;
import com.projetojava.brewer.service.exception.NomeEstiloJaCadastradoException;

@Controller
@RequestMapping("/estilo")
public class EstilosController {
	
	@Autowired
	private CadastroEstiloService cadastroEstiloService;
	
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
		
		return new ModelAndView("redirect:/estilo/novo");
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvar(@RequestBody @Valid Estilo estilo, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity
					.badRequest()
					.header("Content-Type", "plain/text; charset=utf-8")
					.body(result.getFieldError("nome").getDefaultMessage());					
		}
			
		try {
			estilo = cadastroEstiloService.cadastrar(estilo);
		} catch (NomeEstiloJaCadastradoException e) {
			return ResponseEntity
					.badRequest()
					.header("Content-Type", "plain/text; charset=utf-8")
					.body(e.getMessage());	
		}
		
		return ResponseEntity.ok().body(estilo);
		
	}
	
}
