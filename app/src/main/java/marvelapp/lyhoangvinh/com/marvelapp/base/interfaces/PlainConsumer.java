package marvelapp.lyhoangvinh.com.marvelapp.base.interfaces;

import android.support.annotation.NonNull;

public interface PlainConsumer<T> {
    void accept(@NonNull T t);
}
