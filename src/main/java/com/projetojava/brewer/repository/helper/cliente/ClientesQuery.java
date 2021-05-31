package com.projetojava.brewer.repository.helper.cliente;

import com.projetojava.brewer.model.Cliente;
import com.projetojava.brewer.repository.filter.ClienteFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientesQuery {

    public Page<Cliente> filtrar(ClienteFilter filter, Pageable pageable);

    Cliente buscaComEstado(Long codigo);
}
