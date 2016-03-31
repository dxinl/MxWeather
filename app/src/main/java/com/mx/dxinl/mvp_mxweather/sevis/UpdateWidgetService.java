package com.mx.dxinl.mvp_mxweather.sevis;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mx.dxinl.mvp_mxweather.model.JSONHelper;
import com.mx.dxinl.mvp_mxweather.model.NetworkHelper;
import com.mx.dxinl.mvp_mxweather.model.SharedPreferencesHelper;
import com.mx.dxinl.mvp_mxweather.model.bean.NowWeatherBean;
import com.mx.dxinl.mvp_mxweather.vus.widget.Widget2_1;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

/**
 * Created by dxinl on 2016/3/29.
 */
public class UpdateWidgetService extends Service {
	private Thread updateWidgetThread;

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (updateWidgetThread == null || !updateWidgetThread.isAlive()) {
			updateWidgetThread = new Thread(new Runnable() {
				@Override
				public void run() {
					//noinspection InfiniteLoopStatement
					while (true) {
						System.out.println("UpdateWidgetService:" + String.valueOf(Calendar.getInstance().getTimeInMillis()));
						getDataAndUpdateWidget();
						System.gc();
						try {
							Thread.sleep(120 * 60 * 1000);
						} catch (InterruptedException ignored) {
						}
					}
				}
			});
			updateWidgetThread.start();
		}
		return START_REDELIVER_INTENT;
	}

	@SuppressWarnings("TryWithIdenticalCatches")
	private void getDataAndUpdateWidget() {
		SharedPreferencesHelper spHelper = new SharedPreferencesHelper(this);
		String[] cityInfo = spHelper.getCurrentCityInfo();
		if (cityInfo == null || cityInfo.length != 3) {
			cityInfo = new String[] {"北京", "CN101010100", "weather"};
		}
		NowWeatherBean nowWeather = null;
		try {
			JSONObject jsonObject = NetworkHelper.get().getJSONFromNetwork(3000, cityInfo[1], cityInfo[2]);
			if (jsonObject != null) {
				JSONHelper jsonHelper = new JSONHelper(jsonObject);
				if (jsonHelper.checkJSONObject()) {
					nowWeather = jsonHelper.getNowWeather();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent();
		intent.setAction(Widget2_1.REFRESH_ACTION);
		intent.putExtra("NowWeather", nowWeather);
		intent.putExtra("CurrentCity", cityInfo[0]);
		sendBroadcast(intent);
	}
}
