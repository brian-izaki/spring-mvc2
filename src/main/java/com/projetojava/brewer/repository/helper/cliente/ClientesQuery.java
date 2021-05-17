package com.projetojava.brewer.repository.helper.cliente;

import com.projetojava.brewer.model.Cliente;
import com.projetojava.brewer.repository.filter.ClienteFilter;

import java.util.List;

public interface ClientesQuery {

    public List<Cliente> filtrar(ClienteFilter filter);

}
