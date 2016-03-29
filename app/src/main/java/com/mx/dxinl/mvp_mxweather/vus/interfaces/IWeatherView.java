package com.mx.dxinl.mvp_mxweather.vus.interfaces;

import android.content.Context;

import com.mx.dxinl.mvp_mxweather.model.bean.AirQualityBean;
import com.mx.dxinl.mvp_mxweather.model.bean.DailyWeatherBean;
import com.mx.dxinl.mvp_mxweather.model.bean.HourlyWeatherBean;
import com.mx.dxinl.mvp_mxweather.model.bean.NowWeatherBean;
import com.mx.dxinl.mvp_mxweather.model.bean.SuggestionBean;

import java.util.List;

/**
 * Created by DengXinliang on 2016/1/28.
 */
public interface IWeatherView {
	void setData(List<HourlyWeatherBean> hourlyWeathers, NowWeatherBean nowWeather,
	             AirQualityBean airQuality, List<DailyWeatherBean> dailyWeathers, SuggestionBean suggestion);

	void clearAllData();

	void setRefreshing(boolean isRefreshing);

	void updateWidget2_1(NowWeatherBean nowWeatherBean);

	Context getContext();
}
