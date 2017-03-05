package com.dementiev.testwork.ui.itemsCollection;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dementiev.testwork.R;
import com.dementiev.testwork.model.interactor.LoadItemsInteractor;
import com.dementiev.testwork.ui.base.BaseActivity;
import com.dementiev.testwork.ui.itemsCollection.base.ItemsCollectionPresenter;
import com.dementiev.testwork.ui.itemsCollection.base.ItemsCollectionView;

import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements ItemsCollectionView {
    ItemsCollectionPresenter itemsCollectionPresenter = null;
    RecyclerView itemsList;
    ItemsListAdapter itemsListAdapter;
    ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = $(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemsList = $(R.id.items_list);
        spinner = $(R.id.loading);
        spinner.setVisibility(View.GONE);

        FloatingActionButton fab = $(R.id.fab);
        fab.setOnClickListener(view -> {
            itemsCollectionPresenter.onRefreshCommand();
        });
        // Presenter init
        itemsCollectionPresenter = MainPresenter.create(
                LoadItemsInteractor.create(this, Schedulers.io(), Schedulers.computation())
        );
        itemsCollectionPresenter.<ItemsCollectionView>bind(this);

        // All inits where presenter is needed

        configureAdapter();
        configureList();
    }

    private void configureAdapter() {
        itemsListAdapter = ItemsListAdapter.create(
                itemsCollectionPresenter.getItemsSource(),
                itemsCollectionPresenter::onItemSelected
        );
    }

    private void configureList() {
        itemsList.setLayoutManager(new LinearLayoutManager(this));
        itemsList.setAdapter(itemsListAdapter);
    }

    @Override
    protected void onDestroy() {
        if (itemsCollectionPresenter != null) {
            itemsCollectionPresenter.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void showUpdate() {
        spinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideUpdate() {
        spinner.setVisibility(View.GONE);
    }


    @Override
    public void onItemsCollectionUpdated(boolean isError) {
        if (!isError) {
            itemsList.setAdapter(null);
            itemsList.setLayoutManager(null);
            configureAdapter();
            configureList();
            itemsListAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, R.string.list_load_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void close() {
        this.finish();
    }
}
