package marvelapp.lyhoangvinh.com.marvelapp.di.module;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import dagger.Module;
import dagger.Provides;
import marvelapp.lyhoangvinh.com.marvelapp.base.activity.BaseActivity;
import marvelapp.lyhoangvinh.com.marvelapp.di.qualifier.ActivityContext;
import marvelapp.lyhoangvinh.com.marvelapp.di.qualifier.ActivityFragmentManager;

@Module
public class ActivityModule {

    private final BaseActivity mActivity;

    public ActivityModule(BaseActivity activity) {
        mActivity = activity;
    }

    @Provides
    protected FragmentActivity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    protected Context provideContext() {
        return mActivity;
    }

    @Provides
    @ActivityFragmentManager
    protected FragmentManager provideFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }


    @Provides
    protected LifecycleOwner provideLifeCycleOwner() {
        return mActivity;
    }
}