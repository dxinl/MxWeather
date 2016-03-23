package com.mx.dxinl.mvp_mxweather.presenters.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by DengXinliang on 2016/3/23.
 */
public interface WidgetPresenter {
	Bitmap getImageBitmap(Context context, String code);

	Bitmap drawWeatherIconForWidget(Context context, Bitmap bitmap);
}
