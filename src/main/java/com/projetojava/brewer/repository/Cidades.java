package com.projetojava.brewer.repository;

import com.projetojava.brewer.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Cidades extends JpaRepository<Cidade, Long> {
    public List<Cidade> findByEstadoCodigo(Long codigoEstado);
}
