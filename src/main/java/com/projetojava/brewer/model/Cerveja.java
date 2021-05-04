package com.projetojava.brewer.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public class Cerveja {
	
	@NotBlank(message = "SKU é obrigatório")
	private String sku;
	
	@NotBlank(message = "Nome é obrigatório")
	private String nome;
	
	@NotBlank(message = "Descricao é obrigatório")
	@Size(message = "O máximo de caracters na descrição é 50",max = 50)
	private String descricao;
	
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
