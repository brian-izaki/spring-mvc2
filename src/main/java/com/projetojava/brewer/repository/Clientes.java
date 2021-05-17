package com.projetojava.brewer.repository;

import com.projetojava.brewer.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Clientes extends JpaRepository<Cliente, Long> {

}
