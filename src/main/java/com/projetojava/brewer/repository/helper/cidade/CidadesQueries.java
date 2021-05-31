package com.projetojava.brewer.repository.helper.cidade;

import com.projetojava.brewer.model.Cidade;
import com.projetojava.brewer.repository.filter.CidadeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CidadesQueries {

    public Page<Cidade> filtrar(CidadeFilter filter, Pageable pageable);

    Cidade buscaCidadeEstado(Long codigo);
}
