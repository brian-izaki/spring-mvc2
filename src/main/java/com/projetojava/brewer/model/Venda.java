package com.projetojava.brewer.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "venda")
public class Venda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotNull(message = "Data de Criação é obrigatória")
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "valor_frete")
    private BigDecimal valorFrete;

    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto;

    @NotNull(message = "Valor Total é obrigatório")
    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    private String observacao;

    @Column(name = "data_hora_entrega")
    private LocalDateTime dataHoraEntrega;

    @NotNull(message = "Status da venda é obrigatório")
    @Enumerated(EnumType.STRING)
    private StatusVenda status = StatusVenda.ORCAMENTO;

    @NotNull(message = "O cliente é obrigatório")
    @ManyToOne
    @JoinColumn(name = "codigo_cliente")
    private Cliente cliente;

    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne
    @JoinColumn(name = "codigo_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<ItemVenda> itens;

    @Transient
    private String uuid;

    @Transient
    private LocalDate dataEntrega;

    @Transient
    private LocalTime horaEntrega;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public StatusVenda getStatus() {
        return status;
    }

    public void setStatus(StatusVenda status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getDataHoraEntrega() {
        return dataHoraEntrega;
    }

    public void setDataHoraEntrega(LocalDateTime dataHoraEntrega) {
        this.dataHoraEntrega = dataHoraEntrega;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public LocalTime getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(LocalTime horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isNova() {
        return codigo == null;
    }

    public void adicionarItens(List<ItemVenda> itens) {
        this.itens = itens;
        this.itens.forEach(i -> i.setVenda(this)); // para cada item está sendo setando este objeto Venda para ele. Para fazer referência.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        return Objects.equals(codigo, venda.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
