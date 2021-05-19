package com.projetojava.brewer.service;

import com.projetojava.brewer.model.Usuario;
import com.projetojava.brewer.repository.Usuarios;
import com.projetojava.brewer.service.exception.EmailUsuarioJaExistenteException;
import com.projetojava.brewer.service.exception.SenhaObrigatoriaUsuarioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CadastroUsuarioService {

    @Autowired
    private Usuarios usuarios;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public void salvar(Usuario usuario) {
        Optional<Usuario> emailOptional = usuarios.findByEmailIgnoreCase(usuario.getEmail());

        if (emailOptional.isPresent()) {
            throw new EmailUsuarioJaExistenteException("E-mail digitado já existe");
        }

        if (usuario.isNovo()) {
            if (StringUtils.isEmpty(usuario.getSenha()))
                throw new SenhaObrigatoriaUsuarioException("Senha é obrigatória para novo usuário");

            usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
            usuario.setConfirmacaoSenha(usuario.getSenha());
        }

        usuarios.save(usuario);
    }

}
