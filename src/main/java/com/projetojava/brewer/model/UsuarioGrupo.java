package com.projetojava.brewer.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "usuario_grupo")
public class UsuarioGrupo implements Serializable {

    private static final long serialVersionUID = 1L;

    // por essa tabela ter chave composta foi criado essa nova classe.
    // embeddedId diz que o id está na classe criada
    @EmbeddedId
    private UsuarioGrupoId id;

    public UsuarioGrupoId getId() {
        return id;
    }

    public void setId(UsuarioGrupoId id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioGrupo that = (UsuarioGrupo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
