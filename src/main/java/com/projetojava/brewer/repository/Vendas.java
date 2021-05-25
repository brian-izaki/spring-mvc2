package com.projetojava.brewer.repository;

import com.projetojava.brewer.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Vendas extends JpaRepository<Venda, Long> {

}
