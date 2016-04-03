package com.mx.dxinl.mvp_mxweather.presenters.impl;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.model.JSONHelper;
import com.mx.dxinl.mvp_mxweather.model.NetworkHelper;
import com.mx.dxinl.mvp_mxweather.model.bean.AirQualityBean;
import com.mx.dxinl.mvp_mxweather.model.bean.DailyWeatherBean;
import com.mx.dxinl.mvp_mxweather.model.bean.HourlyWeatherBean;
import com.mx.dxinl.mvp_mxweather.model.bean.NowWeatherBean;
import com.mx.dxinl.mvp_mxweather.model.bean.SuggestionBean;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.WeatherPresenter;
import com.mx.dxinl.mvp_mxweather.utils.ImageLoader;
import com.mx.dxinl.mvp_mxweather.vus.interfaces.IWeatherView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by DengXinliang on 2016/1/28.
 */
public class WeatherPresenterImpl implements WeatherPresenter {
	private final int UPDATE_MSG_WHAT = 0x0408;
	private IWeatherView view;
	private UpdateWeatherHandler mHandler;
	private boolean stopSettingData;

	public WeatherPresenterImpl(IWeatherView view) {
		this.view = view;
		this.mHandler = new UpdateWeatherHandler();
	}

	@Override
	@SuppressWarnings("TryWithIdenticalCatches")
	public void showData(final String num, final String type) {
		stopSettingData = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject jsonObject = null;
				try {
					jsonObject = NetworkHelper.get().getJSONFromNetwork(3000, num, type);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}

				Message msg = mHandler.obtainMessage(UPDATE_MSG_WHAT);
				msg.obj = jsonObject;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	public void cancelGetWeatherTask() {
		stopSettingData = true;
		ImageLoader.get().cancelGetImgTask();
	}

	@Override
	public void setImageBitmap(ImageView view, String code) {
		ImageLoader.get().setImageBitmap(view, code);
	}

	private final class UpdateWeatherHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == UPDATE_MSG_WHAT) {
				if (stopSettingData) {
					return;
				}

				JSONObject jsonObject = (JSONObject) msg.obj;
				view.setRefreshing(false);
				if (jsonObject == null) {
					Toast.makeText(view.getIViewContext(), R.string.cannot_get_data_from_network, Toast.LENGTH_SHORT).show();
					view.clearAllData();
					return;
				}

				JSONHelper jsonHelper = new JSONHelper(jsonObject);
				if (!jsonHelper.checkJSONObject()) {
					Toast.makeText(view.getIViewContext(), R.string.incorrect_city, Toast.LENGTH_SHORT).show();
					view.clearAllData();
					return;
				}

				List<HourlyWeatherBean> hourlyWeathers = jsonHelper.getHourlyWeather();
				NowWeatherBean nowWeather = jsonHelper.getNowWeather();
				AirQualityBean airQuality = jsonHelper.getAirQuality();
				List<DailyWeatherBean> dailyWeathers = jsonHelper.getDailyWeathers();
				SuggestionBean suggestion = jsonHelper.getSuggestion();

				view.setData(hourlyWeathers, nowWeather, airQuality, dailyWeathers, suggestion);
				view.updateWidget2_1(nowWeather);
			}
		}
	}
}
