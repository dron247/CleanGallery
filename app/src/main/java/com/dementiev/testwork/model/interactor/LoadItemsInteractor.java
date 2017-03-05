package com.dementiev.testwork.model.interactor;

import android.content.Context;

import com.dementiev.testwork.model.interactor.base.Interactor;
import com.dementiev.testwork.model.network.Endpoint;
import com.dementiev.testwork.model.storage.ItemsCacheDb;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;

/**
 * Created by dron on 04.03.17.
 */

public class LoadItemsInteractor implements Interactor {
    ItemsCacheDb db;
    private Scheduler performScheduler;
    private Scheduler observeScheduler;
    private Context context;

    private LoadItemsInteractor(Context context, Scheduler performScheduler, Scheduler observeScheduler) {
        this.performScheduler = performScheduler;
        this.observeScheduler = observeScheduler;
        this.context = context;
        db = new ItemsCacheDb(context);
    }

    public static LoadItemsInteractor create(Context context, Scheduler performScheduler, Scheduler observeScheduler) {
        return new LoadItemsInteractor(context, performScheduler, observeScheduler);
    }

    @Override
    public Completable perform() {
        // заменить Completable на что-то, на что можно подписаться и ловить onNext в конце рефреша
        // без потери связи, для обработки пересозданий
        return new Completable() {
            @Override
            protected void subscribeActual(CompletableObserver observer) {
                db.clearCache();
                Endpoint endpoint = Endpoint.create();
                endpoint.getData().subscribeOn(performScheduler)
                        //.doOnNext(item -> Log.d("NETWORK", item.getTitle()))
                        .buffer(100)
                        .observeOn(observeScheduler)
                        .subscribe(
                                items -> {
                                    db.writeItems(items);
                                    // onNext
                                    //Log.d("NETWORK", String.valueOf(items.size()));
                                },
                                observer::onError,
                                observer::onComplete
                        );
            }
        };
    }

}
