package marvelapp.lyhoangvinh.com.marvelapp.base.view;


import marvelapp.lyhoangvinh.com.marvelapp.base.response.ErrorEntity;

public interface BaseView {
    void showProgress();

    void hideProgress();

    void onError(ErrorEntity error);

    void setProgress(boolean show);

    void showMessage(String message);
}
