package com.projetojava.brewer.model;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemVenda {

    private Long codigo;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private Cerveja cerveja;

    public BigDecimal getValorTotal() {
        return valorUnitario.multiply(new BigDecimal(quantidade));
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Cerveja getCerveja() {
        return cerveja;
    }

    public void setCerveja(Cerveja cerveja) {
        this.cerveja = cerveja;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemVenda itemVenda = (ItemVenda) o;
        return Objects.equals(codigo, itemVenda.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
