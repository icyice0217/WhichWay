package org.bangeek.shjt.utils;

import android.text.TextUtils;
import android.util.Log;

import org.bangeek.shjt.BuildConfig;
import org.bangeek.shjt.models.ApiModel;
import org.bangeek.shjt.web.services.WebService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by BinGan on 2016/9/6.
 */
public class ServiceUtils {
    private final static String API_ADDRESS_QUERY = "http://www.jt.sh.cn/trafficWeb/lbs/modify.xml";
    private final static String BUS_LINE_LIST_QUERY = "px_line";
    private final static String CAR_MONITOR_QUERY = "px_car_monitor";
    private static List<ApiModel> apiModels = null;

    public static void getApiList() {
        WebService.getInstance().get(API_ADDRESS_QUERY)
                .observeOn(Schedulers.io())
                .map(new Func1<String, List<ApiModel>>() {
                    @Override
                    public List<ApiModel> call(String s) {
                        try {
                            InputStream is = new ByteArrayInputStream(s.getBytes(), 0, s.getBytes().length);
                            return (List<ApiModel>) XmlUtils.parse(is, XmlUtils.ParseType.ApiList);
                        } catch (Exception ex) {
                            Log.e(BuildConfig.TAG, "Parsing xml failed.");
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<List<ApiModel>>() {
                    @Override
                    public void call(List<ApiModel> apiModels) {
                        //onNext
                        ServiceUtils.apiModels = apiModels;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        //onError
                        Log.e(BuildConfig.TAG, "Error: " + throwable);
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        //onComplete
                        Log.d(BuildConfig.TAG, "Update API List successfully.");
                    }
                });
    }

    public static void getBusLineList(Action1<Object> onNext) {
        ApiModel api = getApiByName(BUS_LINE_LIST_QUERY);
        if (api == null) return;
        String url = api.getUrl();
        if (TextUtils.isEmpty(url)) return;

        WebService.getInstance().get(url)
                .observeOn(Schedulers.io())
                .map(new Func1<String, Object>() {
                    @Override
                    public Object call(String s) {
                        try {
                            InputStream is = new ByteArrayInputStream(s.getBytes(), 0, s.getBytes().length);
                            return XmlUtils.parse(is, XmlUtils.ParseType.BusLine);
                        } catch (Exception ex) {
                            Log.e(BuildConfig.TAG, "Parsing xml failed.");
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(onNext,
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                //onError
                                Log.e(BuildConfig.TAG, "Error: " + throwable);
                            }
                        }, new Action0() {
                            @Override
                            public void call() {
                                //onComplete
                                Log.d(BuildConfig.TAG, "Update API List successfully.");
                            }
                        });
    }

    public static void getCarsByLine(String lineId, String stopId, boolean direction, Action1<Object> onNext) {
        ApiModel api = getApiByName(CAR_MONITOR_QUERY);
        if (api == null) return;
        String url = api.getUrl();
        if (TextUtils.isEmpty(url)) return;
        url = String.format("%s?lineid=%s&stopid=%s&direction=%s&my=%s&t=%s", url, lineId, stopId,
                direction ? "true" : "false", "HELLOWORLD",
                new SimpleDateFormat("yyyy-MM-ddHH:mm", Locale.CHINA).format(new Date(System.currentTimeMillis())));

        WebService.getInstance().get(url)
                .observeOn(Schedulers.io())
                .map(new Func1<String, Object>() {
                    @Override
                    public Object call(String s) {
                        try {
                            InputStream is = new ByteArrayInputStream(s.getBytes(), 0, s.getBytes().length);
                            return XmlUtils.parse(is, XmlUtils.ParseType.Cars);
                        } catch (Exception ex) {
                            Log.e(BuildConfig.TAG, "Parsing xml failed.");
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(onNext,
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                //onError
                                Log.e(BuildConfig.TAG, "Error: " + throwable);
                            }
                        }, new Action0() {
                            @Override
                            public void call() {
                                //onComplete
                                Log.d(BuildConfig.TAG, "Update API List successfully.");
                            }
                        });
    }

    public static boolean hasUpdateLinesData(int version) {
        ApiModel api = getApiByName(BUS_LINE_LIST_QUERY);
        if (api == null)
            return false;
        try {
            int apiVersion = Integer.parseInt(api.getVersion());
            return apiVersion > version;
        } catch (Exception ex) {
            return false;
        }
    }

    private static ApiModel getApiByName(String name) {
        if (apiModels == null || name == null) return null;
        for (ApiModel api : apiModels) {
            if (name.equals(api.getName())) {
                return api;
            }
        }
        return null;
    }
}
