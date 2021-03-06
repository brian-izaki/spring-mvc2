package com.projetojava.brewer.model;

import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.projetojava.brewer.validation.SKU;
import org.codehaus.groovy.util.StringUtil;
import org.thymeleaf.util.StringUtils;

@Entity
@Table(name = "cerveja")
public class Cerveja {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@SKU
	@NotBlank(message = "SKU é obrigatório")
	private String sku;
	
	@NotBlank(message = "Nome é obrigatório")
	private String nome;
	
	@NotBlank(message = "Descricao é obrigatório")
	@Size(message = "O máximo de caracters na descrição é 50",max = 50)
	private String descricao;
	
	@NotNull(message = "Valor é obrigatório")
	@DecimalMin(value = "0.05", message = "O valor mínimo da cerveja é de R$ 0,05")
	@DecimalMax(value = "9999999.99", message = "O valor máximo da cerveja é de R$ 9.999.999,99")
	private BigDecimal valor;
	
	@NotNull(message = "Teor alcoólico é obrigatório")
	@DecimalMax(value = "100", message = "A porcentagem de teor alcoólico não deve ultrapassar de 100%")
	@Column(name = "teor_alcoolico")
	private BigDecimal teorAcoolico;
	
	@NotNull(message = "A comissão é obrigatória")
	@DecimalMax(value = "90", message = "A comissão deve ser no máximo 90%")
	private BigDecimal comissao;
	
	@NotNull(message = "Quantidade em estoque é obrigatório")
	@Min(value = 1, message = "O estoque deve ter no mínimo 1 item")
	@Max(value = 1000, message = "O estoque máximo permitido é de 1000 itens")
	@Column(name = "quantidade_estoque")
	private int quantidadeEstoque;
	
	@NotNull(message = "Origem é obrigatória")
	@Enumerated(EnumType.STRING)
	private Origem origem;
	
	@NotNull(message = "Sabor é obrigatório")
	@Enumerated(EnumType.STRING)
	private Sabor sabor;
	
	@NotNull(message = "Estilo e obrigatório")
	@ManyToOne
	@JoinColumn(name = "codigo_estilo")
	private Estilo estilo;

	private String foto;
	
	@Column(name = "content_type")
	private String contentType;

	@Transient
	private boolean novaFoto;

	@PrePersist
	@PreUpdate
	private void prePersistUpdate() {
		sku = sku.toUpperCase();
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getTeorAcoolico() {
		return teorAcoolico;
	}

	public void setTeorAcoolico(BigDecimal teorAcoolico) {
		this.teorAcoolico = teorAcoolico;
	}

	public BigDecimal getComissao() {
		return comissao;
	}

	public void setComissao(BigDecimal comissao) {
		this.comissao = comissao;
	}

	public int getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(int quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public Sabor getSabor() {
		return sabor;
	}

	public void setSabor(Sabor sabor) {
		this.sabor = sabor;
	}

	public Estilo getEstilo() {
		return estilo;
	}

	public void setEstilo(Estilo estilo) {
		this.estilo = estilo;
	}
	
	
	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public boolean isNovaFoto() {
		return novaFoto;
	}

	public void setNovaFoto(boolean novaFoto) {
		this.novaFoto = novaFoto;
	}

	public String getFotoOuMock () {
		return !StringUtils.isEmpty(foto) ? foto : "cerveja-mock.png";
	}

	public boolean isNovo() {
		return codigo == null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cerveja other = (Cerveja) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
}
