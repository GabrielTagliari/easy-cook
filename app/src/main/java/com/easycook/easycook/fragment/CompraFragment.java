package com.easycook.easycook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.easycook.easycook.R;
import com.easycook.easycook.adapter.CompraRecyclerViewAdapter;
import com.easycook.easycook.decoration.SimpleDividerItemDecoration;
import com.easycook.easycook.model.ListaCompra;
import com.easycook.easycook.util.ConstantsUsuario;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CompraFragment extends Fragment {

    /* Constantes */
    public static final String LISTA_COMPRA = "ListaCompra";
    public static final String TITULO = "titulo";

    /* Atributos */
    private List<ListaCompra> listasCompras = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;
    private CompraRecyclerViewAdapter adapter;
    private ParseQuery<ParseObject> query;

    @BindView(R.id.toolbar_compra) Toolbar toolbar;
    @BindView(R.id.rv_compras) RecyclerView recyclerView;

    public CompraFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compra_main, container, false);

        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.listas_compras);

        if (recyclerView != null) {
            Context context = view.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
            adapter = new CompraRecyclerViewAdapter(listasCompras, mListener);
            recyclerView.setAdapter(adapter);
        }

        getListasCompras();

        return view;
    }

    public List<ListaCompra> getListasCompras() {
        query = ParseQuery.getQuery(LISTA_COMPRA);
        query.whereEqualTo(ConstantsUsuario.USUARIO, ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        listasCompras.clear();

                        for (ParseObject parseObject : objects) {
                            ListaCompra listaCompra = new ListaCompra();
                            listaCompra.setTitulo(parseObject.getString(TITULO));
                            listaCompra.setUsuario(parseObject.getString(ConstantsUsuario.USUARIO));
                            listaCompra.setQuantidadeComprada(0);
                            listaCompra.setQuantidadeTotal(10);

                            listasCompras.add(listaCompra);
                        }

                        recyclerView.swapAdapter(adapter, false);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        return listasCompras;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_lista:
                abrirTelaAdicionarLista();
                return true;
            case R.id.action_remove_lista:
                removerTodasListas();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removerTodasListas() {
        query = ParseQuery.getQuery("ListaCompra");
        query.whereEqualTo(ConstantsUsuario.USUARIO, ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
            if (e == null) {
                if (objects.size() > 0) {
                    listasCompras.clear();

                    for (ParseObject parseObject : objects) {
                        parseObject.deleteEventually();
                    }

                    getListasCompras();
                }
            } else {
                e.printStackTrace();
            }
            }
        });
    }

    private void abrirTelaAdicionarLista() {
        MockSalvar();
    }

    private void MockSalvar() {
        ListaCompra lista = new ListaCompra();
        lista.setTitulo("Churrasco");
        lista.put(ConstantsUsuario.USUARIO, ParseUser.getCurrentUser().getObjectId());

        final ParseObject listaCompra = ParseObject.create("ListaCompra");
        listaCompra.put("titulo", lista.getTitulo());
        listaCompra.put(ConstantsUsuario.USUARIO, ParseUser.getCurrentUser().getObjectId());

        final ParseObject parseProduto1 = ParseObject.create("Produto");
        parseProduto1.put("nome", "Morango");
        parseProduto1.put("categoria", "Fruta");
        parseProduto1.put("preco", 12.00);
        parseProduto1.put("comprado", false);

        parseProduto1.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            if (e != null) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                final ParseObject parseProduto2 = ParseObject.create("Produto");
                parseProduto2.put("nome", "Banana");
                parseProduto2.put("categoria", "Fruta");
                parseProduto2.put("preco", 15.00);
                parseProduto2.put("comprado", true);

                parseProduto2.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        ParseRelation<ParseObject> relation = listaCompra.getRelation("itemListaCompra");
                        relation.add(parseProduto1);
                        relation.add(parseProduto2);
                        listaCompra.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "Sucesso ao salvar", Toast.LENGTH_SHORT).show();
                                getListasCompras();
                            } else {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            }
                        });
                    }
                    }
                });
            }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ListaCompra item);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(isVisible());
    }
}
