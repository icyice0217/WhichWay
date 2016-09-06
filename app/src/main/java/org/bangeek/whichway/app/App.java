package org.bangeek.whichway.app;

import android.app.Application;
import android.content.Context;

import org.bangeek.shjt.utils.ServiceUtils;

/**
 * Created by BinGan on 2016/9/6.
 */
public class App extends Application {
    public static final String TAG = "WhichWay";
    static Context _context;

    public static synchronized App context() {
        return (App) _context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _context = getApplicationContext();

        ServiceUtils.getApiList();
    }
}
