package com.projetojava.brewer.repository;

import com.projetojava.brewer.model.Cidade;
import com.projetojava.brewer.repository.helper.cidade.CidadesQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Cidades extends JpaRepository<Cidade, Long>, CidadesQueries {
    public List<Cidade> findByEstadoCodigo(Long codigoEstado);

    public Optional<Cidade> findByNomeAndEstadoCodigo(String nome, Long estado);
}
