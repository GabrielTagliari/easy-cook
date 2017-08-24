package com.easycook.easycook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.easycook.easycook.R;
import com.easycook.easycook.adapter.NovaListaCompraAdapter;
import com.easycook.easycook.decoration.SimpleDividerItemDecoration;
import com.easycook.easycook.model.Produto;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListaCompraActivity extends AppCompatActivity {

    @BindView(R.id.tb_add_lista) Toolbar toolbar;
    @BindView(R.id.rv_itens_compra) RecyclerView mListItemCompra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compra);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArrayList<Produto> produtos = new ArrayList<>();

        Produto produto1 = new Produto();
        produto1.setNome("Teste");

        produtos.add(produto1);

        Produto produto2 = new Produto();
        produto2.setNome("Teste 2");

        produtos.add(produto2);

        mListItemCompra.setAdapter(new NovaListaCompraAdapter(produtos, this));
        mListItemCompra.addItemDecoration(new SimpleDividerItemDecoration(this));

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mListItemCompra.setLayoutManager(layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_lista, menu);
        return super.onCreateOptionsMenu(menu);
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
}
