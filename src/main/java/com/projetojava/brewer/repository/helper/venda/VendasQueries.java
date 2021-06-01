package com.projetojava.brewer.repository.helper.venda;

import com.projetojava.brewer.dto.VendaMes;
import com.projetojava.brewer.dto.VendaOrigem;
import com.projetojava.brewer.model.Venda;
import com.projetojava.brewer.repository.filter.VendaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface VendasQueries {

    public Page<Venda> filtrar(VendaFilter filter, Pageable pageable);

    Venda buscarComItens(Long codigo);

    BigDecimal valorTotalNoAno();

    BigDecimal valorTotalNoMes();

    BigDecimal valorTicketMedioNoAno();

    public List<VendaMes> totalPorMes();

    List<VendaOrigem> porOrigem();
}
