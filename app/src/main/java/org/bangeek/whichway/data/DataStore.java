package org.bangeek.whichway.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.bangeek.shjt.models.Line;
import org.bangeek.shjt.models.LineCollection;

import java.lang.reflect.Type;

/**
 * Created by BinGan on 2016/9/6.
 */
public class DataStore {
    private static final String DATA_STORE_API_LIST = "DATA_STORE_API_LIST";
    private static final String DATA_STORE_BUS_LINES = "DATA_STORE_BUS_LINES";

    public static final String STOP_LINE_JSON_ASSET = "linestop.json";

    public static void saveData(Context context, Object object, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, new Gson().toJson(object));
        editor.apply();
    }

    public static <T> T loadData(Context context, String name, Class<T> classOfT) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        String data = sharedPreferences.getString(name, null);
        if (TextUtils.isEmpty(data)) return null;
        return new Gson().fromJson(data, classOfT);
    }

    public static <T> T loadData(Context context, String name, Type typeOfT) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        String data = sharedPreferences.getString(name, null);
        if (TextUtils.isEmpty(data)) return null;
        return new Gson().fromJson(data, typeOfT);
    }

    public static void saveBusLines(Context context, Object busLines) {
        if (busLines instanceof LineCollection) {
            LineCollection lineCollection = (LineCollection) busLines;

            SharedPreferences sharedPreferences = context.getSharedPreferences(DATA_STORE_BUS_LINES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("version", lineCollection.getVersion());
            if (lineCollection.getList().size() > 0) {
                for (Line line : lineCollection.getList()) {
                    editor.putString(line.getName(), line.getValue());
                }
            }
            editor.apply();
        }
    }

    public static int getBuslinesVersion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATA_STORE_BUS_LINES, Context.MODE_PRIVATE);
        try {
            return Integer.parseInt(sharedPreferences.getString("version", "0"));
        } catch (Exception ex) {
            return 0;
        }
    }

    public static String getBuslineByName(Context context, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATA_STORE_BUS_LINES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, null);
    }
}
