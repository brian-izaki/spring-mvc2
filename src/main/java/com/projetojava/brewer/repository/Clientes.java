package com.projetojava.brewer.repository;

import com.projetojava.brewer.model.Cliente;
import com.projetojava.brewer.repository.helper.cliente.ClientesQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Clientes extends JpaRepository<Cliente, Long>, ClientesQuery {

    public Optional<Cliente> findByCpfOuCnpj(String cpfOuCnpj);
}
