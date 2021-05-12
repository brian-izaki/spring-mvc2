package com.projetojava.brewer.repository.helper.cerveja;

import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.repository.filter.CervejaFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CervejasQueries {
    public List<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable);
}
