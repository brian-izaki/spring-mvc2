package com.projetojava.brewer.service;

import com.projetojava.brewer.model.Cliente;
import com.projetojava.brewer.repository.Clientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroClienteService {

    @Autowired
    private Clientes clientes;

    @Transactional
    public void salvar(Cliente cliente) {
        clientes.save(cliente);
    }

}
