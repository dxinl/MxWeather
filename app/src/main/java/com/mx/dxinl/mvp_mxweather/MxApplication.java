package com.mx.dxinl.mvp_mxweather;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Deng Xinliang on 2016/8/9.
 */
public class MxApplication extends Application {
    private static final String SDCARD_PATH    = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String IMAGE_PATH     = "MxWeather" + File.separator + "Image";

    private static Context app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static String getAppExternalCacheDir() {
        File file = app.getExternalCacheDir();
        if (file != null) {
            return file.getAbsolutePath();
        } else {
            return SDCARD_PATH + File.separator + IMAGE_PATH;
        }
    }
}
