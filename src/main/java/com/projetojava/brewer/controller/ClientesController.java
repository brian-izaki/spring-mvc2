package com.projetojava.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/clientes")
public class ClientesController {
	
	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public String novo() {
		return "cliente/CadastroCliente";
	}
	
}
