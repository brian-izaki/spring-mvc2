package com.projetojava.brewer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "cidade")
public class Cidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotBlank(message = "O nome da cidade é obrigatório")
    private String nome;

    @NotNull(message = "O estado é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY) // fetchtype impede que inicialize a busca por estado
    @JoinColumn(name = "codigo_estado")
    @JsonIgnore // não leva a cidade junto nos response
    private Estado estado;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cidade cidade = (Cidade) o;
        return Objects.equals(codigo, cidade.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    public boolean temEstado() {
        return estado != null;
    }
}
