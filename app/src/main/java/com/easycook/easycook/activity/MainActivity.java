package com.easycook.easycook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.easycook.easycook.R;
import com.easycook.easycook.adapter.TabsAdapter;
import com.easycook.easycook.fragment.CompraFragment;
import com.easycook.easycook.fragment.dummy.DummyContent;
import com.easycook.easycook.util.SlidingTabLayout;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements CompraFragment.OnListFragmentInteractionListener {

    //@BindView(R.id.toolbar_logo) Toolbar toolbarPrincipal;
    @BindView(R.id.view_pager_main) ViewPager viewPager;
    @BindView(R.id.sliding_tab_main) SlidingTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //setSupportActionBar(toolbarPrincipal);

        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(tabsAdapter);
        slidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tv_item_tab);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.accent));
        slidingTabLayout.setViewPager(viewPager);
    }

    public void logOut(View view) {
        ParseUser.logOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
