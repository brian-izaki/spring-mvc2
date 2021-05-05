package com.projetojava.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projetojava.brewer.model.Estilo;

public interface Estilos extends JpaRepository<Estilo, Long>{
	
}
