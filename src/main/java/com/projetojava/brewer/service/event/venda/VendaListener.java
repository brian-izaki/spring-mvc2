package com.projetojava.brewer.service.event.venda;

import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.model.ItemVenda;
import com.projetojava.brewer.repository.Cervejas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class VendaListener {

    @Autowired
    Cervejas cervejas;

    @EventListener
    public void vendaEmitida(VendaEvent vendaEvent) {
        for(ItemVenda item : vendaEvent.getVenda().getItens()) {
            Cerveja cerveja = cervejas.findOne(item.getCerveja().getCodigo());
            cerveja.setQuantidadeEstoque(cerveja.getQuantidadeEstoque() - item.getQuantidade());
            cervejas.save(cerveja);
        }
    };

}
