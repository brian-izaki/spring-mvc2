package com.projetojava.brewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetojava.brewer.model.Estilo;
import com.projetojava.brewer.repository.Estilos;

@Service
public class CadastroEstiloService {
	
	@Autowired
	Estilos estilos;
	
	@Transactional
	public void cadastrar(Estilo estilo) {
		estilos.save(estilo);
	}
	
}
