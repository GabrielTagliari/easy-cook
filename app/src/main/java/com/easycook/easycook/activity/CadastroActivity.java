package com.easycook.easycook.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.easycook.easycook.R;
import com.easycook.easycook.model.Usuario;
import com.easycook.easycook.util.ConstantsUsuario;
import com.easycook.easycook.validator.ValidatorFields;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

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

    private ProgressDialog progressDialog;
    private ValidatorFields validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        ButterKnife.bind(this);

        criarToolbar();

        progressDialog = new ProgressDialog(this, R.style.ProgressDialog);
        progressDialog.setMessage(getString(R.string.progress_carregando));

        validator = new ValidatorFields(this);
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
        if (validator.validarConfirmacaoSenha(mSenhaLayout, mConfirmacaoSenhaLayout)) {
            if (validarCamposTela()) {
                salvarUsuario();
            }
        }
    }

    private boolean validarCamposTela() {
        ArrayList<TextInputLayout> camposParaValidar = new ArrayList<>();
        camposParaValidar.add(mNomeLayout);
        camposParaValidar.add(mSobrenomeLayout);
        camposParaValidar.add(mEmailLayout);
        camposParaValidar.add(mSenhaLayout);
        camposParaValidar.add(mConfirmacaoSenhaLayout);

        boolean camposValidados = validator.validarCamposObrigatorios(camposParaValidar);

        boolean campoEmailValidado = validator.validarCampoEmail(mEmailLayout);

        return camposValidados && campoEmailValidado & isSexoSelecionado();
    }

    private void salvarUsuario() {
        ParseUser usuario = new ParseUser();
        usuario.put(ConstantsUsuario.NOME, mNome.getText().toString());
        usuario.put(ConstantsUsuario.SOBRENOME, mSobrenome.getText().toString());
        usuario.put(ConstantsUsuario.SEXO, getSexoSelecionado());
        usuario.setEmail(mEmail.getText().toString());
        usuario.setPassword(mSenha.getText().toString());
        usuario.setUsername(mNome.getText().toString());

        progressDialog.show();

        usuario.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    progressDialog.dismiss();
                    abrirTelaPrincipal();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(CadastroActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
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

    private String getSexoSelecionado (){
        switch (mSexoRadioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_masculino:
                return String.valueOf(ConstantsUsuario.MASCULINO);
            case R.id.rb_feminino:
                return String.valueOf(ConstantsUsuario.FEMININO);
        }
        return null;
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}