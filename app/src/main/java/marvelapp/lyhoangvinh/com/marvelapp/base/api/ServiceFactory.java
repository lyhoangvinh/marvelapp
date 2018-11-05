package marvelapp.lyhoangvinh.com.marvelapp.base.api;


import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.File;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.schedulers.Schedulers;
import io.realm.RealmObject;
import lyhoangvinh.com.myutil.network.Tls12SocketFactory;
import marvelapp.lyhoangvinh.com.marvelapp.BuildConfig;
import marvelapp.lyhoangvinh.com.marvelapp.constants.ConstantsApi;
import okhttp3.Cache;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    public static <T> T makeService(Class<T> serviceClass, Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsApi.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
        return retrofit.create(serviceClass);
    }

    public static OkHttpClient.Builder makeOkHttpClientBuilder(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        // : 4/26/2017 add the UnauthorisedInterceptor to this retrofit, or 401
        OkHttpClient.Builder builder = null;
        builder = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null)
                .connectTimeout(ConstantsApi.TIME_OUT_API, TimeUnit.SECONDS)
                .writeTimeout(ConstantsApi.TIME_OUT_API, TimeUnit.SECONDS)
                .readTimeout(ConstantsApi.TIME_OUT_API, TimeUnit.SECONDS);

        // add cache to client
        final File baseDir = context.getCacheDir();
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpResponseCache");
            builder.cache(new Cache(cacheDir, 10 * 1024 * 1024)); // 10 MB
        }

        return enableTls12OnPreLollipop(builder);
    }

    /**
     * Enable TLS 1.2 on Pre Lollipop android versions
     *
     * @param client OkHttpClient Builder
     * @return builder with SSL Socket Factory set
     * according to {@link OkHttpClient.Builder#sslSocketFactory(SSLSocketFactory)} deprecation,
     * Please add config SSL with {@link X509TrustManager} by using {@link }
     * * how to enable tls on android 4.4
     * <a href="https://github.com/square/okhttp/issues/2372"></a>
     */
    private static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                // TODO: 9/7/17 set SSL socket factory with X509TrustManager
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }

    /**
     * Make gson with custom date time deserializer
     *
     * @return {@link Gson} object
     */
    public static Gson makeGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
    }

    /**
     * Make gson which {@link DateDeserializer} and compatible with {@link RealmObject}
     *
     * @return {@link Gson} object
     */
    public static Gson makeGsonForRealm() {
        return new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
    }

    private static class DateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            String date = element.getAsString();
            SimpleDateFormat formatter = new SimpleDateFormat(ConstantsApi.DATE_TIME_FORMAT);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                return formatter.parse(date);
            } catch (ParseException e) {
                return null;
            }
        }
    }
}
