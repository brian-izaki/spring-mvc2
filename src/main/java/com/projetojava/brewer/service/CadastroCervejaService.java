package com.projetojava.brewer.service;

import com.projetojava.brewer.service.event.cerveja.CervejaSalvaEvent;
import com.projetojava.brewer.service.exception.ImpossivelExcluirEntidadeException;
import com.projetojava.brewer.storage.FotoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.repository.Cervejas;

import javax.persistence.PersistenceException;

@Service
public class CadastroCervejaService {
	
	@Autowired
	private Cervejas cervejas;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private FotoStorage fotoStorage;

	@Transactional
	public void salvar(Cerveja cerveja) {

		cervejas.save(cerveja);

		publisher.publishEvent(new CervejaSalvaEvent(cerveja));
	}

	@Transactional
	public void excluir(Cerveja cerveja) {
		try {
			String foto = cerveja.getFoto();
			cervejas.delete(cerveja);
			cervejas.flush();

			fotoStorage.excluir(foto);
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar cerveja. Já foi feita uma venda com ela");
		}
	}
	
}
