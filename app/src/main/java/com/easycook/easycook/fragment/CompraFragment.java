package com.easycook.easycook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.easycook.easycook.R;
import com.easycook.easycook.adapter.CompraRecyclerViewAdapter;
import com.easycook.easycook.decoration.SimpleDividerItemDecoration;
import com.easycook.easycook.model.ListaCompra;
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

    // TODO: Customize parameters
    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;

    private List<ListaCompra> listasCompras = new ArrayList<>();

    private ParseQuery<ParseObject> query;

    @BindView(R.id.toolbar_compra) Toolbar toolbar;
    @BindView(R.id.rv_compras) RecyclerView recyclerView;

    private CompraRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CompraFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_compra_main, container, false);

        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.listas_compras);

        //populaListaTeste();

        // Set the adapter
        if (recyclerView != null) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new CompraRecyclerViewAdapter(listasCompras, mListener);
            recyclerView.setAdapter(adapter);
        }

        getListasCompras();

        return view;
    }

    public List<ListaCompra> getListasCompras() {
        query = ParseQuery.getQuery("ListaCompra");
        query.whereEqualTo("usuario", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {
                        listasCompras.clear();

                        for (ParseObject parseObject : objects) {
                            ListaCompra listaCompra = new ListaCompra();
                            listaCompra.setTitulo(parseObject.getString("titulo"));
                            listaCompra.setUsuario(parseObject.getString("usuario"));
                            listaCompra.setQuantidadeComprada(0);
                            listaCompra.setQuantidadeTotal(10);

                            listasCompras.add(listaCompra);
                        }

                        //adapter.notifyDataSetChanged();

                        recyclerView.swapAdapter(adapter, false);
                    }

                } else {
                    e.printStackTrace();
                }

            }
        });

        return listasCompras;
    }

    /*private void populaListaTeste() {
        ListaCompra listaTeste = new ListaCompra();
        listaTeste.setTitulo("Churrasco");
        listaTeste.setQuantidadeTotal(10);
        listaTeste.setQuantidadeComprada(1);

        ListaCompra listaTeste2 = new ListaCompra();
        listaTeste2.setTitulo("Familia");
        listaTeste2.setQuantidadeTotal(5);
        listaTeste2.setQuantidadeComprada(4);

        listasCompras.add(listaTeste);
        listasCompras.add(listaTeste2);
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_compras, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_lista:
                abreTelaAdicionarLista();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void abreTelaAdicionarLista() {
        MockSalvar();
    }

    private void MockSalvar() {
        ListaCompra lista = new ListaCompra();
        lista.setTitulo("Churrasco");
        lista.put("usuario", ParseUser.getCurrentUser().getObjectId());

        final ParseObject listaCompra = ParseObject.create("ListaCompra");
        listaCompra.put("titulo", lista.getTitulo());
        listaCompra.put("usuario", ParseUser.getCurrentUser().getObjectId());

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
                                            recyclerView.swapAdapter(adapter, false);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ListaCompra item);
    }
}
