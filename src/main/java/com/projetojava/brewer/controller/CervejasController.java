package com.projetojava.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.projetojava.brewer.controller.page.PageWrapper;
import com.projetojava.brewer.dto.CervejaDTO;
import com.projetojava.brewer.repository.Cervejas;
import com.projetojava.brewer.repository.filter.CervejaFilter;
import com.projetojava.brewer.service.exception.ImpossivelExcluirEntidadeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.model.Origem;
import com.projetojava.brewer.model.Sabor;
import com.projetojava.brewer.repository.Estilos;
import com.projetojava.brewer.service.CadastroCervejaService;

import java.util.List;

@Controller
@RequestMapping("/cervejas")
public class CervejasController {

    @Autowired
    private Estilos estilos;

    @Autowired
    private CadastroCervejaService cadastroCervejaService;

    @Autowired
    private Cervejas cervejas;

    @RequestMapping("/novo")
    public ModelAndView novo(Cerveja cerveja) {
        ModelAndView mv = new ModelAndView("cerveja/CadastroCerveja");
        mv.addObject("sabores", Sabor.values());
        mv.addObject("estilos", estilos.findAll());
        mv.addObject("origens", Origem.values());

        return mv;
    }

    @RequestMapping(value = {"/novo", "{\\d+}"}, method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Cerveja cerveja, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return novo(cerveja);
        }

        cadastroCervejaService.salvar(cerveja);
        attributes.addFlashAttribute("mensagem", "Cerveja salva com sucesso!");


        return new ModelAndView("redirect:/cervejas/novo");
    }

    @GetMapping
    public ModelAndView pesquisar(CervejaFilter cervejaFilter, BindingResult result,
                                  @PageableDefault(size = 5) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("cerveja/PesquisaCervejas");
        mv.addObject("estilos", estilos.findAll());
        mv.addObject("sabores", Sabor.values());
        mv.addObject("origens", Origem.values());

        PageWrapper<Cerveja> paginaWrapper = new PageWrapper<>(cervejas.filtrar(cervejaFilter, pageable),
                httpServletRequest);

        mv.addObject("pagina", paginaWrapper);
        return mv;
    }

    @GetMapping("/{codigo}")
    public ModelAndView editar(@PathVariable("codigo") Cerveja cerveja) {
        ModelAndView mv = novo(cerveja);
        mv.addObject(cerveja);
        return mv;
    }

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<CervejaDTO> pesquisar(String skuOuNome) {
        return cervejas.porSkuOuNome(skuOuNome);
    }

    @DeleteMapping("/{codigo}")
    public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Cerveja cerveja) {

        try{
            cadastroCervejaService.excluir(cerveja);
        } catch (ImpossivelExcluirEntidadeException e) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body(e.getMessage());
        }

        return ResponseEntity.ok().body("Excluido com sucesso");
    }
}
