package com.projetojava.brewer.repository.helper.venda;

import com.projetojava.brewer.model.Venda;
import com.projetojava.brewer.repository.filter.VendaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendasQueries {

    public Page<Venda> filtrar(VendaFilter filter, Pageable pageable);

}
