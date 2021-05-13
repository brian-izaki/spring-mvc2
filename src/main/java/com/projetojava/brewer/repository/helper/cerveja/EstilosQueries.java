package com.projetojava.brewer.repository.helper.cerveja;

import com.projetojava.brewer.model.Estilo;
import com.projetojava.brewer.repository.filter.EstiloFilter;

import java.util.List;

public interface EstilosQueries {


    public List<Estilo> filtrar(EstiloFilter filter);
}
