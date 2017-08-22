package com.easycook.easycook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import com.easycook.easycook.R;
import com.easycook.easycook.adapter.TabsAdapter;
import com.easycook.easycook.fragment.CompraFragment;
import com.easycook.easycook.model.ListaCompra;
import com.easycook.easycook.util.SlidingTabLayout;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements CompraFragment.OnListFragmentInteractionListener {

    @BindView(R.id.view_pager_main) ViewPager mViewPager;
    @BindView(R.id.sliding_tab_main) SlidingTabLayout slidingTabLayout;
    private TabsAdapter tabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        tabsAdapter = new TabsAdapter(getSupportFragmentManager(), this);

        int limit = (tabsAdapter.getCount() > 1 ? tabsAdapter.getCount() - 1 : 1);

        mViewPager.setAdapter(tabsAdapter);
        mViewPager.setOffscreenPageLimit(limit);

        slidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tv_item_tab);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.accent));
        slidingTabLayout.setViewPager(mViewPager);
    }

    public void logOut(View view) {
        ParseUser.logOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(mViewPager.getCurrentItem() == 1) {
            menu.findItem(R.id.action_add_lista).setVisible(true);
            menu.findItem(R.id.action_remove_lista).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onListFragmentInteraction(ListaCompra item) {

    }
}
