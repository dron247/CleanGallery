package com.dementiev.testwork.ui.base;

/**
 * Created by dron on 03.03.17.
 */

public interface BasePresenter {
    <E extends BaseView> void bind(E view);

    void unbind();

    boolean isAttached();
}
