package com.easycook.easycook.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.easycook.easycook.R;
import com.easycook.easycook.util.ConstantsUsuario;
import com.parse.ParseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CadastroActivity extends AppCompatActivity {

    @BindView(R.id.tiet_nome) TextInputEditText mNome;
    @BindView(R.id.tiet_sobrenome) TextInputEditText mSobrenome;
    @BindView(R.id.tiet_email) TextInputEditText mEmail;
    @BindView(R.id.tiet_senha) TextInputEditText mSenha;
    @BindView(R.id.tiet_confirmacao_senha) TextInputEditText mConfirmacaoSenha;
    @BindView(R.id.radioGroup) RadioGroup mSexoRadioGroup;
    @BindView(R.id.rb_feminino) RadioButton mRadioFeminino;

    @BindView(R.id.til_nome) TextInputLayout mNomeLayout;
    @BindView(R.id.til_sobrenome) TextInputLayout mSobrenomeLayout;
    @BindView(R.id.til_email) TextInputLayout mEmailLayout;
    @BindView(R.id.til_senha) TextInputLayout mSenhaLayout;
    @BindView(R.id.til_confirmacao_senha) TextInputLayout mConfirmacaoSenhaLayout;

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        ButterKnife.bind(this);

        criarToolbar();
    }

    private void criarToolbar() {
        toolbar.setTitle(R.string.botao_cadastrar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.primary_text));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cadastrarUsuario(View view) {
        if (validarCamposTela()) {
            salvarUsuario();
        }
    }

    private boolean validarCamposTela() {

        boolean erroPreenchimento;

        ArrayList<TextInputLayout> camposParaValidar = new ArrayList<>();
        camposParaValidar.add(mNomeLayout);
        camposParaValidar.add(mSobrenomeLayout);
        camposParaValidar.add(mEmailLayout);
        camposParaValidar.add(mSenhaLayout);
        camposParaValidar.add(mConfirmacaoSenhaLayout);

        for (TextInputLayout campo : camposParaValidar) {
            if (campo.getEditText() != null) {
                if (campo.getEditText().getText().toString().isEmpty()) {
                    campo.setErrorEnabled(true);
                    campo.setError(getString(R.string.campo_obrigatorio));
                } else {
                    campo.setError(null);
                    campo.setErrorEnabled(false);
                }
            }
        }

        if (!isEmailValido(mEmail.getText().toString())) {
            mEmailLayout.setErrorEnabled(true);
            mEmailLayout.setError("Email inválido");
        } else {
            mEmailLayout.setError(null);
            mEmailLayout.setErrorEnabled(false);
        }

        validarConfirmacaoSenha();

        erroPreenchimento = isSexoSelecionado();

        return erroPreenchimento;
    }

    private boolean isEmailValido(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void salvarUsuario() {
        ParseUser usuario = new ParseUser();
        usuario.put(ConstantsUsuario.NOME, mNome.getText().toString());
        usuario.put(ConstantsUsuario.SOBRENOME, mSobrenome.getText().toString());
        usuario.put(ConstantsUsuario.SEXO, getSexoSelecionado());
        usuario.setEmail(mEmail.getText().toString());
        usuario.setPassword(mSenha.getText().toString());
    }

    private boolean isSexoSelecionado() {
        if (mSexoRadioGroup.getCheckedRadioButtonId() <= 0) {
            Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content),
                    R.string.validacao_sexo, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        } else {
            mRadioFeminino.setError(null);
            return true;
        }
    }

    private void validarConfirmacaoSenha() {
        String senha = mSenha.getText().toString();
        String confirmacaoSenha = mConfirmacaoSenha.getText().toString();

        if (!senha.equals(confirmacaoSenha)) {
            mConfirmacaoSenhaLayout.setError(getResources().getString(R.string.validacao_senha));
        } else {
            mConfirmacaoSenhaLayout.setError(null);
        }
    }

    private String getSexoSelecionado (){
        switch (mSexoRadioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_masculino:
                return String.valueOf(ConstantsUsuario.MASCULINO);
            case R.id.rb_feminino:
                return String.valueOf(ConstantsUsuario.FEMININO);
        }
        return null;
    }
}
