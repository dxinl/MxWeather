package com.mx.dxinl.mvp_mxweather.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import com.mx.dxinl.mvp_mxweather.R;

/**
 * Created by DengXinliang on 2016/1/4.
 */
public class OtherUtils {
	private static final boolean DEBUG = false;

	public static boolean isDebug() {
		return DEBUG;
	}

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

	public static Bitmap drawWeatherIconWithCircleBkg(Context context, Bitmap bitmap) {
		try {
			Bitmap bkgBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.transparent_circle_bkg_white);
			int width = bkgBmp.getWidth();
			int height = bkgBmp.getHeight();
			int padding = context.getResources().getDimensionPixelSize(R.dimen.small_padding);
			Bitmap tmpBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(tmpBmp);

			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			paint.setDither(true);

			canvas.drawBitmap(bkgBmp, 0, 0, paint);
			canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
					new RectF(padding, padding, width - padding, height - padding), paint);
			return tmpBmp;
		} catch (Exception e) {
			e.printStackTrace();
			return bitmap;
		}
	}

}
