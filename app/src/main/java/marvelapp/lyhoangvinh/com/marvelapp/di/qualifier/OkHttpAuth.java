package marvelapp.lyhoangvinh.com.marvelapp.di.qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Authorization header {@link okhttp3.OkHttpClient}
 */

@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface OkHttpAuth {
}
