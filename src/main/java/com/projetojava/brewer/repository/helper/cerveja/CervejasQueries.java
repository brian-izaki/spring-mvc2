package com.projetojava.brewer.repository.helper.cerveja;

import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.repository.filter.CervejaFilter;

import java.util.List;

public interface CervejasQueries {
    public List<Cerveja> filtrar(CervejaFilter filtro);
}
