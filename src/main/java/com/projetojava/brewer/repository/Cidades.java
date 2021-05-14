package com.projetojava.brewer.repository;

import com.projetojava.brewer.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Cidades extends JpaRepository<Cidade, Long> {
    public List<Cidade> findByEstadoCodigo(Long codigoEstado);
}
