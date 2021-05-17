package com.projetojava.brewer.model;

import com.projetojava.brewer.model.validation.group.CfpGroup;
import com.projetojava.brewer.model.validation.group.CnpjGroup;

public enum TipoPessoa {
    FISICA("Física", "CPF", "000.000.000-00", CfpGroup.class),
    JURIDICA("Jurídica", "CNPJ", "00.000.000/0000-00", CnpjGroup.class);

    private final String descricao;
    private final String documento;
    private final String mascara;
    private final Class<?> grupo;

    TipoPessoa(String descricao, String documento, String mascara, Class<?> grupo) {
        this.descricao = descricao;
        this.documento = documento;
        this.mascara = mascara;
        this.grupo = grupo;
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

    public Class<?> getGrupo() {
        return grupo;
    }

    public static String removerFormatacao(String cpfOuCnpj) {
        return cpfOuCnpj.replaceAll("\\.|-|\\/", "");
    }
}
