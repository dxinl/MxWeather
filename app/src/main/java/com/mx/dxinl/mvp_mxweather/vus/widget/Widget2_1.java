package com.mx.dxinl.mvp_mxweather.vus.widget;

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

/**
 * Created by DengXinliang on 2016/3/23.
 */
public class Widget2_1 extends AppWidgetProvider {
	public static final String REFRESH_ACTION = "com.mx.dxinl.mxweather.action.REFRESH";
	private static final String APP_WIDGET_ID_STR = "appWidgetId";

	@Override
	public void onReceive(final Context context, Intent intent) {
		super.onReceive(context, intent);

		if (intent.getAction().equals(REFRESH_ACTION)) {
			final NowWeatherBean nowWeather = intent.getParcelableExtra("NowWeather");
			final String cityName = intent.getStringExtra("CurrentCity");
			new Thread(new Runnable() {
				@Override
				public void run() {
					WidgetPresenter presenter = new WidgetPresenterImpl();
					RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget2_1);
					Bitmap bitmap = presenter.getImageBitmap(nowWeather.code);
					if (bitmap != null) {
						remoteViews.setImageViewBitmap(R.id.weather_icon, drawImageBitmap(context, bitmap));
					} else {
						remoteViews.setImageViewBitmap(R.id.weather_icon,
								BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
					}
					remoteViews.setTextViewText(R.id.temperature, nowWeather.tmp + context.getResources().getString(R.string.tmp));
					remoteViews.setTextViewText(R.id.city_name, cityName != null ? cityName : "");

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
		appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
	}

	private Bitmap drawImageBitmap(Context context, Bitmap bitmap) {
		try {
			Bitmap bkgBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.transparent_circle_bkg_white);
			int width = bkgBmp.getWidth();
			int height = bkgBmp.getHeight();
			Bitmap tmpBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(tmpBmp);

			canvas.drawBitmap(bkgBmp, 0, 0, null);
			canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
					new Rect(width / 6, height / 6, width * 5 / 6, height * 5 / 6), new Paint(Paint.ANTI_ALIAS_FLAG));
			return tmpBmp;
		} catch (Exception e) {
			e.printStackTrace();
			return bitmap;
		}
	}
}
