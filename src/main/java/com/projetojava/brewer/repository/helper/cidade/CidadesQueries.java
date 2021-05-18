package com.projetojava.brewer.repository.helper.cidade;

import com.projetojava.brewer.model.Cidade;
import com.projetojava.brewer.repository.filter.CidadeFilter;

import java.util.List;

public interface CidadesQueries {

    public List<Cidade> filtrar(CidadeFilter filter);

}
