package com.mx.dxinl.mvp_mxweather.vus.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.model.bean.NowWeatherBean;
import com.mx.dxinl.mvp_mxweather.sevis.UpdateWidgetService;
import com.mx.dxinl.mvp_mxweather.utils.ImageLoader;
import com.mx.dxinl.mvp_mxweather.utils.OtherUtils;
import com.mx.dxinl.mvp_mxweather.vus.MainActivity;

/**
 * Created by DengXinliang on 2016/3/23.
 */
public class Widget2_1 extends AppWidgetProvider {
	public static final String REFRESH_ACTION = "com.mx.dxinl.mxweather.action.REFRESH";
	private static final String APP_WIDGET_ID_STR = "appWidgetId";
	private boolean isServiceRunning = false;

	@Override
	public void onReceive(final Context context, final Intent intent) {
		super.onReceive(context, intent);

		if (intent.getAction().equals(REFRESH_ACTION)) {
			final NowWeatherBean nowWeather = intent.getParcelableExtra("NowWeather");
			final String cityName = intent.getStringExtra("CurrentCity");
			new Thread(new Runnable() {
				@Override
				public void run() {
					makeRemoteViewsAndUpdate(context, nowWeather, cityName);
				}
			}).start();
		}

		Intent serviceIntent = new Intent(context, UpdateWidgetService.class);
		context.startService(serviceIntent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		for (int appWidgetId : appWidgetIds) {
			onWidgetUpdate(context, appWidgetManager, appWidgetId);
		}
	}

	private void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget2_1);
		setOnClickPendingIntent(context, remoteViews);
		appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
	}

	private void makeRemoteViewsAndUpdate(Context context, NowWeatherBean nowWeather, String cityName) {
		RemoteViews remoteViews = makeRemoteViews(context, nowWeather, cityName);
		setOnClickPendingIntent(context, remoteViews);

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		appWidgetManager.updateAppWidget(new ComponentName(context, Widget2_1.class), remoteViews);
	}

	private RemoteViews makeRemoteViews(Context context, NowWeatherBean nowWeather, String cityName) {
		RemoteViews remoteViews = new RemoteViews(
				context.getPackageName(), R.layout.widget2_1);

		if (nowWeather == null) {
			return remoteViews;
		}

		Bitmap bitmap = getImageBitmap(context, nowWeather.code);
		remoteViews.setImageViewBitmap(
				R.id.weather_icon, OtherUtils.drawWeatherIconWithCircleBkg(context, bitmap));
		remoteViews.setTextViewText(R.id.temperature,
				nowWeather.tmp + context.getResources().getString(R.string.tmp));
		remoteViews.setTextViewText(R.id.city_name, cityName != null ? cityName : "");

		return remoteViews;
	}

	private void setOnClickPendingIntent(Context context, RemoteViews remoteViews) {
		Intent clickIntent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget2_1, pendingIntent);
	}

	private Bitmap getImageBitmap(Context context, String code) {
		Bitmap bitmap = ImageLoader.get().getImageBitmap(code);
		if (bitmap == null) {
			return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
		}
		return bitmap;
	}
}
