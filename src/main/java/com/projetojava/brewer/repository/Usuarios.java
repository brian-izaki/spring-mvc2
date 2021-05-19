package com.projetojava.brewer.repository;

import com.projetojava.brewer.model.Usuario;
import com.projetojava.brewer.repository.helper.usuario.UsuariosQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Usuarios extends JpaRepository<Usuario, Long>, UsuariosQueries {

    public Optional<Usuario> findByEmailIgnoreCase(String email);

}
