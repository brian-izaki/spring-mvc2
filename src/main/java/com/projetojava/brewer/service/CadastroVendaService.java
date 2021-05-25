package com.projetojava.brewer.service;

import com.projetojava.brewer.model.ItemVenda;
import com.projetojava.brewer.model.Venda;
import com.projetojava.brewer.repository.Vendas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CadastroVendaService {

    @Autowired
    private Vendas vendas;

    @Transactional
    public void salvar(Venda venda) {

        if(venda.isNova()) {
            venda.setDataCriacao(LocalDateTime.now());
        }

        BigDecimal valorTotalItens = venda.getItens().stream()
                .map(ItemVenda::getValorTotal)
                .reduce(BigDecimal::add)
                .get();

        BigDecimal valorTotal = calcularValorTotal(valorTotalItens, venda.getValorFrete(),
                venda.getValorDesconto());
        venda.setValorTotal(valorTotal);

        if (venda.getDataEntrega() != null) {
            venda.setDataHoraEntrega(LocalDateTime.of(venda.getDataEntrega(), venda.getHoraEntrega()));
        }

        vendas.save(venda);
    }

    private BigDecimal calcularValorTotal(BigDecimal valorTotalItens,
                                          BigDecimal valorFrete,
                                          BigDecimal valorDesconto) {
        return valorTotalItens
                .add(Optional.ofNullable(valorFrete).orElse(BigDecimal.ZERO))
                .subtract(Optional.ofNullable(valorDesconto).orElse(BigDecimal.ZERO));
    }

}
