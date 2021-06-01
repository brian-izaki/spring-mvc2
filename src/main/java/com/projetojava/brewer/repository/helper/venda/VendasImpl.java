package com.projetojava.brewer.repository.helper.venda;

import com.projetojava.brewer.dto.VendaMes;
import com.projetojava.brewer.dto.VendaOrigem;
import com.projetojava.brewer.model.StatusVenda;
import com.projetojava.brewer.model.TipoPessoa;
import com.projetojava.brewer.model.Venda;
import com.projetojava.brewer.repository.filter.VendaFilter;
import com.projetojava.brewer.repository.paginacao.PaginacaoUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Optional;

public class VendasImpl implements VendasQueries{

    @PersistenceContext
    EntityManager manager;

    @Autowired
    PaginacaoUtil paginacaoUtil;

    @SuppressWarnings("unckecked")
    @Override
    @Transactional
    public Page<Venda> filtrar(VendaFilter filter, Pageable pageable) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Venda.class);
        paginacaoUtil.preparar(criteria, pageable);
        filtrarVendas(filter, criteria);

        return new PageImpl(criteria.list(), pageable, total(filter));
    }

    @Override
    @Transactional
    public Venda buscarComItens(Long codigo) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Venda.class);
        criteria.createAlias("itens", "i", JoinType.LEFT_OUTER_JOIN);
        criteria.add(Restrictions.eq("codigo", codigo));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (Venda) criteria.uniqueResult();
    }

    @Override
    public BigDecimal valorTotalNoAno() {
        Optional<BigDecimal> optional = Optional.ofNullable(
                manager.createQuery("select sum(valorTotal) from Venda where year(dataCriacao) = :ano " +
                                "and status = :status",
                    BigDecimal.class) // resultado da consulta é do tipo BigDecimal
                    .setParameter("ano", Year.now().getValue())
                    .setParameter("status", StatusVenda.EMITIDA)
                    .getSingleResult());
        // caso não retorne nada, retornará um 0.
        return optional.orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal valorTotalNoMes() {
        Optional<BigDecimal> optional = Optional.ofNullable(
                manager.createQuery("select sum(valorTotal) from Venda where month(dataCriacao) = :mes " +
                                "and status = :status",
                        BigDecimal.class)
                        .setParameter("mes", MonthDay.now().getMonthValue())
                        .setParameter("status", StatusVenda.EMITIDA)
                        .getSingleResult());
        // caso não retorne nada, retornará um 0.
        return optional.orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal valorTicketMedioNoAno() {
        Optional<BigDecimal> optional = Optional.ofNullable(
                manager.createQuery("select sum(valorTotal)/count(*) from Venda where year(dataCriacao) = :ano " +
                                "and status = :status",
                        BigDecimal.class)
                        .setParameter("ano", Year.now().getValue())
                        .setParameter("status", StatusVenda.EMITIDA)
                        .getSingleResult());
        // caso não retorne nada, retornará um 0.
        return optional.orElse(BigDecimal.ZERO);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<VendaMes> totalPorMes() {
        List<VendaMes> vendasMes = manager.createNamedQuery("Vendas.totalPorMes").getResultList();

        LocalDate hoje = LocalDate.now();
        for (int i = 1; i <= 6; i++) {
            String mesIdeal = String.format("%d/%02d", hoje.getYear(), hoje.getMonthValue());
            boolean possuiMes = vendasMes.stream().filter(v -> v.getMes().equals(mesIdeal)).findAny().isPresent();
            if (!possuiMes) {
                vendasMes.add(i - 1, new VendaMes(mesIdeal, 0));
            }

            hoje = hoje.minusMonths(1);
        }

        return vendasMes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<VendaOrigem> porOrigem() {
        List<VendaOrigem> porOrigem = manager.createNamedQuery("Vendas.porOrigem").getResultList();

        LocalDate hoje = LocalDate.now();
        for (int i = 1; i <= 6; i++) {
            String mesIdeal = String.format("%d/%02d", hoje.getYear(), hoje.getMonthValue());
            boolean possuiMes = porOrigem.stream().filter(v -> v.getMes().equals(mesIdeal)).findAny().isPresent();
            if (!possuiMes) {
                porOrigem.add(i - 1, new VendaOrigem(mesIdeal, 0, 0));
            }

            hoje = hoje.minusMonths(1);
        }

        return porOrigem;
    }

    private Long total(VendaFilter filter) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Venda.class);
        filtrarVendas(filter, criteria);
        criteria.setProjection(Projections.rowCount());

        return (Long) criteria.uniqueResult();
    }

    private void filtrarVendas(VendaFilter filter, Criteria criteria) {
        if (filter == null)
            return;

        criteria.createAlias("cliente", "c");

        if (!StringUtils.isEmpty(filter.getCodigo()))
            criteria.add(Restrictions.eq("codigo",
                    filter.getCodigo()));

        if (filter.getStatusVenda() != null)
            criteria.add(Restrictions.eq("status",
                    filter.getStatusVenda()));

        if (filter.getDataDe() != null) {
            LocalDateTime dataHora = LocalDateTime.of(filter.getDataDe(), LocalTime.MIDNIGHT);
            criteria.add(Restrictions.ge("dataCriacao",
                    dataHora));
        }

        if (filter.getDataAte() != null){
            LocalDateTime dataHora = LocalDateTime.of(filter.getDataAte(), LocalTime.of(23, 59, 59));
            criteria.add(Restrictions.le("dataCriacao",
                    dataHora));
        }

        if (filter.getValorDe() != null)
            criteria.add(Restrictions.ge("valorTotal",
                    filter.getValorDe()));

        if (filter.getValorAte() != null)
            criteria.add(Restrictions.le("valorTotal",
                    filter.getValorAte()));

        if (!StringUtils.isEmpty(filter.getNomeCliente()))
            criteria.add(Restrictions.ilike("c.nome",
                    filter.getNomeCliente(), MatchMode.ANYWHERE));

        if (!StringUtils.isEmpty(filter.getCpfOuCnpj()))
            criteria.add(Restrictions.ilike("c.cpfOuCnpj",
                    TipoPessoa.removerFormatacao(filter.getCpfOuCnpj()), MatchMode.ANYWHERE));

    }
}
