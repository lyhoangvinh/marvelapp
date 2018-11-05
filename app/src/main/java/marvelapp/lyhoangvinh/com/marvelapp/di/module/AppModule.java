package marvelapp.lyhoangvinh.com.marvelapp.di.module;

import android.app.Application;
import android.content.Context;


import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import marvelapp.lyhoangvinh.com.marvelapp.base.api.ServiceFactory;
import marvelapp.lyhoangvinh.com.marvelapp.di.qualifier.ApplicationContext;

@Module
public class AppModule {

    protected Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    static Gson provideGson() {
        return ServiceFactory.makeGsonForRealm();
    }
}