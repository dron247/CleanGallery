package com.dementiev.testwork.ui.itemsDetails;


import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dementiev.testwork.R;
import com.dementiev.testwork.model.entity.Item;
import com.dementiev.testwork.ui.base.BaseActivity;
import com.dementiev.testwork.ui.itemsDetails.base.ItemDetailsPresenter;
import com.dementiev.testwork.ui.itemsDetails.base.ItemDetailsView;

import java.util.AbstractList;

public class DetailsActivity extends BaseActivity implements ItemDetailsView {
    private static final String POSITION_KEY = "current_position";
    int currentPagePosition = 0;
    ItemDetailsPresenter detailsPresenter;
    ImageView btnNext;
    ImageView btnPrev;
    AbstractList<Item> itemsSource;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager viewPager;

    public static void create(Context context, int position) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(POSITION_KEY, position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DETAILS", "created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        currentPagePosition = getIntent().getIntExtra(POSITION_KEY, 0);

        // toolbar
        Toolbar toolbar = $(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Drawable backArrow = getDrawable2(R.drawable.ic_arrow_back_black_24dp);
        backArrow.setColorFilter(getColor2(R.color.vibrant_text), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationIcon(backArrow);
        toolbar.setNavigationOnClickListener(v -> finish());


        detailsPresenter = DetailsPresenter.create();
        detailsPresenter.bind(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        itemsSource = detailsPresenter.getItemsSource();
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(
                getFragmentManager(),
                itemsSource);

        btnNext = $(R.id.arrow_right);
        btnPrev = $(R.id.arrow_left);

        btnNext.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        });

        btnPrev.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        });


        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentPagePosition);
        viewPager.addOnPageChangeListener(new PageListener());
        checkScrollButtonsVisibility();
    }

    private void checkScrollButtonsVisibility() {
        if (viewPager == null || viewPager.getAdapter() == null) {
            return;
        }
        if (currentPagePosition == 0) {
            btnPrev.setVisibility(View.INVISIBLE);
        } else {
            btnPrev.setVisibility(View.VISIBLE);
        }

        int count = itemsSource.size();
        if (count == 0 || currentPagePosition >= count - 1) {
            btnNext.setVisibility(View.INVISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        if (detailsPresenter != null) {
            detailsPresenter.unbind();
        }

        super.onDestroy();
    }

    @Override // view
    public Context getContext() {
        return this;
    }

    @Override // view
    public void close() {
        finish();
    }

    private class PageListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            currentPagePosition = position;
            checkScrollButtonsVisibility();
        }
    }


}
