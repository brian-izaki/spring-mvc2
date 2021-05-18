package com.projetojava.brewer.repository.helper.cidade;

import com.projetojava.brewer.model.Cidade;
import com.projetojava.brewer.repository.Cidades;
import com.projetojava.brewer.repository.filter.CidadeFilter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CidadesImpl implements CidadesQueries{

    @Autowired
    private Cidades cidades;

    @PersistenceContext
    private EntityManager manager;

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Cidade> filtrar(CidadeFilter filter) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cidade.class);

        filtrarCidades(filter, criteria);
        criteria.createAlias("estado", "e");

        return criteria.list();
    }

    private void filtrarCidades(CidadeFilter filter, Criteria criteria){
        if (filter == null)
            return;

        if (!StringUtils.isEmpty(filter.getNome()))
            criteria.add(Restrictions.ilike("nome", filter.getNome(), MatchMode.ANYWHERE));

        if (filter.getEstado() != null)
            criteria.add(Restrictions.eq("estado", filter.getEstado()));
    }

}
