package com.projetojava.brewer.repository.helper.venda;

import com.projetojava.brewer.model.Venda;
import com.projetojava.brewer.repository.filter.VendaFilter;

import java.util.List;

public interface VendasQueries {

    public List<Venda> filtrar(VendaFilter filter);

}
