package com.projetojava.brewer.service;

import com.projetojava.brewer.service.event.cerveja.CervejaSalvaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetojava.brewer.model.Cerveja;
import com.projetojava.brewer.repository.Cervejas;

@Service
public class CadastroCervejaService {
	
	@Autowired
	private Cervejas cervejas;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Transactional
	public void salvar(Cerveja cerveja) {

		cervejas.save(cerveja);

		publisher.publishEvent(new CervejaSalvaEvent(cerveja));
	}
	
}
