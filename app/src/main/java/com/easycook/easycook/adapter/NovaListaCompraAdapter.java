package com.easycook.easycook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.easycook.easycook.R;
import com.easycook.easycook.model.Produto;

import java.util.ArrayList;

import butterknife.BindView;

/** Created by gabriel on 8/23/17. */
public class NovaListaCompraAdapter extends RecyclerView.Adapter {

    private ArrayList<Produto> produtos;
    private Context context;

    public NovaListaCompraAdapter(ArrayList<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.activity_lista_compra_itens, parent, false);

        return new NovaListaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        NovaListaViewHolder holder = (NovaListaViewHolder) viewHolder;

        Produto produto = produtos.get(position);

        holder.mImageView.setImageDrawable(getDrawableImagem(position));
        holder.mNomeView.setText(produto.getNome());
    }

    private TextDrawable getDrawableImagem(int position) {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        return TextDrawable.builder()
                .buildRound(produtos.get(position).getNome().substring(0,1), color);
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    private class NovaListaViewHolder extends RecyclerView.ViewHolder{

        final ImageView mImageView;
        final TextView mNomeView;

        public NovaListaViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.img_produto);
            mNomeView = (TextView) itemView.findViewById(R.id.tv_nome_produto);
        }
    }
}
