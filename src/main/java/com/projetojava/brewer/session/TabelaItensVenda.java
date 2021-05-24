package com.projetojava.brewer.session;

import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.model.ItemVenda;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SessionScope
@Component
public class TabelaItensVenda {

    private List<ItemVenda> itens = new ArrayList<>();

    public BigDecimal getValorTotal() {
        return itens.stream()
                .map(ItemVenda::getValorTotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public void adicionarItem(Cerveja cerveja, Integer quantidade) {
        Optional<ItemVenda> itemVendaOptional = buscarItemPorCerveja(cerveja);

        ItemVenda itemVenda;
        if (itemVendaOptional.isPresent()) {
            itemVenda = itemVendaOptional.get();
            itemVenda.setQuantidade(itemVenda.getQuantidade() + quantidade);
        } else {
            itemVenda = new ItemVenda();
            itemVenda.setCerveja(cerveja);
            itemVenda.setQuantidade(quantidade);
            itemVenda.setValorUnitario(cerveja.getValor());
            itens.add(0, itemVenda);
        }
    }

    public void alterarQuantidadeItens(Cerveja cerveja, Integer quantidade) {
        ItemVenda itemVenda = buscarItemPorCerveja(cerveja).get();
        itemVenda.setQuantidade(quantidade);
    }

    public int total() {
        return itens.size();
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    private Optional<ItemVenda> buscarItemPorCerveja(Cerveja cerveja) {
        return itens.stream()
                .filter(item -> item.getCerveja().equals(cerveja))
                .findAny();
    }
}
