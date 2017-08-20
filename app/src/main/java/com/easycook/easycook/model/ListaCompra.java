package com.easycook.easycook.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

/** Created by gabriel on 8/19/17. */
@ParseClassName("ListaCompra")
public class ListaCompra extends ParseObject {

    private String titulo;

    private int quantidadeTotal;
    private int quantidadeComprada;

    public ListaCompra() {}

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(int quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public int getQuantidadeComprada() {
        return quantidadeComprada;
    }

    public void setQuantidadeComprada(int quantidadeComprada) {
        this.quantidadeComprada = quantidadeComprada;
    }
}
