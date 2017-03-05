package com.dementiev.testwork.model.interactor.base;

import io.reactivex.Completable;

/**
 * Created by dron on 05.03.17.
 */

public interface Interactor {
    Completable perform();
}
