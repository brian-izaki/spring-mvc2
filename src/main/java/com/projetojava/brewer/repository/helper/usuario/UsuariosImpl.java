package com.projetojava.brewer.repository.helper.usuario;

import com.projetojava.brewer.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

public class UsuariosImpl implements UsuariosQueries{

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Optional<Usuario> porEmailEAtivo(String email) {
        // está sendo utilizado o JPQL por isso não utiliza o select, e ele está se referindo à model e não tabela
        return manager
                .createQuery("from Usuario where lower(email) = lower(:email) and ativo = true", Usuario.class)
                .setParameter("email", email).getResultList().stream().findFirst();
    }

}
