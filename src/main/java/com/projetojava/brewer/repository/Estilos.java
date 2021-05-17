package com.projetojava.brewer.repository;

import java.util.Optional;

import com.projetojava.brewer.repository.helper.estilo.EstilosQueries;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projetojava.brewer.model.Estilo;
import org.springframework.stereotype.Repository;

@Repository
public interface Estilos extends JpaRepository<Estilo, Long>, EstilosQueries {

    Optional<Estilo> findByNomeIgnoreCase(String nome);

}
