package com.projetojava.brewer.repository.helper.estilo;

import com.projetojava.brewer.model.Estilo;
import com.projetojava.brewer.repository.filter.EstiloFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstilosQueries {

    public Page<Estilo> filtrar(EstiloFilter filter, Pageable pageable);

}
