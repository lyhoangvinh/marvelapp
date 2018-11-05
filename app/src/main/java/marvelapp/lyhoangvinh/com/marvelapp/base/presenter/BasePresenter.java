package marvelapp.lyhoangvinh.com.marvelapp.base.presenter;


import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;




import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import marvelapp.lyhoangvinh.com.marvelapp.base.api.ApiService;
import marvelapp.lyhoangvinh.com.marvelapp.base.api.ApiUtils;
import marvelapp.lyhoangvinh.com.marvelapp.base.api.ServiceManager;
import marvelapp.lyhoangvinh.com.marvelapp.base.interfaces.Lifecycle;
import marvelapp.lyhoangvinh.com.marvelapp.base.interfaces.PlainConsumer;
import marvelapp.lyhoangvinh.com.marvelapp.base.interfaces.Refreshable;
import marvelapp.lyhoangvinh.com.marvelapp.base.response.BaseResponse;
import marvelapp.lyhoangvinh.com.marvelapp.base.response.ErrorEntity;
import marvelapp.lyhoangvinh.com.marvelapp.base.view.BaseView;
import marvelapp.lyhoangvinh.com.marvelapp.data.Resource;
import marvelapp.lyhoangvinh.com.marvelapp.data.Status;
import marvelapp.lyhoangvinh.com.marvelapp.di.qualifier.ActivityContext;
import retrofit2.Response;

public abstract class BasePresenter<V extends BaseView> implements Lifecycle, Refreshable {

    @Nullable
    private V view;

    protected Context context;

    protected ServiceManager manager;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    public BasePresenter(@ActivityContext Context context, ServiceManager manager) {
        this.context = context;
        this.manager = manager;
        mCompositeDisposable = new CompositeDisposable();
    }

    public ServiceManager getManager() {
        return manager;
    }

    @Nullable
    public V getView() {
        return view;
    }

    public Context getContext() {
        return context;
    }

    public ApiService getApi(){
        return manager.getApiService();
    }

    public void bindView(V view) {
        this.view = view;
    }

    public void unbindView() {
        this.view = null;
    }

    /**
     * @return {@link LifecycleOwner} associate with this presenter (host activities, fragments)
     */
    protected LifecycleOwner getLifeCircleOwner() {
        return (LifecycleOwner) view;
    }

    /**
     * NULL SAFE
     * Add new api request to {@link CompositeDisposable} and execute immediately
     * All error case and progress showing will be handled automatically
     * @param request           observable request
     * @param showProgress      true if should show loading progress
     *
     * @param responseConsumer   callback for success response.
     * @param errorConsumer     callback for error case.
     *                          If both of these listeners are null, the request will be subscribed
     *                          on io thread without observing on main thread
     *                          * no update UI in case of both success and error are null
     * @param forceResponseWithoutCheckNullView the success result will be returned without check null for view
     * @param <T> Type of response body
     */
    protected <T> void addRequest(
            Single<BaseResponse<T>> request, boolean showProgress,
            boolean forceResponseWithoutCheckNullView,
            @Nullable PlainConsumer<T> responseConsumer,
            @Nullable PlainConsumer<ErrorEntity> errorConsumer) {

        boolean shouldUpdateUI = showProgress || responseConsumer != null || errorConsumer != null;

        if (showProgress && view != null) {
            view.showProgress();
        }

        Disposable disposable = ApiUtils.makeRequest(request, shouldUpdateUI, response -> {
            if (responseConsumer != null && (forceResponseWithoutCheckNullView || view != null)) {
                responseConsumer.accept(response);
            }
        }, error -> {
            if (errorConsumer != null) {
                errorConsumer.accept(error);
            } else if (view != null) {
                view.onError(error);
            }
        }, () -> {
            // complete
            if (showProgress && view != null) {
                view.hideProgress();
            }
        });

        if (mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * Add a request without handling error and no update UI
     */
    protected <T> void addRequest(Single<BaseResponse<T>> request) {
        addRequest(request, false, false, null, null);
    }

    /**
     * Add a request with success listener
     */
    protected <T> void addRequest(Single<BaseResponse<T>> request, boolean showProgress,
                                  @Nullable PlainConsumer<T> responseConsumer) {
        addRequest(request, showProgress, false, responseConsumer, null);
    }

    /**
     * Add a request with success listener and error listener
     */
    protected <T> void addRequest(Single<BaseResponse<T>> request, boolean showProgress,
                                  @Nullable PlainConsumer<T> responseConsumer,
                                  @Nullable PlainConsumer<ErrorEntity> errorListener) {
        addRequest(request, showProgress, false, responseConsumer, errorListener);
    }

    /**
     * Add a request with success listener and forceResponseWithoutCheckNullView param
     */
    protected <T> void addRequest(Single<BaseResponse<T>> request, boolean showProgress,
                                  boolean forceResponseWithoutCheckNullView,
                                  @Nullable PlainConsumer<T> responseConsumer) {
        addRequest(request, showProgress, forceResponseWithoutCheckNullView, responseConsumer, null);
    }


    /**
     * add a request with {@link marvelapp.lyhoangvinh.com.marvelapp.data.Resource} flowable created by
     * {@link marvelapp.lyhoangvinh.com.marvelapp.base.data.BaseRepo( Single , PlainConsumer)}
     * @param showProgress
     * @param resourceFlowable
     * @param response
     * @param <T>
     */
    public <T> void addRequest(boolean showProgress, Flowable<Resource<T>> resourceFlowable, @Nullable PlainConsumer<T> response) {
        Disposable disposable = resourceFlowable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(resource -> {
                    if (resource != null && getView() != null) {
                        Log.d("source", "addRequest: resource changed: " + resource.toString());
                        if (resource.data != null && response != null) {
                            response.accept(resource.data);
                        }
                        if (showProgress) {
                            getView().setProgress(resource.status == Status.LOADING);
                        }
                        if (resource.message != null) {
                            getView().showMessage(resource.message);
                        }
                    }
                });
        if (mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public <T> void addRequest(Flowable<Resource<T>> resourceFlowable, PlainConsumer<T> response) {
        addRequest(true, resourceFlowable, response);
    }

    public <T> void addRequest(Flowable<Resource<T>> resourceFlowable) {
        addRequest(true, resourceFlowable, null);
    }

    public <T> void addRequest(boolean showProgress, Flowable<Resource<T>> resourceFlowable) {
        addRequest(showProgress, resourceFlowable, null);
    }


    protected <T> void addRequestDefault(
            Single<Response<T>> request, boolean showProgress,
            boolean forceResponseWithoutCheckNullView,
            @Nullable PlainConsumer<T> responseConsumer,
            @Nullable PlainConsumer<ErrorEntity> errorConsumer) {

        boolean shouldUpdateUI = showProgress || responseConsumer != null || errorConsumer != null;

        if (showProgress && getView() != null) {
            getView().showProgress();
        }

        Disposable disposable = ApiUtils.makeRequestDefault(request, shouldUpdateUI, response -> {
            if (responseConsumer != null && (forceResponseWithoutCheckNullView || view != null)) {
                responseConsumer.accept(response);
            }
        }, error -> {
            if (errorConsumer != null) {
                errorConsumer.accept(error);
            } else if (view != null) {
                getView().onError(error);
            }
        }, () -> {
            // complete
            if (showProgress && getView() != null) {
                getView().hideProgress();
            }
        });

        if (mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    protected <T> void addRequestDefault(boolean showProgress, Single<Response<T>> request,
                                         @Nullable PlainConsumer<T> responseConsumer) {
        addRequestDefault(request, showProgress, false, responseConsumer, null);
    }

    protected <T> void addRequestDefault(boolean showProgress, Single<Response<T>> request) {
        addRequestDefault(request, showProgress, false, null, null);
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void refresh(){

    }

    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null){
            mCompositeDisposable.dispose();
        }
        unbindView();
    }
}
