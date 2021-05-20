package com.projetojava.brewer.repository.filter;

import com.projetojava.brewer.model.Grupo;

public class UsuarioFilter {

    private String nome;
    private String email;
    private Grupo grupos;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Grupo getGrupos() {
        return grupos;
    }

    public void setGrupos(Grupo grupos) {
        this.grupos = grupos;
    }
}
