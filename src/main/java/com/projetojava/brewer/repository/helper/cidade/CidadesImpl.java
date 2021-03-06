package com.projetojava.brewer.repository.helper.cidade;

import com.projetojava.brewer.model.Cidade;
import com.projetojava.brewer.model.Cliente;
import com.projetojava.brewer.repository.Cidades;
import com.projetojava.brewer.repository.filter.CidadeFilter;
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

public class CidadesImpl implements CidadesQueries{

    @Autowired
    private Cidades cidades;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @PersistenceContext
    private EntityManager manager;

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Page<Cidade> filtrar(CidadeFilter filter, Pageable pageable) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cidade.class);

        paginacaoUtil.preparar(criteria, pageable);
        adicionarFiltro(filter, criteria);

        criteria.createAlias("estado", "e");

        return new PageImpl(criteria.list(), pageable, total(filter));
    }

    @Override
    @Transactional(readOnly = true)
    public Cidade buscaCidadeEstado(Long codigo) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cidade.class);
        criteria.createAlias("estado", "e");
        criteria.add(Restrictions.eq("codigo", codigo));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return (Cidade) criteria.uniqueResult();
    }

    private Long total(CidadeFilter filtro) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cidade.class);
        adicionarFiltro(filtro, criteria);

        criteria.setProjection(Projections.rowCount());

        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(CidadeFilter filter, Criteria criteria){
        if (filter == null)
            return;

        if (!StringUtils.isEmpty(filter.getNome()))
            criteria.add(Restrictions.ilike("nome", filter.getNome(), MatchMode.ANYWHERE));

        if (filter.getEstado() != null)
            criteria.add(Restrictions.eq("estado", filter.getEstado()));
    }

}
