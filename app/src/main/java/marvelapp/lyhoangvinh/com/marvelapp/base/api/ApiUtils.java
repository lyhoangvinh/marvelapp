package marvelapp.lyhoangvinh.com.marvelapp.base.api;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import marvelapp.lyhoangvinh.com.marvelapp.base.interfaces.PlainConsumer;
import marvelapp.lyhoangvinh.com.marvelapp.base.response.BaseResponse;
import marvelapp.lyhoangvinh.com.marvelapp.base.response.ErrorEntity;
import retrofit2.Response;

/**
 * Created by vinh on 9/10/17.
 */

public final class ApiUtils {

    /**
     * Create new retrofit api request
     *
     * @param request          observable request
     * @param shouldUpdateUi   true if should update UI after response returned
     * @param responseConsumer consume response data
     * @param errorConsumer    consume errors
     * @param <T>              Type of response body
     */
    public static <T> Disposable makeRequest(
            Single<BaseResponse<T>> request, boolean shouldUpdateUi,
            @NonNull PlainConsumer<T> responseConsumer,
            @Nullable PlainConsumer<ErrorEntity> errorConsumer,
            @Nullable Action onComplete) {

        Single<BaseResponse<T>> single = request.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io());
        if (shouldUpdateUi) {
            single = single.observeOn(AndroidSchedulers.mainThread());
        }

        return single.subscribe(response -> {
            if (response.isSuccess()) {
                responseConsumer.accept(response.getData());
            } else if (errorConsumer != null) {
                errorConsumer.accept(ErrorEntity.getError(response.getErrorMessage()));
            }
            if (onComplete != null) {
                onComplete.run();
            }
        }, throwable -> {
            if (throwable instanceof RuntimeException) {
                // must be fixed while developing
//                throw new Exception(throwable);
            }
            // handle error
            if (errorConsumer != null) {
                errorConsumer.accept(ErrorEntity.getError(throwable));
            }
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public static <T> Disposable makeRequestDefault(
            Single<Response<T>> request, boolean shouldUpdateUi,
            @NonNull PlainConsumer<T> responseConsumer,
            @Nullable PlainConsumer<ErrorEntity> errorConsumer,
            @Nullable Action onComplete) {

        Single<Response<T>> single = request.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io());
        if (shouldUpdateUi) {
            single = single.observeOn(AndroidSchedulers.mainThread());
        }

        return single.subscribe(response -> {
            if (response.isSuccessful()) {
                responseConsumer.accept(Objects.requireNonNull(response.body()));
            } else {
                if (errorConsumer != null) {
                    errorConsumer.accept(ErrorEntity.getError(response.errorBody().string()));
                }
            }
            if (onComplete != null) {
                onComplete.run();
            }
        }, throwable -> {
            // handle error
            if (errorConsumer != null) {
                errorConsumer.accept(ErrorEntity.getError(throwable));
            }
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }


    public static <T> Disposable makeRequest(Single<BaseResponse<T>> request, boolean shouldUpdateUi, @NonNull PlainConsumer<T> responseConsumer) {
        return makeRequest(request, shouldUpdateUi, responseConsumer, null, null);
    }

    public static <T> Disposable makeRequest(Single<BaseResponse<T>> request, boolean shouldUpdateUi,
                                             @NonNull PlainConsumer<T> responseConsumer,
                                             @Nullable PlainConsumer<ErrorEntity> errorConsumer) {
        return makeRequest(request, shouldUpdateUi, responseConsumer, errorConsumer, null);
    }
}
