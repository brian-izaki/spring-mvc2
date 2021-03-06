package com.projetojava.brewer.repository.helper.cerveja;

import com.projetojava.brewer.dto.CervejaDTO;
import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.repository.filter.CervejaFilter;
import com.projetojava.brewer.repository.paginacao.PaginacaoUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public class CervejasImpl implements CervejasQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true) // diz que é uma transação somente leitura
    public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cerveja.class);

        paginacaoUtil.preparar(criteria, pageable);

        adicionarFiltro(filtro, criteria);

        return new PageImpl(criteria.list(), pageable, total(filtro));
    }

    @Override
    public List<CervejaDTO> porSkuOuNome(String skuOuNome) {
        String jpql = "select new com.projetojava.brewer.dto.CervejaDTO(codigo, sku, nome, origem, valor, foto) " +
                "from Cerveja where lower(sku) like lower(:skuOuNome) or lower(nome) like lower(:skuOuNome) ";
        List<CervejaDTO> cervejasFiltradas = manager.createQuery(jpql, CervejaDTO.class)
                .setParameter("skuOuNome", skuOuNome + "%")
                .getResultList();
        return cervejasFiltradas;
    }

    @Override
    public Long totalQuantidadeEstoque() {
        String jpql = "select SUM(quantidadeEstoque) from Cerveja";
        Optional<Long> totalQuantidade = Optional.ofNullable(
                manager.createQuery(jpql, Long.class).getSingleResult());

        return totalQuantidade.orElse(0L);
    }

    @Override
    public BigDecimal totalValorEstoque() {
        String jpql = "select SUM(valor * quantidadeEstoque)  from Cerveja";
        Optional<BigDecimal> totalValor = Optional.ofNullable(
                manager.createQuery(jpql, BigDecimal.class).getSingleResult());

        return totalValor.orElse(BigDecimal.ZERO);
    }

    private Long total(CervejaFilter filtro) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cerveja.class);
        adicionarFiltro(filtro, criteria);
        criteria.setProjection(Projections.rowCount());

        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(CervejaFilter filtro, Criteria criteria) {
        if (filtro != null) {
            if (!StringUtils.isEmpty(filtro.getSku()))
                criteria.add(Restrictions.eq("sku", filtro.getSku()));

            if (!StringUtils.isEmpty(filtro.getNome()))
                criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));

            if (isEstiloPresente(filtro))
                criteria.add(Restrictions.eq("estilo", filtro.getEstilo()));

            if (filtro.getSabor() != null)
                criteria.add(Restrictions.eq("sabor", filtro.getSabor()));

            if (filtro.getOrigem() != null)
                criteria.add(Restrictions.eq("origem", filtro.getOrigem()));

            if (filtro.getValorDe() != null)
                criteria.add(Restrictions.ge("valor", filtro.getValorDe()));

            if (filtro.getValorAte() != null)
                criteria.add(Restrictions.le("valor", filtro.getValorAte()));

        }
    }

    private boolean isEstiloPresente(CervejaFilter filtro) {
        return filtro.getEstilo() != null && filtro.getEstilo().getCodigo() != null;
    }
}
