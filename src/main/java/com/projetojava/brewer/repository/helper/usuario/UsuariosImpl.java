package com.projetojava.brewer.repository.helper.usuario;

import com.projetojava.brewer.model.Grupo;
import com.projetojava.brewer.model.Usuario;
import com.projetojava.brewer.model.UsuarioGrupo;
import com.projetojava.brewer.repository.filter.UsuarioFilter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuariosImpl implements UsuariosQueries{

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Optional<Usuario> porEmailEAtivo(String email) {
        // está sendo utilizado o JPQL por isso não utiliza o select, e ele está se referindo à model e não tabela
        return manager
                .createQuery("from Usuario where lower(email) = lower(:email) and ativo = true", Usuario.class)
                .setParameter("email", email).getResultList().stream().findFirst();
    }

    @Override
    public List<String> permissoes(Usuario usuario) {
        return manager.createQuery(
                "select DISTINCT p.nome from Usuario u " +
                        "inner join u.grupos g " +
                        "inner join g.permissoes p " +
                        "where u = :usuario", String.class)
                .setParameter("usuario", usuario)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> filtrar(UsuarioFilter filter) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Usuario.class);

        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        adicionarFiltro(filter, criteria);

        return criteria.list();
    }

    private void adicionarFiltro(UsuarioFilter filter, Criteria criteria) {
        if (filter == null)
            return;

        if (!StringUtils.isEmpty(filter.getNome()))
            criteria.add(Restrictions.ilike("nome", filter.getNome(), MatchMode.ANYWHERE));

        if (!StringUtils.isEmpty(filter.getEmail()))
            criteria.add(Restrictions.ilike("email", filter.getEmail(), MatchMode.ANYWHERE));

        criteria.createAlias("grupos", "g", JoinType.LEFT_OUTER_JOIN);
        if(filter.getGrupos() != null && !filter.getGrupos().isEmpty()) {
            List<Criterion> subqueries = new ArrayList<>();
            for (Long codigoGrupo : filter.getGrupos().stream().mapToLong(Grupo::getCodigo).toArray()) {
                DetachedCriteria dc = DetachedCriteria.forClass(UsuarioGrupo.class);
                dc.add(Restrictions.eq("id.grupo.codigo", codigoGrupo));
                dc.setProjection(Projections.property("id.usuario"));

                subqueries.add(Subqueries.propertyIn("codigo", dc));
            }

            Criterion[] criterions = new Criterion[subqueries.size()];
            criteria.add(Restrictions.and(subqueries.toArray(criterions)));
        }


    }

}
