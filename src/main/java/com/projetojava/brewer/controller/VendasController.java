package com.projetojava.brewer.controller;

import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.repository.Cervejas;
import com.projetojava.brewer.session.TabelaItensSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/vendas")
public class VendasController {

    @Autowired
    private Cervejas cervejas;

    @Autowired
    private TabelaItensSession tabelaItens;

    @GetMapping("/novo")
    public ModelAndView novo() {
        ModelAndView mv = new ModelAndView("venda/CadastroVenda");

        mv.addObject("uuid", UUID.randomUUID().toString());

        return mv;
    }

    @PostMapping("/item")
    public ModelAndView adicionarItem(Long codigoCerveja, String uuid) {
        Cerveja cerveja = cervejas.findOne(codigoCerveja);
        tabelaItens.adicionarItem(uuid, cerveja, 1);

        return mvTabelaItens(uuid);
    }

    // está sendo realizado um findOne automático. O Spring e o JPA que realizam essa integração
    // foi necessário adicionar uma configuração no WEBConfig para habilitar esta funcionalidade.
    @PutMapping("/item/{codigoCerveja}")
    public ModelAndView alterarQuatidadeItem(@PathVariable("codigoCerveja") Cerveja cerveja,
                                             Integer quantidade,
                                             String uuid){
        tabelaItens.alterarQuantidadeItens(uuid, cerveja, quantidade);
        return mvTabelaItens(uuid);
    }

    @DeleteMapping("/item/{uuid}/{codigoCerveja}")
    public ModelAndView excluirItem(@PathVariable("codigoCerveja") Cerveja cerveja,
                                    @PathVariable("uuid") String uuid) {
        tabelaItens.excluirItem(uuid, cerveja);
        return mvTabelaItens(uuid);
    }

    private ModelAndView mvTabelaItens(String uuid) {
        ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
        mv.addObject("itens", tabelaItens.getItens(uuid));
        return mv;
    }
}
