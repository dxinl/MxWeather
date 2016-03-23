package com.mx.dxinl.mvp_mxweather.vus.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.RemoteViews;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.model.bean.NowWeatherBean;
import com.mx.dxinl.mvp_mxweather.presenters.impl.WidgetPresenterImpl;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.WidgetPresenter;
import com.mx.dxinl.mvp_mxweather.vus.MainActivity;

/**
 * Created by DengXinliang on 2016/3/23.
 */
public class Widget2_1 extends AppWidgetProvider {
	public static final String REFRESH_ACTION = "com.mx.dxinl.mxweather.action.REFRESH";
	private static final String APP_WIDGET_ID_STR = "appWidgetId";

	@Override
	public void onReceive(final Context context, final Intent intent) {
		super.onReceive(context, intent);

		if (intent.getAction().equals(REFRESH_ACTION)) {
			final NowWeatherBean nowWeather = intent.getParcelableExtra("NowWeather");
			final String cityName = intent.getStringExtra("CurrentCity");
			new Thread(new Runnable() {
				@Override
				public void run() {
					WidgetPresenter presenter = new WidgetPresenterImpl();
					RemoteViews remoteViews = new RemoteViews(
							context.getPackageName(), R.layout.widget2_1);
					Bitmap bitmap = presenter.getImageBitmap(context, nowWeather.code);
					remoteViews.setImageViewBitmap(
							R.id.weather_icon, presenter.drawWeatherIconForWidget(context, bitmap));
					remoteViews.setTextViewText(R.id.temperature,
							nowWeather.tmp + context.getResources().getString(R.string.tmp));
					remoteViews.setTextViewText(R.id.city_name, cityName != null ? cityName : "");
					setOnClickPendingIntent(context, remoteViews);

					AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
					appWidgetManager.updateAppWidget(new ComponentName(context, Widget2_1.class), remoteViews);
				}
			}).start();
		}
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

	private void setOnClickPendingIntent(Context context, RemoteViews remoteViews) {
		Intent clickIntent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget2_1, pendingIntent);
	}
}
