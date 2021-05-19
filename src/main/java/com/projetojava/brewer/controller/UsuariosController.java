package com.projetojava.brewer.controller;

import com.projetojava.brewer.model.Usuario;
import com.projetojava.brewer.service.CadastroUsuarioService;
import com.projetojava.brewer.service.exception.EmailUsuarioJaExistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;

	@GetMapping(value = "/novo")
	public ModelAndView novo(Usuario usuario) {
		return new ModelAndView("usuario/CadastroUsuario");
	}

	@PostMapping(value = "/novo")
	public ModelAndView salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(usuario);
		}

		try {
			cadastroUsuarioService.salvar(usuario);
		} catch (EmailUsuarioJaExistenteException e) {
			result.rejectValue("email", e.getMessage(), e.getMessage());
			return novo(usuario);
		}

		attributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");

		return new ModelAndView("redirect:/usuarios/novo");
	}
}
