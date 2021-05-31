package com.projetojava.brewer.controller;

import com.projetojava.brewer.controller.page.PageWrapper;
import com.projetojava.brewer.controller.validator.VendaValidator;
import com.projetojava.brewer.dto.VendaMes;
import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.model.ItemVenda;
import com.projetojava.brewer.model.StatusVenda;
import com.projetojava.brewer.model.Venda;
import com.projetojava.brewer.repository.Cervejas;
import com.projetojava.brewer.repository.Vendas;
import com.projetojava.brewer.repository.filter.VendaFilter;
import com.projetojava.brewer.security.UsuarioSistema;
import com.projetojava.brewer.service.CadastroVendaService;
import com.projetojava.brewer.session.TabelaItensSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
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

    @Autowired
    private Vendas vendas;

    @InitBinder("venda")
    public void inicializarValidador(WebDataBinder binder) {
        binder.setValidator(vendaValidator);
    }

    @GetMapping
    public ModelAndView pesquisar(VendaFilter filter, BindingResult result,
                                  @PageableDefault(size = 5) Pageable pageable,
                                  HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("venda/PesquisaVendas");

        PageWrapper<Venda> pagina = new PageWrapper(vendas.filtrar(filter, pageable), httpServletRequest);

        mv.addObject("paginaVendas", pagina);
        mv.addObject("statusVenda", StatusVenda.values());

        return mv;
    }

    @GetMapping("/{codigo}")
    public ModelAndView editar(@PathVariable Long codigo) {
        Venda venda = vendas.buscarComItens(codigo);

        setUuid(venda);
        for(ItemVenda item : venda.getItens()) {
            tabelaItens.adicionarItem(venda.getUuid(), item.getCerveja(), item.getQuantidade());
        }
        
        ModelAndView mv = novo(venda);
        mv.addObject(venda);
        return mv;
    }

    @GetMapping("/novo")
    public ModelAndView novo(Venda venda) {
        ModelAndView mv = new ModelAndView("venda/CadastroVenda");

        setUuid(venda);

        mv.addObject("itens", venda.getItens());
        mv.addObject("valorFrete", venda.getValorFrete());
        mv.addObject("valorDesconto", venda.getValorDesconto());
        mv.addObject("valorTotalItens", tabelaItens.getValorTotal(venda.getUuid()));

        return mv;
    }

    @PostMapping(value = "/novo", params = "salvar")
    public ModelAndView salvar(Venda venda, BindingResult result, RedirectAttributes attributes,
                               @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
        validarVenda(venda, result);

        if (result.hasErrors()) {
            return novo(venda);
        }

        venda.setUsuario(usuarioSistema.getUsuario());

        cadastroVendaService.salvar(venda);
        attributes.addFlashAttribute("mensagem", "Venda feita com sucesso");

        return new ModelAndView("redirect:/vendas/novo");
    }

    @PostMapping(value = "/novo", params = "emitir")
    public ModelAndView emitir(Venda venda, BindingResult result, RedirectAttributes attributes,
                               @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
        validarVenda(venda, result);

        if (result.hasErrors()) {
            return novo(venda);
        }

        venda.setUsuario(usuarioSistema.getUsuario());

        cadastroVendaService.emitir(venda);
        attributes.addFlashAttribute("mensagem", "Venda emitida com sucesso");

        return new ModelAndView("redirect:/vendas/novo");
    }

    @PostMapping(value = "/novo", params = "enviarEmail")
    public ModelAndView enviarEmail(Venda venda, BindingResult result, RedirectAttributes attributes,
                                    @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
        validarVenda(venda, result);

        if (result.hasErrors()) {
            return novo(venda);
        }

        venda.setUsuario(usuarioSistema.getUsuario());

        cadastroVendaService.salvar(venda);
        attributes.addFlashAttribute("mensagem", "Venda feita com sucesso");

        return new ModelAndView("redirect:/vendas/novo");
    }

    @PostMapping(value = "/novo", params = "cancelar")
    public ModelAndView cancelar(Venda venda, BindingResult result, RedirectAttributes attributes,
                                    @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
        try {
            cadastroVendaService.cancelar(venda);
        } catch (AccessDeniedException e) {
            return new ModelAndView("/403");
        }

        attributes.addFlashAttribute("mensagem", "Venda cancelada com sucesso");
        return new ModelAndView("redirect:/vendas/" + venda.getCodigo());
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

    @GetMapping("/totalPorMes")
    public @ResponseBody List <VendaMes> listarTotalVendaPorMes() {
        return vendas.totalPorMes();
    }

    private ModelAndView mvTabelaItens(String uuid) {
        ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
        mv.addObject("itens", tabelaItens.getItens(uuid));
        mv.addObject("valorTotal", tabelaItens.getValorTotal(uuid));
        return mv;
    }

    private void validarVenda(Venda venda, BindingResult result) {
        venda.adicionarItens(tabelaItens.getItens(venda.getUuid()));
        venda.calcularValorTotal();

        vendaValidator.validate(venda, result);
    }

    private void setUuid(Venda venda) {
        if (StringUtils.isEmpty(venda.getUuid()))
            venda.setUuid(UUID.randomUUID().toString());
    }
}
