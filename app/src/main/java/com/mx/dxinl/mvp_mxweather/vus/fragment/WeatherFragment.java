package com.mx.dxinl.mvp_mxweather.vus.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.model.bean.AirQualityBean;
import com.mx.dxinl.mvp_mxweather.model.bean.DailyWeatherBean;
import com.mx.dxinl.mvp_mxweather.model.bean.HourlyWeatherBean;
import com.mx.dxinl.mvp_mxweather.model.bean.NowWeatherBean;
import com.mx.dxinl.mvp_mxweather.model.bean.SuggestionBean;
import com.mx.dxinl.mvp_mxweather.presenters.impl.WeatherPresenterImpl;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.WeatherPresenter;
import com.mx.dxinl.mvp_mxweather.vus.MainActivity;
import com.mx.dxinl.mvp_mxweather.vus.base.HasOptionsMenuFragment;
import com.mx.dxinl.mvp_mxweather.vus.interfaces.IWeatherView;
import com.mx.dxinl.mvp_mxweather.vus.view.DailyTemperatureView;
import com.mx.dxinl.mvp_mxweather.vus.view.HourlyWeatherView;
import com.mx.dxinl.mvp_mxweather.vus.widget.Widget2_1;

import java.util.List;

/**
 * Created by DengXinliang on 2015/12/31.
 */
public class WeatherFragment extends HasOptionsMenuFragment implements IWeatherView, SwipeRefreshLayout.OnRefreshListener {
	private WeatherPresenter presenter;
	private SwipeRefreshLayout swipeRefreshLayout;
	private HourlyWeatherView hourlyWeatherView;
	private DailyTemperatureView dailyTemperatureView;
	private ImageView weatherIcon;
	private TextView weatherDesc;
	private TextView temperature;
	private TextView airQuality;
	private TextView humidity;
	private TextView wind;
	private TextView visibility;
	private FrameLayout suggestion;
	private FrameLayout dayIcons;
	private FrameLayout nightIcons;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_weather, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		presenter = new WeatherPresenterImpl(this);

		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
		swipeRefreshLayout.setOnRefreshListener(this);

		hourlyWeatherView = (HourlyWeatherView) view.findViewById(R.id.hourly_weather);
		dailyTemperatureView = (DailyTemperatureView) view.findViewById(R.id.daily_tmp_weather);

		weatherIcon = (ImageView) view.findViewById(R.id.weather_icon);
		weatherDesc = (TextView) view.findViewById(R.id.weather_desc);
		temperature = (TextView) view.findViewById(R.id.temperature);
		airQuality = (TextView) view.findViewById(R.id.aqi);
		humidity = (TextView) view.findViewById(R.id.humidity);
		wind = (TextView) view.findViewById(R.id.wind);
		visibility = (TextView) view.findViewById(R.id.visibility);

		suggestion = (FrameLayout) view.findViewById(R.id.suggestion);
		dayIcons = (FrameLayout) view.findViewById(R.id.day_weather);
		nightIcons = (FrameLayout) view.findViewById(R.id.night_weather);


		swipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(true);
			}
		});
		onRefresh();
	}

	@Override
	public Context getContext() {
		return getActivity();
	}

	public void cancelGetWeatherTask() {
		presenter.cancelGetWeatherTask();
	}

	public void setHourlyWeathers(List<HourlyWeatherBean> hourlyWeathers) {
		if (hourlyWeathers != null) {
			hourlyWeatherView.setHourlyWeathers(hourlyWeathers);
		}
	}

	public void setDailyWeatherData(List<DailyWeatherBean> dailyWeathers) {
		if (dailyWeathers != null) {
			dailyTemperatureView.setData(dailyWeathers);
			dayIcons.setVisibility(View.VISIBLE);
			WeatherIconsFragment dayWeatherFragment = WeatherIconsFragment.newInstance(presenter, dailyWeathers);
			getChildFragmentManager().beginTransaction().replace(R.id.day_weather, dayWeatherFragment).commitAllowingStateLoss();

			nightIcons.setVisibility(View.VISIBLE);
			WeatherIconsFragment nightWeatherFragment = WeatherIconsFragment.newInstance(presenter, dailyWeathers, true);
			getChildFragmentManager().beginTransaction().replace(R.id.night_weather, nightWeatherFragment).commitAllowingStateLoss();
		}
	}

	public void setNowWeather(NowWeatherBean nowWeather) {
		if (nowWeather != null) {
			String tmp = nowWeather.tmp + "°";
			temperature.setText(tmp);
			setWeatherIcon(nowWeather.code);
			weatherDesc.setText(nowWeather.txt);

			String humStr = String.format(getString(R.string.humidity), nowWeather.hum) + "%";
			humidity.setText(humStr);
			wind.setText(String.format(getString(R.string.wind), nowWeather.dir + nowWeather.sc));
			visibility.setText(String.format(getString(R.string.visibility), nowWeather.vis));
		}
	}

	public void setAirQuality(AirQualityBean aqi) {
		if (aqi != null && aqi.aqi != null) {
			airQuality.setText(String.format(getString(R.string.aqi), aqi.aqi, aqi.qlty));
		} else {
			airQuality.setText(getString(R.string.not_aqi));
		}
	}

	private void setWeatherIcon(String code) {
		presenter.setImageBitmap(weatherIcon, code);
	}

	private void setSuggestion(SuggestionBean suggestionInfo) {
		if (suggestionInfo != null) {
			suggestion.setVisibility(View.VISIBLE);
			SuggestionFragment suggestionFragment = SuggestionFragment.newInstance(suggestionInfo);
			getChildFragmentManager().beginTransaction().replace(R.id.suggestion, suggestionFragment).commitAllowingStateLoss();
		}
	}

	@Override
	public void setData(List<HourlyWeatherBean> hourlyWeathers, NowWeatherBean nowWeather,
	                    AirQualityBean airQuality, List<DailyWeatherBean> dailyWeathers, SuggestionBean suggestion) {
		if (getActivity() != null) {
			setHourlyWeathers(hourlyWeathers);
			setDailyWeatherData(dailyWeathers);
			setNowWeather(nowWeather);
			setAirQuality(airQuality);
			setSuggestion(suggestion);
		}
	}

	@Override
	public void clearAllData() {
		hourlyWeatherView.clearData();
		dailyTemperatureView.clearData();

		weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
		weatherDesc.setText("");
		temperature.setText("");
		airQuality.setText("");
		humidity.setText("");
		wind.setText("");
		visibility.setText("");

		suggestion.setVisibility(View.INVISIBLE);
		dayIcons.setVisibility(View.INVISIBLE);
		nightIcons.setVisibility(View.INVISIBLE);
	}

	@Override
	public void setRefreshing(boolean isRefreshing) {
		swipeRefreshLayout.setRefreshing(isRefreshing);
	}

	@Override
	public void updateWidget2_1(NowWeatherBean nowWeather) {
		Intent intent = new Intent();
		intent.setAction(Widget2_1.REFRESH_ACTION);
		intent.putExtra("NowWeather", nowWeather);
		intent.putExtra("CurrentCity", ((MainActivity) getActivity()).getCurrentCityName());
		getActivity().sendBroadcast(intent);
	}

	@Override
	public void onRefresh() {
		cancelGetWeatherTask();
		presenter.showData(((MainActivity) getActivity()).getCurrentCityNum(), ((MainActivity) getActivity()).getCurrentCityType());
		getActivity().setTitle(((MainActivity) getActivity()).getCurrentCityName());
	}
}
