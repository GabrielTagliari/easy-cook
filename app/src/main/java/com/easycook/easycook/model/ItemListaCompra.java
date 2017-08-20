package com.easycook.easycook.model;

/** Created by gabriel on 8/19/17. */
class ItemListaCompra {

    private String nome;
    private String categoria;
    private Double preco;
    private boolean comprado;

    public ItemListaCompra() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    boolean isComprado() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }
}
