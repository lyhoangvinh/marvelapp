package marvelapp.lyhoangvinh.com.marvelapp.di.module;


import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import marvelapp.lyhoangvinh.com.marvelapp.base.api.ApiService;
import marvelapp.lyhoangvinh.com.marvelapp.base.api.ServiceFactory;
import marvelapp.lyhoangvinh.com.marvelapp.di.qualifier.ApplicationContext;
import marvelapp.lyhoangvinh.com.marvelapp.di.qualifier.OkHttpNoAuth;
import okhttp3.OkHttpClient;

@Module
public class NetworkModule {

    protected Context context;

    public NetworkModule(@ApplicationContext Context context) {
        this.context = context;
    }

    @Provides
    @OkHttpNoAuth
    @Singleton
    static OkHttpClient provideOkHttpClientNoAuth(@ApplicationContext Context context) {
        return ServiceFactory.makeOkHttpClientBuilder(context).build();
    }


    @Provides
    @Singleton
    static ApiService provideApiService(Gson gson, @OkHttpNoAuth OkHttpClient okHttpClient) {
        return ServiceFactory.makeService(ApiService.class, gson, okHttpClient);
    }
}
