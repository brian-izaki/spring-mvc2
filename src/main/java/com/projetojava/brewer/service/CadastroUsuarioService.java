package com.projetojava.brewer.service;

import com.projetojava.brewer.model.Usuario;
import com.projetojava.brewer.repository.Usuarios;
import com.projetojava.brewer.service.exception.EmailUsuarioJaExistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CadastroUsuarioService {

    @Autowired
    private Usuarios usuarios;

    @Transactional
    public void salvar(Usuario usuario) {
        Optional<Usuario> emailOptional = usuarios.findByEmailIgnoreCase(usuario.getEmail());

        if (emailOptional.isPresent()) {
            throw new EmailUsuarioJaExistenteException("E-mail digitado já existe");
        }

        usuarios.save(usuario);
    }

}
