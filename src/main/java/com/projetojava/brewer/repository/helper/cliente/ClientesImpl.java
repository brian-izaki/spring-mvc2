package com.projetojava.brewer.repository.helper.cliente;

import com.projetojava.brewer.model.Cliente;
import com.projetojava.brewer.repository.filter.ClienteFilter;
import com.projetojava.brewer.repository.paginacao.PaginacaoUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ClientesImpl implements ClientesQuery{

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> filtrar(ClienteFilter filter, Pageable pageable) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cliente.class);

        paginacaoUtil.preparar(criteria, pageable);

        adicionarFiltro(filter, criteria);

        return new PageImpl(criteria.list(), pageable, total(filter));
    }

    private Long total(ClienteFilter filtro) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cliente.class);
        adicionarFiltro(filtro, criteria);

        criteria.setProjection(Projections.rowCount());

        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(ClienteFilter filter, Criteria criteria) {
        if (!StringUtils.isEmpty(filter.getNome()))
            criteria.add(Restrictions.ilike("nome", filter.getNome(), MatchMode.ANYWHERE));

        if (!StringUtils.isEmpty(filter.getCpfOuCnpj()))
            criteria.add(Restrictions.eq("cpfOuCnpj", filter.getCpfOuCnpj()));
    }

}
