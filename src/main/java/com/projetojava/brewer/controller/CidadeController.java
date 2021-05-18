package com.projetojava.brewer.controller;

import com.projetojava.brewer.controller.page.PageWrapper;
import com.projetojava.brewer.model.Cidade;
import com.projetojava.brewer.repository.Cidades;
import com.projetojava.brewer.repository.Estados;
import com.projetojava.brewer.repository.filter.CidadeFilter;
import com.projetojava.brewer.service.CadastroCidadeService;
import com.projetojava.brewer.service.exception.NomeCidadeJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private Cidades cidades;

    @Autowired
    private Estados estados;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @GetMapping
    public ModelAndView pesquisar(CidadeFilter filter,
                                  @PageableDefault(size = 5) Pageable pageable,
                                  HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("cidade/PesquisaCidades");

        mv.addObject("estados", estados.findAll());

        PageWrapper<Cidade> pagina = new PageWrapper<>(cidades.filtrar(filter, pageable), httpServletRequest);
        mv.addObject("paginaCidades", pagina);

        return mv;
    }

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Cidade> pesquisarPorCodigoEstado(
            @RequestParam(name = "estado", defaultValue = "-1") Long codigoEstado) {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return cidades.findByEstadoCodigo(codigoEstado);
    }

    @RequestMapping("/novo")
    public ModelAndView novo(Cidade cidade) {

        ModelAndView mv = new ModelAndView("cidade/CadastroCidade");

        mv.addObject("estados", estados.findAll());

        return mv;
    }

    @PostMapping("/novo")
    public ModelAndView salvar(@Valid Cidade cidade, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return novo(cidade);
        }

        try {
            cadastroCidadeService.salvar(cidade);
        } catch (NomeCidadeJaCadastradoException e) {
            result.rejectValue("nome", e.getMessage(), e.getMessage());
            return novo(cidade);
        }

        attributes.addFlashAttribute("mensagem", "Cidade cadastrada com sucesso");
        return new ModelAndView("redirect:/cidades/novo");
    }

}
