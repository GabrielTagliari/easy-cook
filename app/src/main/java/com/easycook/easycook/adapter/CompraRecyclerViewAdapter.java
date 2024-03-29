package com.easycook.easycook.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.easycook.easycook.R;
import com.easycook.easycook.fragment.CompraFragment.OnListFragmentInteractionListener;
import com.easycook.easycook.model.ListaCompra;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ListaCompra} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CompraRecyclerViewAdapter extends RecyclerView.Adapter<CompraRecyclerViewAdapter.ViewHolder> {

    private final List<ListaCompra> mLista;
    private final OnListFragmentInteractionListener mListener;

    public CompraRecyclerViewAdapter(List<ListaCompra> items, OnListFragmentInteractionListener listener) {
        mLista = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_compra, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mLista.get(position);
        holder.mImageView.setImageDrawable(getDrawableImagem(position));
        holder.mTituloView.setText(mLista.get(position).getTitulo());
        holder.mDescricaoView.setText(
                mLista.get(position).getQuantidadeComprada()
                + " de "
                + mLista.get(position).getQuantidadeTotal()
                + " produtos comprados");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    private TextDrawable getDrawableImagem(int position) {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        return TextDrawable.builder()
                .buildRound(mLista.get(position).getTitulo().substring(0,1), color);
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mImageView;
        final TextView mTituloView;
        final TextView mDescricaoView;
        ListaCompra mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.img_lista_compra);
            mTituloView = (TextView) view.findViewById(R.id.titulo);
            mDescricaoView = (TextView) view.findViewById(R.id.descricao);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTituloView.getText() + "'";
        }
    }
}
