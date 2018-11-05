package marvelapp.lyhoangvinh.com.marvelapp.di.module;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;



import dagger.Module;
import dagger.Provides;
import marvelapp.lyhoangvinh.com.marvelapp.base.fragment.BaseFragment;
import marvelapp.lyhoangvinh.com.marvelapp.di.qualifier.ActivityContext;
import marvelapp.lyhoangvinh.com.marvelapp.di.qualifier.ActivityFragmentManager;
import marvelapp.lyhoangvinh.com.marvelapp.di.qualifier.ChildFragmentManager;
import marvelapp.lyhoangvinh.com.marvelapp.di.scopes.PerFragment;


@Module
public class FragmentModule {

    private final BaseFragment mFragment;

    public FragmentModule(BaseFragment fragment) {
        mFragment = fragment;
    }

    @PerFragment
    @Provides
    Fragment provideFragment() {
        return mFragment;
    }

    @Provides
    @PerFragment
    @ChildFragmentManager
    FragmentManager provideChildFragmentManager() { return mFragment.getChildFragmentManager(); }


    @Provides
    FragmentActivity provideActivity() {
        return mFragment.getActivity();
    }

    @Provides
    @ActivityFragmentManager
    FragmentManager provideFragmentManager() {
        return mFragment.getActivity().getSupportFragmentManager();
    }


    @Provides
    @ActivityContext
    Context provideContext() {
        return mFragment.getContext();
    }
}
