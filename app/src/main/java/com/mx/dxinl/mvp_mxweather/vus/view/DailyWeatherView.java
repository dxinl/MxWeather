package com.mx.dxinl.mvp_mxweather.vus.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DengXinliang on 2016/3/15.
 */
public class DailyWeatherView extends View {
	public DailyWeatherView(Context context) {
		super(context);
	}

	public DailyWeatherView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DailyWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public DailyWeatherView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
