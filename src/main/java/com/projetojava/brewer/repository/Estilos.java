package com.projetojava.brewer.repository;

import java.util.Optional;

import com.projetojava.brewer.repository.helper.cerveja.EstilosQueries;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projetojava.brewer.model.Estilo;

public interface Estilos extends JpaRepository<Estilo, Long>, EstilosQueries {

    Optional<Estilo> findByNomeIgnoreCase(String nome);

}
