package marvelapp.lyhoangvinh.com.marvelapp.base.activity;


import android.os.Bundle;
import android.support.annotation.CallSuper;


import javax.inject.Inject;

import marvelapp.lyhoangvinh.com.marvelapp.base.presenter.BasePresenter;
import marvelapp.lyhoangvinh.com.marvelapp.base.view.BaseView;

/**
 * Created by vinh on 9/12/17.
 */

public abstract class BasePresenterActivity<V extends BaseView, P extends BasePresenter<V>> extends BaseActivity {

    @Inject
    protected P presenter;

    public P getPresenter() {
        return presenter;
    }

    @CallSuper
    protected void initialize() {
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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
