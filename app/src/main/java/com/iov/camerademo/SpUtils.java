package com.iov.camerademo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by youchao on 2018/7/26.
 */

public class SpUtils {

    private static SpUtils spUtils;

    private SpUtils() {
    }

    public static SpUtils getInstance() {
        if (spUtils == null) {
            spUtils = new SpUtils();
        }

        return spUtils;
    }

    private SharedPreferences sharedPreferences = null;

    private Context context = null;

    public void init(Context context) {
        this.context = context;
    }

    public SpUtils getSp(String spName) {
        sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return spUtils;
    }

    @SuppressLint("ApplySharedPref")
    public void saveString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, " ");
    }
}
