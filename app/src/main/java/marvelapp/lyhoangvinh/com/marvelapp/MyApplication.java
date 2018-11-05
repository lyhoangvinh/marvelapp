package marvelapp.lyhoangvinh.com.marvelapp;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import marvelapp.lyhoangvinh.com.marvelapp.base.api.ServiceManager;
import marvelapp.lyhoangvinh.com.marvelapp.di.component.AppComponent;
import marvelapp.lyhoangvinh.com.marvelapp.di.component.DaggerAppComponent;
import marvelapp.lyhoangvinh.com.marvelapp.di.module.AppModule;
import marvelapp.lyhoangvinh.com.marvelapp.di.module.NetworkModule;
import marvelapp.lyhoangvinh.com.marvelapp.di.module.ServiceModule;

public class MyApplication extends Application {

    protected AppComponent appComponent;

    @Inject
    ServiceManager serviceManager;

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    // component
    protected void setupAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .serviceModule(new ServiceModule(this))
                .networkModule(new NetworkModule(this))
                .build();
        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        if (appComponent == null) {
            setupAppComponent();
        }
        return appComponent;
    }


    public static MyApplication get(Activity activity) {
        return (MyApplication)activity.getApplication();
    }

    public static MyApplication get(Fragment fragment) {
        return get(fragment.getActivity());
    }

    public static MyApplication get(Context context) {
        return (MyApplication)context.getApplicationContext();
    }

}

