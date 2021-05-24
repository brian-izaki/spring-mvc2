package com.projetojava.brewer.controller;

import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.repository.Cervejas;
import com.projetojava.brewer.session.TabelaItensVenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/vendas")
public class VendasController {

    @Autowired
    private Cervejas cervejas;

    @Autowired
    private TabelaItensVenda tabelaItensVenda;

    @GetMapping("/novo")
    public String novo() {
        return "venda/CadastroVenda";
    }

    @PostMapping("/item")
    public ModelAndView adicionarItem(Long codigoCerveja) {
        Cerveja cerveja = cervejas.findOne(codigoCerveja);
        tabelaItensVenda.adicionarItem(cerveja, 1);

        return mvTabelaItens();
    }

    // está sendo realizado um findOne automático. O Spring e o JPA que realizam essa integração
    // foi necessário adicionar uma configuração no WEBConfig para habilitar esta funcionalidade.
    @PutMapping("/item/{codigoCerveja}")
    public ModelAndView alterarQuatidadeItem(@PathVariable("codigoCerveja") Cerveja cerveja,
                                             Integer quantidade){
        tabelaItensVenda.alterarQuantidadeItens(cerveja, quantidade);
        return mvTabelaItens();
    }

    @DeleteMapping("/item/{codigoCerveja}")
    public ModelAndView excluirItem(@PathVariable("codigoCerveja") Cerveja cerveja) {
        tabelaItensVenda.excluirItem(cerveja);
        return mvTabelaItens();
    }

    private ModelAndView mvTabelaItens() {
        ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
        mv.addObject("itens", tabelaItensVenda.getItens());
        return mv;
    }
}
