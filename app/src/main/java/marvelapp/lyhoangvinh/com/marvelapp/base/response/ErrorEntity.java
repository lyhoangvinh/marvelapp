package marvelapp.lyhoangvinh.com.marvelapp.base.response;


import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Entity for handling error
 */

public class ErrorEntity {

    public static final int HTTP_ERROR_CODE_UNAUTHORIZED = 401;

    public static final String OOPS = "Oops! please try again";
    public static final String NETWORK_UNAVAILABLE = "Network problem!";
    public static final String ERROR_UNAUTHORIZED = "Error! Please re-login!";

    private String message = "";
    private int httpCode = 0;

    public static ErrorEntity getError(@Nullable Throwable throwable) {
        ErrorEntity e = new ErrorEntity();
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            if (httpException.code() == HTTP_ERROR_CODE_UNAUTHORIZED) {
                e.setHttpCode(HTTP_ERROR_CODE_UNAUTHORIZED);
                e.setMessage(ERROR_UNAUTHORIZED);
            } else {
                // get the body fail
                Response baseResponse = httpException.response();
                e.setHttpCode(baseResponse.code());
                if (!baseResponse.isSuccessful() && baseResponse.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(baseResponse.errorBody().string());
                        e.setMessage(jObjError.getString("message"));
                    } catch (Exception eXX) {
                        eXX.printStackTrace();
                        e.setMessage("Error");
                    }
                }
            }
        } else if (throwable instanceof IOException){
            e.setMessage(NETWORK_UNAVAILABLE);
        } else {
            e.setMessage(OOPS);
        }
        return e;
    }

    public static ErrorEntity getError(String reason) {
        if (reason != null) {
            return new ErrorEntity(reason, 0);
        } else {
            return new ErrorEntity(OOPS, 0);
        }
    }

    public static ErrorEntity getErrorOops() {
        return new ErrorEntity(OOPS, 0);
    }

    public ErrorEntity() {
    }

    public ErrorEntity(String message, int httpCode) {
        this.message = message;
        this.httpCode = httpCode;
    }

    public static int getHttpErrorCodeUnauthorized() {
        return HTTP_ERROR_CODE_UNAUTHORIZED;
    }

    public static String getOOPS() {
        return OOPS;
    }

    public static String getNetworkUnavailable() {
        return NETWORK_UNAVAILABLE;
    }

    public static String getErrorUnauthorized() {
        return ERROR_UNAUTHORIZED;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }
}
