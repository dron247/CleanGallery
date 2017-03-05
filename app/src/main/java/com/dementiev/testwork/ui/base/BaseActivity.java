package com.dementiev.testwork.ui.base;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by dron on 03.03.17.
 */

public class BaseActivity extends AppCompatActivity {
    @SuppressWarnings("uncecked")
    public <T extends View> T $(int id) {
        return (T) findViewById(id);
    }

    protected Drawable getDrawable2(@DrawableRes int imageResId) {
        Drawable img;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            img = getResources().getDrawable(imageResId, null);
        } else {
            img = getResources().getDrawable(imageResId);
        }
        return img;
    }

    protected int getColor2(@ColorRes int colorId) {
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getResources().getColor(colorId, null);
        } else {
            color = getResources().getColor(colorId);
        }
        return color;
    }
}
