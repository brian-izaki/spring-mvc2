package com.projetojava.brewer.controller.page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class PageWrapper<T> {
    private Page<T> page;
    private UriComponentsBuilder uriBuilder;

    public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
        this.page = page;
        String httpUrl = httpServletRequest
                .getRequestURL()
                .append( httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "")
                .toString()
                .replaceAll("\\+", "%20")
                .replaceAll("excluido", "");

        this.uriBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl);
    }

    public List<T> getConteudo() {
        return page.getContent();
    }

    public boolean isVazia() {
        return page.getContent().isEmpty();
    }

    public int getAtual() {
        return page.getNumber();
    }

    public boolean getPrimeira() {
        return page.isFirst();
    }

    public boolean getUltima() {
        return page.isLast();
    }

    public int getTotal() {
        return page.getTotalPages();
    }

    public String urlParaPagina(int pagina) {
        // constrói o parâmetro da query, e substitui o parâmetro "page" que está vindo pelo argumento que for passado.
        // o build se refere ao valores que vierem codificados pois nas urls não aceitam caracteres especiais.
        return uriBuilder
                .replaceQueryParam("page", pagina)
                .build(true)
                .encode()
                .toUriString();
    }

    public String urlOrdenada(String propriedade) {
        UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder
                .fromUriString(uriBuilder.build(true).encode().toUriString());

        String valorSort = String.format("%s,%s", propriedade, inverterDirecao(propriedade));

        return uriBuilderOrder
                .replaceQueryParam("sort", valorSort).build(true).encode().toUriString();
    }

    public boolean descendente(String propriedade){
        return inverterDirecao(propriedade).equals("asc");
    }

    public String inverterDirecao(String propriedade){
        String direcao = "asc";

        Sort.Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
        if (order != null) {
            direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc";
        }

        return direcao;
    }

    public boolean ordenada(String propriedade) {
        Sort.Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;

        return order != null;
    }

}
