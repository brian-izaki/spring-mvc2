package com.projetojava.brewer.repository.helper.usuario;

import com.projetojava.brewer.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuariosQueries {

    public Optional<Usuario> porEmailEAtivo(String email);

    public List<String> permissoes(Usuario usuario);

}
