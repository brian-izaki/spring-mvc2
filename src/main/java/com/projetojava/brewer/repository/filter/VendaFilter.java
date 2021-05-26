package com.projetojava.brewer.repository.filter;

import com.projetojava.brewer.model.Cliente;
import com.projetojava.brewer.model.StatusVenda;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VendaFilter {

    private Long codigo;
    private StatusVenda statusVenda;
    private LocalDate dataDe;
    private LocalDate dataAte;
    private BigDecimal valorDe;
    private BigDecimal valorAte;
    private String nomeCliente;
    private String cpfOuCnpj;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public StatusVenda getStatusVenda() {
        return statusVenda;
    }

    public void setStatusVenda(StatusVenda statusVenda) {
        this.statusVenda = statusVenda;
    }

    public LocalDate getDataDe() {
        return dataDe;
    }

    public void setDataDe(LocalDate dataDe) {
        this.dataDe = dataDe;
    }

    public LocalDate getDataAte() {
        return dataAte;
    }

    public void setDataAte(LocalDate dataAte) {
        this.dataAte = dataAte;
    }

    public BigDecimal getValorDe() {
        return valorDe;
    }

    public void setValorDe(BigDecimal valorDe) {
        this.valorDe = valorDe;
    }

    public BigDecimal getValorAte() {
        return valorAte;
    }

    public void setValorAte(BigDecimal valorAte) {
        this.valorAte = valorAte;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getCpfOuCnpj() {
        return cpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        this.cpfOuCnpj = cpfOuCnpj;
    }
}
