package com.projetojava.brewer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projetojava.brewer.model.validation.ClienteGroupSequenceProvider;
import com.projetojava.brewer.model.validation.group.CpfGroup;
import com.projetojava.brewer.model.validation.group.CnpjGroup;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "cliente")
@GroupSequenceProvider(ClienteGroupSequenceProvider.class)
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Tipo pessoa é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa")
    private TipoPessoa tipoPessoa;

    @NotBlank(message = "CPF/CNPJ é obrigatório")
    @CPF(groups = CpfGroup.class)
    @CNPJ(groups = CnpjGroup.class)
    @Column(name = "cpf_cnpj")
    private String cpfOuCnpj;

    private String telefone;

    @NotNull(message = "E-mail é obrigatório")
    @Email(message = "E-mail está inválido")
    private String email;

    @Embedded
    @JsonIgnore
    private Endereco endereco;

    @PrePersist @PreUpdate
    private void prePersistPreUpdate() {
        this.cpfOuCnpj = this.cpfOuCnpj.replaceAll("\\.|-|\\/", "");
    }

    @PostLoad
    private void postLoad() {
        this.cpfOuCnpj = this.tipoPessoa.formatar(this.cpfOuCnpj);
    }

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

    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getCpfOuCnpj() {
        return cpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        this.cpfOuCnpj = cpfOuCnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getCpfOuCnpjSemFormatacao() {
        return TipoPessoa.removerFormatacao(this.cpfOuCnpj);
    }

    public boolean isNovo() {
        return this.codigo == null;
    }

    public boolean isComCidade() {
        return this.endereco.getCidade() != null && this.endereco.getCidade().getEstado() != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(codigo, cliente.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
