package com.projetojava.brewer.repository.helper.cerveja;

import com.projetojava.brewer.model.Estilo;
import com.projetojava.brewer.repository.filter.EstiloFilter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
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
    public List<Estilo> filtrar(EstiloFilter filter) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Estilo.class);

        if (filter.getNome() != null)
            criteria.add(Restrictions.ilike("nome", filter.getNome(), MatchMode.ANYWHERE));

        return criteria.list();
    }

}
