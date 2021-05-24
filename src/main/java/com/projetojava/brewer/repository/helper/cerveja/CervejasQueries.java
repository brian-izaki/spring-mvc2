package com.projetojava.brewer.repository.helper.cerveja;

import com.projetojava.brewer.dto.CervejaDTO;
import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.repository.filter.CervejaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CervejasQueries {
    public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable);

    public List<CervejaDTO> porSkuOuNome(String skuOuNome);

}
