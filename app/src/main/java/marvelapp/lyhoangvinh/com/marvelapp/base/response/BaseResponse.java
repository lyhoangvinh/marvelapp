package marvelapp.lyhoangvinh.com.marvelapp.base.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {

    @SerializedName("Success")
    @Expose
    private boolean success;

    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;

    @SerializedName("TotalItemCount")
    @Expose
    private int totalItemCount;

    @SerializedName("Data")
    @Expose
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }
}

