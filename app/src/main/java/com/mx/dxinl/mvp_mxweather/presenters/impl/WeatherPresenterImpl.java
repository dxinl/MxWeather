package com.mx.dxinl.mvp_mxweather.presenters.impl;

import android.os.AsyncTask;
import android.util.Log;
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

/**
 * Created by DengXinliang on 2016/1/28.
 */
public class WeatherPresenterImpl implements WeatherPresenter {
	private IWeatherView view;
	private RequestCityWeatherTask requestTask;

	public WeatherPresenterImpl(IWeatherView view) {
		this.view = view;
	}

	@Override
	public void onNavigationItemSelected(int itemId) {

	}

	@Override
	public int onOptionsItemSelected(int itemId) {
		int result = -1;
		switch (itemId) {
			case R.id.action_settings:
				break;
		}
		return result;
	}

	@Override
	public void showData(String name) {
		requestTask = new RequestCityWeatherTask();
		requestTask.execute(name, "weather");
	}

	@Override
	public void cancelGetWeatherTask() {
		if (requestTask != null) {
			requestTask.cancel(true);
			Log.d(WeatherPresenterImpl.class.getSimpleName(), "cancel request task");
		}
		ImageLoader.get().cancelGetImgTask();
	}

	@Override
	public void setImageBitmap(ImageView view, String code) {
		ImageLoader.get().setImageBitmap(view, code);
	}

	private final class RequestCityWeatherTask extends AsyncTask<String, Object, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return NetworkHelper.get().getJSONFromNetwork(3000, params[0], params[1]);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			super.onPostExecute(jsonObject);
			if (jsonObject == null) {
				Log.e(WeatherPresenterImpl.class.getSimpleName(), "Cannot GET JSON From Network.");
				return;
			}

			JSONHelper jsonHelper = new JSONHelper(jsonObject);
			if (!jsonHelper.checkJSONObject()) {
				Toast.makeText(view.getContext(), R.string.incorrect_city, Toast.LENGTH_SHORT).show();
				return;
			}

			List<HourlyWeatherBean> hourlyWeathers = jsonHelper.getHourlyWeather();
			NowWeatherBean nowWeather = jsonHelper.getNowWeather();
			AirQualityBean airQuality = jsonHelper.getAirQuality();
			List<DailyWeatherBean> dailyWeathers = jsonHelper.getDailyWeathers();
			SuggestionBean suggestion = jsonHelper.getSuggestion();

			view.setData(hourlyWeathers, nowWeather, airQuality, dailyWeathers, suggestion);
			view.setRefreshing(false);
		}
	}
}
