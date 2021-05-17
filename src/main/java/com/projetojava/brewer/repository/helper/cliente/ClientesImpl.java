package com.projetojava.brewer.repository.helper.cliente;

import com.projetojava.brewer.model.Cliente;
import com.projetojava.brewer.repository.filter.ClienteFilter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class ClientesImpl implements ClientesQuery{

    @PersistenceContext
    private EntityManager manager;

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> filtrar(ClienteFilter filter) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cliente.class);

        if (!StringUtils.isEmpty(filter.getNome()))
            criteria.add(Restrictions.ilike("nome", filter.getNome(), MatchMode.ANYWHERE));

        if (!StringUtils.isEmpty(filter.getCpfOuCnpj()))
            criteria.add(Restrictions.eq("cpfOuCnpj", filter.getCpfOuCnpj()));

        return criteria.list();
    }

}
