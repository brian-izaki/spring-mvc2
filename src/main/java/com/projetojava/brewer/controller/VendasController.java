package com.projetojava.brewer.controller;

import com.projetojava.brewer.controller.validator.VendaValidator;
import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.model.Venda;
import com.projetojava.brewer.repository.Cervejas;
import com.projetojava.brewer.security.UsuarioSistema;
import com.projetojava.brewer.service.CadastroVendaService;
import com.projetojava.brewer.session.TabelaItensSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/vendas")
public class VendasController {

    @Autowired
    private Cervejas cervejas;

    @Autowired
    private TabelaItensSession tabelaItens;

    @Autowired
    private CadastroVendaService cadastroVendaService;

    @Autowired
    private VendaValidator vendaValidator;

    @InitBinder
    public void inicializarValidador(WebDataBinder binder) {
        binder.setValidator(vendaValidator);
    }

    @GetMapping("/novo")
    public ModelAndView novo(Venda venda) {
        ModelAndView mv = new ModelAndView("venda/CadastroVenda");

        if (StringUtils.isEmpty(venda.getUuid()))
            venda.setUuid(UUID.randomUUID().toString());

        mv.addObject("itens", venda.getItens());

        return mv;
    }

    @PostMapping("/novo")
    public ModelAndView salvar(Venda venda, BindingResult result, RedirectAttributes attributes,
                               @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
        venda.adicionarItens(tabelaItens.getItens(venda.getUuid()));
        venda.calcularValorTotal();

        vendaValidator.validate(venda, result);

        if (result.hasErrors()) {
            return novo(venda);
        }

        venda.setUsuario(usuarioSistema.getUsuario());

        cadastroVendaService.salvar(venda);
        attributes.addFlashAttribute("mensagem", "Venda feita com sucesso");

        return new ModelAndView("redirect:/vendas/novo");
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
                                             String uuid) {
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
        mv.addObject("valorTotal", tabelaItens.getValorTotal(uuid));
        return mv;
    }
}
