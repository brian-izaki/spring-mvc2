package com.projetojava.brewer.repository;

import com.projetojava.brewer.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface Estados extends JpaRepository<Estado, BigInteger> {

}
