package com.projetojava.brewer.service;

import java.util.Optional;

import com.projetojava.brewer.service.exception.ImpossivelExcluirEntidadeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetojava.brewer.model.Estilo;
import com.projetojava.brewer.repository.Estilos;
import com.projetojava.brewer.service.exception.NomeEstiloJaCadastradoException;

import javax.persistence.PersistenceException;

@Service
public class CadastroEstiloService {
	
	@Autowired
	Estilos estilos;
	
	@Transactional
	public Estilo cadastrar(Estilo estilo) {
		
		Optional<Estilo> estiloOptional = estilos.findByNomeIgnoreCase(estilo.getNome());
		
		if(estiloOptional.isPresent()) {
			throw new NomeEstiloJaCadastradoException("Nome do estilo já cadastrado");
		}
		
		return estilos.saveAndFlush(estilo);
	}

	@Transactional
	public void excluir(Long codigo) {
		try{
			estilos.delete(codigo);
			estilos.flush();
		} catch (PersistenceException e){
			throw new ImpossivelExcluirEntidadeException("Não foi possível deletar o estilo, " +
					"ele já foi utilizado para cadastrar uma cerveja.");
		}
	}
}
