package com.boredream.bdcodehelper.utils;

import android.util.Log;

public class LogUtils {

    public static void showLog(String log) {
        showLog("DDD", log);
    }

    public static void showLog(String tag, String log) {
        Log.i(tag, log);
    }
}
