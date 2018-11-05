package marvelapp.lyhoangvinh.com.marvelapp.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import marvelapp.lyhoangvinh.com.marvelapp.base.api.ApiService;
import marvelapp.lyhoangvinh.com.marvelapp.base.api.ServiceManager;
import marvelapp.lyhoangvinh.com.marvelapp.data.RealmDatabase;
import marvelapp.lyhoangvinh.com.marvelapp.di.qualifier.ApplicationContext;

@Module
public class ServiceModule {
    private final Context mContext;

    public ServiceModule(@ApplicationContext Context context) {
        this.mContext = context;
        initRealm();
    }

    private void initRealm() {
        int schemaVersion = 1; // first version
        Realm.init(mContext);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(schemaVersion)
                .migration((realm, oldVersion, newVersion) -> {
                    // migrate realm here
                })
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    @Provides
    @Singleton
    ServiceManager provideServiceManager(@ApplicationContext Context context, ApiService apiService){
       return new ServiceManager(context, apiService);
    }

    @Provides
    @Singleton
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    RealmDatabase provideRealmDatabase(Realm realm) {
        return new RealmDatabase(realm);
    }
}
