package com.projetojava.brewer.repository.helper.usuario;

import com.projetojava.brewer.model.Usuario;
import com.projetojava.brewer.repository.filter.UsuarioFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsuariosQueries {

    public Optional<Usuario> porEmailEAtivo(String email);

    public List<String> permissoes(Usuario usuario);

    public Page<Usuario> filtrar(UsuarioFilter filter, Pageable pageable);

}
