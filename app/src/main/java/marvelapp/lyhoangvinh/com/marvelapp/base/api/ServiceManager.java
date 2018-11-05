package marvelapp.lyhoangvinh.com.marvelapp.base.api;

import android.content.Context;
import android.support.annotation.NonNull;



import javax.inject.Inject;
import javax.inject.Singleton;

import marvelapp.lyhoangvinh.com.marvelapp.di.qualifier.ApplicationContext;

@Singleton
public class ServiceManager {

    protected Context mContext;
    protected ApiService apiService;

    @Inject
    public ServiceManager(@ApplicationContext Context mContext, ApiService apiService) {
        this.mContext = mContext;
        this.apiService = apiService;
    }

    @NonNull
    public ApiService getApiService() {
        return apiService;
    }
}
