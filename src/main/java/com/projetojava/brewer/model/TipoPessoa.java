package com.projetojava.brewer.model;

public enum TipoPessoa {
    FISICA("Física", "CPF", "000.000.000-00"),
    JURIDICA("Jurídica", "CNPJ", "00.000.000/0000-00");

    private final String descricao;
    private final String documento;
    private final String mascara;

    TipoPessoa(String descricao, String documento, String mascara) {
        this.descricao = descricao;
        this.documento = documento;
        this.mascara = mascara;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDocumento() {
        return documento;
    }

    public String getMascara() {
        return mascara;
    }

}
