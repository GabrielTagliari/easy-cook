package com.easycook.easycook.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.ViewGroup;

import com.easycook.easycook.R;
import com.easycook.easycook.fragment.CompraFragment;
import com.easycook.easycook.fragment.PerfilFragment;

import java.util.HashMap;

/** Created by gabriel on 8/8/17. */

public class TabsAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private int[] icones = new int[]{R.drawable.ic_person_outline, R.drawable.ic_assignment};
    private int tamanhoIcone;
    private HashMap<Integer, Fragment> fragmentos = new HashMap<>();

    public TabsAdapter(FragmentManager fm, Context c) {
        super(fm);
        this.context = c;
        double escala = this.context.getResources().getDisplayMetrics().density;
        this.tamanhoIcone = (int) (24 * escala);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new PerfilFragment();
                fragmentos.put(position, fragment);
                break;
            case 1:
                fragment = new CompraFragment();
                break;
        }

        return fragment;
    }

    public Fragment getFragment(int indice) {
        return fragmentos.get(indice);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        fragmentos.remove(position);
    }

    @Override
    public int getCount() {
        return icones.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable drawable = ContextCompat.getDrawable(this.context, icones[position]);
        drawable.setBounds(0, 0, tamanhoIcone, tamanhoIcone);

        ImageSpan imageSpan = new ImageSpan(drawable);

        SpannableString spannableString = new SpannableString(" ");
        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
}
