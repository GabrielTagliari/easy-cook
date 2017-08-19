package com.easycook.easycook.validator;

import android.content.Context;
import android.support.design.widget.TextInputLayout;

import com.easycook.easycook.R;

import java.util.ArrayList;

/** Created by gabriel on 8/19/17. */
public class ValidatorFields {

    private Context context;

    public ValidatorFields(Context context) {
        this.context = context;
    }

    public boolean validarCamposObrigatorios(ArrayList<TextInputLayout> camposParaValidar) {

        boolean camposVazios = false;

        for (TextInputLayout campo : camposParaValidar) {
            if (campo.getEditText() != null) {
                if (campo.getEditText().getText().toString().isEmpty()) {
                    campo.setErrorEnabled(true);
                    campo.setError(context.getString(R.string.campo_obrigatorio));
                    camposVazios = true;
                } else {
                    campo.setError(null);
                    campo.setErrorEnabled(false);
                }
            }
        }

        return !camposVazios;
    }

    public boolean validarCampoEmail (TextInputLayout emailLayout) {
        if (emailLayout.getEditText() == null) {
            return false;
        }

        if (!isEmailValido(emailLayout.getEditText().getText().toString())) {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError(context.getString(R.string.email_invalido));
            return false;
        } else {
            emailLayout.setError(null);
            emailLayout.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validarConfirmacaoSenha(
            TextInputLayout senhaLayout, TextInputLayout confirmacaoSenhaLayout) {

        if (senhaLayout.getEditText() != null && confirmacaoSenhaLayout.getEditText() != null) {
            String senha = senhaLayout.getEditText().getText().toString();
            String confirmacaoSenha = confirmacaoSenhaLayout.getEditText().getText().toString();

            if (!senha.equals(confirmacaoSenha)) {
                confirmacaoSenhaLayout.setErrorEnabled(true);
                confirmacaoSenhaLayout.setError(context.getResources().getString(R.string.validacao_senha));
            } else {
                confirmacaoSenhaLayout.setError(null);
                confirmacaoSenhaLayout.setErrorEnabled(false);
                return true;
            }
        }

        return false;
    }

    private boolean isEmailValido(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
