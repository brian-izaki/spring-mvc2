package com.projetojava.brewer.controller;

import com.projetojava.brewer.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {
	
	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public ModelAndView novo(Usuario usuario) {
		return new ModelAndView("usuario/CadastroUsuario");
	}

	@PostMapping(value = "/novo")
	public ModelAndView salvar(@Valid Usuario usuario, BindingResult result) {
		if (result.hasErrors()) {
			return novo(usuario);
		}

		return new ModelAndView("redirect:/usuarios/novo");
	}
}
