package marvelapp.lyhoangvinh.com.marvelapp.base.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;


import butterknife.ButterKnife;
import marvelapp.lyhoangvinh.com.marvelapp.R;
import marvelapp.lyhoangvinh.com.marvelapp.base.interfaces.UiRefreshable;
import marvelapp.lyhoangvinh.com.marvelapp.base.response.ErrorEntity;
import marvelapp.lyhoangvinh.com.marvelapp.base.view.BaseView;
import marvelapp.lyhoangvinh.com.marvelapp.di.InjectionHelper;
import marvelapp.lyhoangvinh.com.marvelapp.di.component.ActivityComponent;
import marvelapp.lyhoangvinh.com.marvelapp.di.component.DaggerActivityComponent;
import marvelapp.lyhoangvinh.com.marvelapp.di.module.ActivityModule;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    private ActivityComponent mActivityComponent;

    private Dialog progress_dialog = null;

    public abstract int getLayout();

    public abstract void initialize(Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        initialize(savedInstanceState);
    }

    public void addFragment(@IdRes int res, Fragment fragment, @Nullable String tag) {
        getSupportFragmentManager().beginTransaction()
                .add(res, fragment, tag)
                .commit();
    }

    protected void finishWithAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    // activity component, activity may or may not need this
    public ActivityComponent activityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .appComponent(InjectionHelper.getAppComponent(this))
                    .build();
        }
        return mActivityComponent;
    }

    public void showProgressDialog(boolean cancelable) {
        if (progress_dialog == null) {
            progress_dialog = new Dialog(this);
            progress_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progress_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progress_dialog.setContentView(R.layout.progress_dialog);
        }

        if (!progress_dialog.isShowing()) {
            progress_dialog.setCanceledOnTouchOutside(cancelable);
            progress_dialog.setCancelable(cancelable);
            progress_dialog.show();
        }

        if (!isNetworkAvailable(this)) {
            progress_dialog.dismiss();
        }
    }

    public void hideProgressDialog() {
        if (progress_dialog != null && progress_dialog.isShowing()) {
            progress_dialog.dismiss();
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


    @Override
    public void showProgress() {
        showProgressDialog(false);
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
        if (this instanceof UiRefreshable) {
            ((UiRefreshable)this).doneRefresh();
        }
    }

    @Override
    public void onError(ErrorEntity error) {
        showToastLongMessage(error.getMessage());
    }

    @Override
    public void setProgress(boolean show) {
        if (show) {
            showProgress();
        } else {
            hideProgress();
        }
    }


    public void showToastLongMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void showToastShortMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        showToastLongMessage(message);
    }
}
