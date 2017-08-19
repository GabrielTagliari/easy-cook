package com.easycook.easycook.util;

import java.util.HashMap;

/** Created by gabriel on 19/8/17. */
public class ParseErros {

    private HashMap<Integer, String> erros;

    public ParseErros() {
        this.erros = new HashMap<>();
        this.erros.put(201, "Preencher a senha");
        this.erros.put(125, "InvalidEmailAddress");
        this.erros.put(150, "InvalidImageData");
        this.erros.put(150, "InvalidImageData");
        this.erros.put(203, "UserEmailTaken");
        this.erros.put(124, "RequestTimeout");
        this.erros.put(202, "Usuário já existe, escolha outro nome de usuário");
    }

    public String getErro(int codErro) {
        return this.erros.get(codErro);
    }
}
