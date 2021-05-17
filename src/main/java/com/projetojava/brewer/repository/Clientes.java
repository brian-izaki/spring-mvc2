package com.projetojava.brewer.repository;

import com.projetojava.brewer.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Clientes extends JpaRepository<Cliente, Long> {

    public Optional<Cliente> findByCpfOuCnpj(String cpfOuCnpj);
}
