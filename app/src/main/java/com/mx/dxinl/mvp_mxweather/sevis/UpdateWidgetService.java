package com.mx.dxinl.mvp_mxweather.sevis;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.mx.dxinl.mvp_mxweather.model.JSONHelper;
import com.mx.dxinl.mvp_mxweather.model.NetworkHelper;
import com.mx.dxinl.mvp_mxweather.model.SharedPreferencesHelper;
import com.mx.dxinl.mvp_mxweather.model.bean.NowWeatherBean;
import com.mx.dxinl.mvp_mxweather.utils.OtherUtils;
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
	private UpdateWidgetThread updateWidgetThread;

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (updateWidgetThread == null || !updateWidgetThread.isAlive()) {
			updateWidgetThread = new UpdateWidgetThread();
			updateWidgetThread.start();
		}
		return START_REDELIVER_INTENT;
	}

	@SuppressWarnings("TryWithIdenticalCatches")
	private void getDataAndUpdateWidget() {
		SharedPreferencesHelper spHelper = new SharedPreferencesHelper(this);
		String[] cityInfo = spHelper.getCurrentCityInfo();
		if (cityInfo == null || cityInfo.length != 3) {
			cityInfo = new String[]{"北京", "CN101010100", "weather"};
		}
		NowWeatherBean nowWeather = null;
		try {
			JSONObject jsonObject;
			if (!OtherUtils.isDebug()) {
				jsonObject = NetworkHelper.get().getJSONFromNetwork(3000, cityInfo[1], cityInfo[2]);
			} else {
				jsonObject = NetworkHelper.get().getJSON();
			}
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

	@Override
	public void onDestroy() {
		updateWidgetThread.stopThread();
		updateWidgetThread = null;
		super.onDestroy();
	}

	private final class UpdateWidgetThread extends Thread {
		private volatile boolean stopThread = false;

		@Override
		public void run() {
			while (!stopThread) {
				if (OtherUtils.isDebug()) {
					SystemClock.sleep(1000);
				} else {
					SystemClock.sleep(120 * 60 * 1000);
				}

				System.out.println("UpdateWidgetService:" + String.valueOf(Calendar.getInstance().getTimeInMillis()));
				getDataAndUpdateWidget();
				System.gc();
			}
		}

		public void stopThread() {
			stopThread = true;
		}
	}
}
