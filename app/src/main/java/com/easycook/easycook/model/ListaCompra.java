package com.easycook.easycook.model;

import java.util.ArrayList;

/** Created by gabriel on 8/19/17. */
public class ListaCompra {

    private String titulo;
    private ArrayList<ItemListaCompra> itens;

    public ListaCompra() {}

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ArrayList<ItemListaCompra> getItens() {
        return itens;
    }

    public void setItens(ArrayList<ItemListaCompra> itens) {
        this.itens = itens;
    }

    public int getQuantidadeItens() {
        return this.itens.size();
    }

    public int getQuantidadeItensComprados() {
        int quantidade = 0;

        for (ItemListaCompra item : itens) {
            if (item.isComprado()){
                quantidade++;
            }
        }
        return quantidade;
    }
}
