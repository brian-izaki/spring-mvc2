package com.projetojava.brewer.controller;

import com.projetojava.brewer.controller.page.PageWrapper;
import com.projetojava.brewer.model.Cliente;
import com.projetojava.brewer.model.TipoPessoa;
import com.projetojava.brewer.repository.Clientes;
import com.projetojava.brewer.repository.Estados;
import com.projetojava.brewer.repository.filter.ClienteFilter;
import com.projetojava.brewer.service.CadastroClienteService;
import com.projetojava.brewer.service.exception.CpfCnpjClienteJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClientesController {

    @Autowired
    private Estados estados;

    @Autowired
    private Clientes clientes;

    @Autowired
    private CadastroClienteService cadastroClienteService;

    @GetMapping
    public ModelAndView pesquisa(ClienteFilter filter, BindingResult result,
								 @PageableDefault(size = 5) Pageable pageable,
								 HttpServletRequest httpServletRequest) {

        ModelAndView mv = new ModelAndView("cliente/PesquisaCliente");

        PageWrapper<Cliente> clientePagina = new PageWrapper<>(clientes.filtrar(filter, pageable), httpServletRequest);

        mv.addObject("paginaClientes", clientePagina);

        return mv;
    }

    @RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody List<Cliente> pesquisa(String nome) {
        validarTamanhoNOme(nome);

        return clientes.findByNomeStartingWithIgnoreCase(nome);
    }

    @RequestMapping(value = "/novo", method = RequestMethod.GET)
    public ModelAndView novo(Cliente cliente) {
        ModelAndView mv = new ModelAndView("cliente/CadastroCliente");
        mv.addObject("tiposPessoa", TipoPessoa.values());
        mv.addObject("estados", estados.findAll());
        return mv;
    }

    @GetMapping("/{codigo}")
    public ModelAndView editar(@PathVariable Long codigo) {
        Cliente cliente = clientes.buscaComEstado(codigo);
        ModelAndView mv = novo(cliente);
        mv.addObject(cliente);
        return mv;
    }

    @RequestMapping(value = {"/novo", "{\\d+}"}, method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return novo(cliente);
        }

        try {
            cadastroClienteService.salvar(cliente);
        } catch (CpfCnpjClienteJaCadastradoException e) {
            result.rejectValue("cpfOuCnpj", e.getMessage(), e.getMessage());
            return novo(cliente);
        }

        attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso");
        return new ModelAndView("redirect:/clientes/novo");
    }


    private void validarTamanhoNOme(String nome) {
        if(StringUtils.isEmpty(nome) || nome.length() < 3) {
            throw new IllegalArgumentException("Nome passado está vazio ou tem menos de 3 caracteres.");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> tratarIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
