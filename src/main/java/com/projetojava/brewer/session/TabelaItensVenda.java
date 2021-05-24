package com.projetojava.brewer.session;

import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.model.ItemVenda;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        ItemVenda itemVenda = new ItemVenda();
        itemVenda.setCerveja(cerveja);
        itemVenda.setQuantidade(quantidade);
        itemVenda.setValorUnitario(cerveja.getValor());

        itens.add(itemVenda);
    }

    public int total() {
        return itens.size();
    }

    public Object getItens() {
        return itens;
    }
}
