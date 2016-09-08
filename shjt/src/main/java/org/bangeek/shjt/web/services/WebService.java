package org.bangeek.shjt.web.services;

import android.os.Build;
import android.util.Log;

import org.bangeek.shjt.factories.StringConverterFactory;
import org.bangeek.shjt.web.interfs.IWebService;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

    public static String getSignParams() {
        String time = new SimpleDateFormat("yyyy-MM-ddHH:mm", Locale.CHINA).format(new Date(System.currentTimeMillis()));
        String my = getHashByMd5(time);
        return String.format("my=%s&t=%s", my, time);
    }

    public static String getHashByMd5(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(message.getBytes("UTF-8"));
            return bytesToHexString(digest);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Convert byte[] to hex string
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }
}
