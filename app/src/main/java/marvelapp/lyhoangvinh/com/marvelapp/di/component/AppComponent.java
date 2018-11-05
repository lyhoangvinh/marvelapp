package marvelapp.lyhoangvinh.com.marvelapp.di.component;


import android.content.Context;



import javax.inject.Singleton;

import dagger.Component;
import marvelapp.lyhoangvinh.com.marvelapp.MyApplication;
import marvelapp.lyhoangvinh.com.marvelapp.base.api.ApiService;
import marvelapp.lyhoangvinh.com.marvelapp.base.api.ServiceManager;
import marvelapp.lyhoangvinh.com.marvelapp.data.RealmDatabase;
import marvelapp.lyhoangvinh.com.marvelapp.di.module.AppModule;
import marvelapp.lyhoangvinh.com.marvelapp.di.module.NetworkModule;
import marvelapp.lyhoangvinh.com.marvelapp.di.module.ServiceModule;
import marvelapp.lyhoangvinh.com.marvelapp.di.qualifier.ApplicationContext;

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        ServiceModule.class
})
public interface AppComponent {
    @ApplicationContext
    Context context();

    ApiService getApiService();

    RealmDatabase realmDatabase();

    ServiceManager getServiceManager();

    void inject(MyApplication application);
}
