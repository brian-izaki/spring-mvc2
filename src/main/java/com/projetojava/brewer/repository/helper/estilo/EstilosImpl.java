package com.projetojava.brewer.repository.helper.estilo;

import com.projetojava.brewer.model.Estilo;
import com.projetojava.brewer.repository.filter.EstiloFilter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

public class EstilosImpl implements EstilosQueries{

    @PersistenceContext
    private EntityManager manager;

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public Page<Estilo> filtrar(EstiloFilter filter, Pageable pageable) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Estilo.class);

        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;

        criteria.setFirstResult(primeiroRegistro);
        criteria.setMaxResults(totalRegistrosPorPagina);

        aplicarFiltro(filter, criteria);

        return new PageImpl(criteria.list(), pageable, total(filter));
    }

    private Long total(EstiloFilter filter) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Estilo.class);
        aplicarFiltro(filter, criteria);
        criteria.setProjection(Projections.rowCount());

        return (Long) criteria.uniqueResult();

    }

    private void aplicarFiltro(EstiloFilter filter, Criteria criteria) {
        if (filter.getNome() != null)
            criteria.add(Restrictions.ilike("nome", filter.getNome(), MatchMode.ANYWHERE));
    }

}
