package org.bangeek.shjt.web.interfs;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by BinGan on 2016/9/6.
 */
public interface IWebService {
    @GET()
    Observable<String> GetRequest(@Header("User-Agent") String ua, @Url String url);
}
