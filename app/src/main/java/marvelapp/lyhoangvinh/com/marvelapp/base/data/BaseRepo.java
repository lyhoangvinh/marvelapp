package marvelapp.lyhoangvinh.com.marvelapp.base.data;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.Nullable;



import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import marvelapp.lyhoangvinh.com.marvelapp.base.api.ApiService;
import marvelapp.lyhoangvinh.com.marvelapp.base.interfaces.PlainConsumer;
import marvelapp.lyhoangvinh.com.marvelapp.base.response.BaseResponse;
import marvelapp.lyhoangvinh.com.marvelapp.data.RealmDatabase;
import marvelapp.lyhoangvinh.com.marvelapp.data.Resource;
import marvelapp.lyhoangvinh.com.marvelapp.data.SimpleNetworkBoundSource;

/**
 * Created by vinh on 9/15/17.
 *
 */


public abstract class BaseRepo {

    private final ApiService apiService;

    private final LifecycleOwner owner;

    private final RealmDatabase realmDatabase;

    public BaseRepo(LifecycleOwner owner, ApiService apiService, RealmDatabase realmDatabase) {
        this.apiService = apiService;
        this.owner = owner;
        this.realmDatabase = realmDatabase;
    }

    public ApiService getApiService() {
        return apiService;
    }

    /**
     * For single data
     * @param remote
     * @param onSave
     * @param <T>
     * @return
     */
    protected <T> Flowable<Resource<T>> createResource(@Nullable Single<BaseResponse<T>> remote,
                                                       @Nullable PlainConsumer<T> onSave) {
        return Flowable.create(emitter -> {
            new SimpleNetworkBoundSource<T>(emitter, true) {

                @Override
                public Single<BaseResponse<T>> getRemote() {
                    return remote;
                }

                @Override
                public void saveCallResult(T data, boolean isRefresh) {
                    if (onSave != null) {
                        onSave.accept(data);
                    }
                }
            };
        }, BackpressureStrategy.BUFFER);
    }

    /**
     * For a list of data
     * @param isRefresh
     * @param remote
     * @param onSave
     * @param <T>
     * @return
     */
    protected <T> Flowable<Resource<T>> createResource(boolean isRefresh, @Nullable Single<BaseResponse<T>> remote,
                                                       @Nullable OnSaveResultListener<T> onSave) {
        return Flowable.create(emitter -> {
            new SimpleNetworkBoundSource<T>(emitter, isRefresh) {

                @Override
                public Single<BaseResponse<T>> getRemote() {
                    return remote;
                }

                @Override
                public void saveCallResult(T data, boolean isRefresh) {
                    if (onSave != null) {
                        onSave.onSave(data, isRefresh);
                    }
                }
            };
        }, BackpressureStrategy.BUFFER);
    }

    protected interface OnSaveResultListener<T> {
        void onSave(T data, boolean isRefresh);
    }
}