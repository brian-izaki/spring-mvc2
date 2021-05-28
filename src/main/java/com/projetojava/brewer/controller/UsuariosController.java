package com.projetojava.brewer.controller;

import com.projetojava.brewer.controller.page.PageWrapper;
import com.projetojava.brewer.model.Usuario;
import com.projetojava.brewer.repository.Grupos;
import com.projetojava.brewer.repository.Usuarios;
import com.projetojava.brewer.repository.filter.UsuarioFilter;
import com.projetojava.brewer.service.CadastroUsuarioService;
import com.projetojava.brewer.service.StatusUsuario;
import com.projetojava.brewer.service.exception.EmailUsuarioJaExistenteException;
import com.projetojava.brewer.service.exception.ImpossivelExcluirEntidadeException;
import com.projetojava.brewer.service.exception.SenhaObrigatoriaUsuarioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.print.attribute.standard.Media;
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

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Usuario usuario = usuarios.buscarComGrupos(codigo);

		ModelAndView mv = novo(usuario);
		mv.addObject(usuario);
		return mv;
	}

	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity excluir(@PathVariable("codigo") Usuario usuario) {

		try {
			cadastroUsuarioService.excluir(usuario);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8)
					.body(e.getMessage());
		} catch (AccessDeniedException e) {
			return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8)
					.body("Usuário logado não pode se auto excluir apenas editar");
		}

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
				.body("Usuário excluído com sucesso!");
	}

	@GetMapping(value = "/novo")
	public ModelAndView novo(Usuario usuario) {
		ModelAndView mv = new ModelAndView("usuario/CadastroUsuario");
		mv.addObject("grupos", grupos.findAll()	);

		return mv;
	}

	@PostMapping(value = {"/novo", "{\\d+}"})
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
