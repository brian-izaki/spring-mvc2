package com.projetojava.brewer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projetojava.brewer.model.Estilo;
import com.projetojava.brewer.service.CadastroEstiloService;

@Controller
@RequestMapping("/estilo")
public class EstiloController {
	
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
		
		cadastroEstiloService.cadastrar(estilo);
		
		attributes.addFlashAttribute("mensagem", "Estilo Cadastrado com sucesso!");
		
		return new ModelAndView("redirect:/estilo/novo");
	}
	
}
