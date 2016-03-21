package com.mx.dxinl.mvp_mxweather.presenters.interfaces;

import android.widget.ImageView;

/**
 * Created by DengXinliang on 2016/1/28.
 */
public interface WeatherPresenter {

	void showData(String name);

	void cancelGetWeatherTask();

	void setImageBitmap(ImageView view, String code);
}
