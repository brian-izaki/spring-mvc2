package com.projetojava.brewer.service;

import com.projetojava.brewer.model.ItemVenda;
import com.projetojava.brewer.model.Venda;
import com.projetojava.brewer.repository.Vendas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        if (venda.getDataEntrega() != null) {
            venda.setDataHoraEntrega(LocalDateTime.of(venda.getDataEntrega(),
                    venda.getHoraEntrega() != null ? venda.getHoraEntrega() : LocalTime.NOON));
        }

        vendas.save(venda);
    }
}
