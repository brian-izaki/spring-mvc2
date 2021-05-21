package com.projetojava.brewer.controller;

import com.projetojava.brewer.controller.page.PageWrapper;
import com.projetojava.brewer.model.Usuario;
import com.projetojava.brewer.repository.Grupos;
import com.projetojava.brewer.repository.Usuarios;
import com.projetojava.brewer.repository.filter.UsuarioFilter;
import com.projetojava.brewer.service.CadastroUsuarioService;
import com.projetojava.brewer.service.StatusUsuario;
import com.projetojava.brewer.service.exception.EmailUsuarioJaExistenteException;
import com.projetojava.brewer.service.exception.SenhaObrigatoriaUsuarioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;

	@Autowired
	private Usuarios usuarios;

	@Autowired
	private Grupos grupos;

	@GetMapping
	public ModelAndView pesquisar(UsuarioFilter filter, @PageableDefault(size = 3) Pageable pageable,
								  HttpServletRequest httpServletRequest){
		ModelAndView mv = new ModelAndView("usuario/PesquisaUsuarios");
		mv.addObject("grupos", grupos.findAll());
		PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(usuarios.filtrar(filter, pageable)
				, httpServletRequest);

		mv.addObject("usuariosPagina", paginaWrapper);

		return mv;
	}

	@GetMapping(value = "/novo")
	public ModelAndView novo(Usuario usuario) {
		ModelAndView mv = new ModelAndView("usuario/CadastroUsuario");
		mv.addObject("grupos", grupos.findAll()	);

		return mv;
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
		} catch (SenhaObrigatoriaUsuarioException e) {
			result.rejectValue("senha", e.getMessage(), e.getMessage());
			return novo(usuario);
		}

		attributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");

		return new ModelAndView("redirect:/usuarios/novo");
	}

	@PutMapping("/status")
	@ResponseStatus(HttpStatus.OK)
	public void atualizarStatus(@RequestParam("codigos") Long[] codigos, @RequestParam("status") StatusUsuario statusUsuario) {
		cadastroUsuarioService.alterarStatus(codigos, statusUsuario);
	}
}
