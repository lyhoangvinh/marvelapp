package marvelapp.lyhoangvinh.com.marvelapp.base.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;


import javax.inject.Inject;

import marvelapp.lyhoangvinh.com.marvelapp.base.presenter.BasePresenter;
import marvelapp.lyhoangvinh.com.marvelapp.base.view.BaseView;

/**
 * Base fragment with presenter (out of user scope)
 * @param <V> Type of View, must extend {@link BaseView}
 * @param <P> Type of Presenter, must extend {@link BasePresenter}
 */
public abstract class BasePresenterFragment<V extends BaseView, P extends BasePresenter<V>>
        extends BaseFragment {

    @Inject
    protected P presenter;

    public P getPresenter() {
        return presenter;
    }

    @Override
    @CallSuper
    protected void initialize(View view, Context ctx) {
        if (presenter != null) {
            presenter.bindView(getViewLayer());
            presenter.onCreate();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (presenter != null) {
            presenter.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (presenter != null){
            presenter.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    private V getViewLayer() {
        // noinspection unchecked
        return ((V)this);
    }
}