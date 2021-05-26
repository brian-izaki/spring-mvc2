package com.projetojava.brewer.repository.helper.venda;

import com.projetojava.brewer.model.TipoPessoa;
import com.projetojava.brewer.model.Venda;
import com.projetojava.brewer.repository.filter.VendaFilter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class VendasImpl implements VendasQueries{

    @PersistenceContext
    EntityManager manager;

    @SuppressWarnings("unckecked")
    @Override
    @Transactional
    public List<Venda> filtrar(VendaFilter filter) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Venda.class);

        filtrarVendas(filter, criteria);

        return criteria.list();
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
