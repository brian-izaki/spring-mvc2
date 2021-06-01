package com.projetojava.brewer.service;

import com.projetojava.brewer.model.ItemVenda;
import com.projetojava.brewer.model.StatusVenda;
import com.projetojava.brewer.model.Venda;
import com.projetojava.brewer.repository.Vendas;
import com.projetojava.brewer.service.event.venda.VendaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private ApplicationEventPublisher publisher;

    @Transactional
    public void salvar(Venda venda) {

        if (venda.isSalvarProibido()) {
            throw new RuntimeException("Usuário tentando salvar uma venda proibida");
        }

        if(venda.isNova()) {
            venda.setDataCriacao(LocalDateTime.now());
        } else {
            Venda vendaExistente = vendas.findOne(venda.getCodigo());
            venda.setDataCriacao(vendaExistente.getDataCriacao());
        }

        if (venda.getDataEntrega() != null) {
            venda.setDataHoraEntrega(LocalDateTime.of(venda.getDataEntrega(),
                    venda.getHoraEntrega() != null ? venda.getHoraEntrega() : LocalTime.NOON));
        }

        vendas.save(venda);
    }

    @Transactional
    public void emitir(Venda venda) {
        venda.setStatus(StatusVenda.EMITIDA);
        salvar(venda);

        publisher.publishEvent(new VendaEvent(venda));
    }

    @PreAuthorize("#venda.usuario == principal.usuario or hasRole('CANCELAR_VENDA')")
    @Transactional
    public void cancelar(Venda venda) {
        Venda vendaExistente = vendas.findOne(venda.getCodigo());

        vendaExistente.setStatus(StatusVenda.CANCELADA);
        vendas.save(vendaExistente);
    }
}
