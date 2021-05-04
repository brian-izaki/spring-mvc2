package com.projetojava.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {
	
	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public String novo() {
		return "usuario/CadastroUsuario";
	}
	
}
