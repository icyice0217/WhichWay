package org.bangeek.shjt.web.services;

import android.os.Build;
import android.util.Log;

import org.bangeek.shjt.factories.StringConverterFactory;
import org.bangeek.shjt.web.interfs.IWebService;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;

/**
 * Created by BinGan on 2016/9/6.
 */
public class WebService {
    public static final String TAG = "WebService";
    public static final String USER_AGENT = String.format("Dalvik/2.1.0 (Linux; U; Android %s; %s Build/%s)", Build.VERSION.RELEASE, Build.MODEL, Build.ID);//Fake User Agent

    private volatile static WebService mInstance = null;
    private IWebService mService;

    public static WebService getInstance() {
        if (mInstance == null) {
            synchronized (WebService.class) {
                if (mInstance == null) {
                    mInstance = new WebService();
                }
            }
        }
        return mInstance;
    }

    private WebService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d(WebService.TAG, message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.215", 8888)))
//                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.16.36.112", 8888)))
                .build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl("http://www.jt.sh.cn")
                .addConverterFactory(new StringConverterFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        mService = mRetrofit.create(IWebService.class);
    }

    public Observable<String> get(String url) {
        return mService.GetRequest(USER_AGENT, url);
    }
}
