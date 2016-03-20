package com.mx.dxinl.mvp_mxweather.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by DengXinliang on 2016/1/4.
 */
public class OtherUtils {
	public static int getScreenWidth(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return metrics.widthPixels;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return metrics.heightPixels;
	}

	public static String getString(String s) {
		return s == null ? "" : s;
	}
}
