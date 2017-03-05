package com.dementiev.testwork.ui.itemsDetails;

import android.app.Fragment;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dementiev.testwork.R;
import com.dementiev.testwork.model.entity.Item;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by dron on 05.03.17.
 */

public class DetailsPage extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_ITEM_ID = "item_id";
    private static final String ARG_ITEM_TITLE = "item_title";
    private static final String ARG_ITEM_IMG = "item_img";

    private static final String ARG_ITEM_POS = "item_pos";
    private static final String ARG_ITEM_IS_LAST = "item_iis_last";
    int itemId;
    String itemTitle;
    String itemImageUri;
    int adapterPosition = 0;
    boolean isLast = false;
    View titleContainer;
    TextView textView;
    TextView errorLabel;
    ImageView image;
    ProgressBar spinner;
    public DetailsPage() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DetailsPage newInstance(Item data, int index, boolean isLast) {
        DetailsPage fragment = new DetailsPage();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_ID, data.getId());
        args.putString(ARG_ITEM_TITLE, data.getTitle());
        args.putString(ARG_ITEM_IMG, data.getImageUri());

        args.putInt(ARG_ITEM_POS, index);
        args.putBoolean(ARG_ITEM_IS_LAST, isLast);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(false);
        itemId = getArguments().getInt(ARG_ITEM_ID);
        itemTitle = getArguments().getString(ARG_ITEM_TITLE);
        itemImageUri = getArguments().getString(ARG_ITEM_IMG);

        adapterPosition = getArguments().getInt(ARG_ITEM_POS);
        isLast = getArguments().getBoolean(ARG_ITEM_IS_LAST);

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(itemTitle);

        errorLabel = (TextView) rootView.findViewById(R.id.error_label);
        errorLabel.setVisibility(View.INVISIBLE);

        image = (ImageView) rootView.findViewById(R.id.picture_host);

        titleContainer = rootView.findViewById(R.id.title_container);

        spinner = (ProgressBar) rootView.findViewById(R.id.loading);


        Picasso.with(getActivity()).load(itemImageUri).fit().centerCrop().into(image, new Callback() {
            @Override
            public void onSuccess() {
                if (isAdded()) {
                    spinner.setVisibility(View.GONE);
                    errorLabel.setVisibility(View.INVISIBLE);
                    Palette.from(((BitmapDrawable) image.getDrawable()).getBitmap()).generate(palette -> {
                        Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                        if (mutedSwatch != null) {
                            titleContainer.setBackgroundColor(mutedSwatch.getRgb());
                            textView.setTextColor(mutedSwatch.getTitleTextColor());
                        }
                    });
                }
            }

            @Override
            public void onError() {
                if (isAdded()) {
                    spinner.setVisibility(View.GONE);
                    String fmt = getResources().getString(R.string.image_load_error);
                    String formatted = String.format(fmt, itemImageUri);
                    errorLabel.setText(formatted);
                    errorLabel.setVisibility(View.VISIBLE);
                }
            }
        });

        return rootView;
    }
}
