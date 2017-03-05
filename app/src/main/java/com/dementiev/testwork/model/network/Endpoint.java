package com.dementiev.testwork.model.network;

import com.dementiev.testwork.BuildConfig;
import com.dementiev.testwork.model.entity.Item;
import com.dementiev.testwork.model.network.entity.ItemJsonObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by dron on 04.03.17.
 */

public class Endpoint {
    private Service service = null;

    private Endpoint() {
    }

    public static Endpoint create() {
        Endpoint instance = new Endpoint();
        instance.intService();
        return instance;
    }

    private void intService() {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            okhttpBuilder.addInterceptor(interceptor);
        }

        OkHttpClient client = okhttpBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://private-db05-jsontest111.apiary-mock.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(Service.class);
    }

    private Item convertItem(ItemJsonObject entity) {
        return new Item(entity.getId(), entity.getTitle(), entity.getUri());
    }

    public Observable<Item> getData() {
        // use stream parser for production
        return service.getItems()
                .flatMap(Observable::fromIterable)
                .map(this::convertItem);
    }

    private interface Service {
        @GET("androids")
        Observable<ArrayList<ItemJsonObject>> getItems();
    }
}
