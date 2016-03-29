package com.mx.dxinl.mvp_mxweather.vus.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.model.bean.HourlyWeatherBean;
import com.mx.dxinl.mvp_mxweather.utils.OtherUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DengXinliang on 2015/12/31.
 */
public class HourlyWeatherView extends View {
	private final int MAX_INT = 999999;

	private List<HourlyWeatherBean> hourlyWeathers;
	private int maxTemperature = -MAX_INT;
	private int minTemperature = MAX_INT;
	private int size;
	private Paint paint = new Paint();
	private RectF rectF = new RectF();
	List<Pair<Float, Float>> centerPairs = new ArrayList<>();

	public HourlyWeatherView(Context context) {
		super(context);
	}

	public HourlyWeatherView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HourlyWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public HourlyWeatherView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		if (widthMode == MeasureSpec.UNSPECIFIED) {
			width = OtherUtils.getScreenWidth(getContext());
		}

		if (heightMode == MeasureSpec.UNSPECIFIED) {
			height = OtherUtils.getScreenHeight(getContext()) / 6;
		}

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (hourlyWeathers == null) {
			rectF.set(0, 0, getWidth(), getHeight());
			paint.setColor(Color.WHITE);
			canvas.drawRect(rectF, paint);
			return;
		}

		float widthUnit = getWidth() / (float) size;
		float chartPadding = getResources().getDimension(R.dimen.middle_margin);
		float chartHeightUnit = (getHeight() / 4f * 2f - chartPadding * 2) / (maxTemperature - minTemperature);
		float textHeightUnit = getHeight() / 4f;
		float radius = getResources().getDimension(R.dimen.circle_radius);

		paint.setAntiAlias(true);
		paint.setTextAlign(Paint.Align.CENTER);
		float normalTxtSize = getResources().getDimension(R.dimen.normal_txt_size);
		for (int i = 0; i < size; i++) {
			float widthStart = widthUnit * i;
			float widthEnd = widthStart + widthUnit;

			rectF.set(widthStart, 0, widthEnd, textHeightUnit);
			paint.setTextSize(normalTxtSize);
			paint.setColor(getResources().getColor(R.color.light_green_700));
			canvas.drawText(OtherUtils.getString(hourlyWeathers.get(i).date),
					rectF.centerX(), rectF.centerY() + normalTxtSize / 2f, paint);

			float centerX = widthStart + widthUnit / 2f;
			float centerY = (maxTemperature + 1 - Integer.valueOf(hourlyWeathers.get(i).tmp)) * chartHeightUnit + textHeightUnit;
			centerPairs.add(Pair.create(centerX, centerY));

			paint.setColor(getResources().getColor(R.color.light_green_700));
			paint.setStrokeWidth(0);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(centerX, centerY, radius, paint);

			paint.setColor(getResources().getColor(R.color.circle));
			paint.setStrokeWidth(radius);
			paint.setStyle(Paint.Style.STROKE);
			float strokeRadius = radius * 2f - paint.getStrokeWidth() / 2;
			canvas.drawCircle(centerX, centerY, strokeRadius, paint);

			if (i > 0) {
				Pair<Float, Float> pre = centerPairs.get(i - 1);
				paint.setColor(getResources().getColor(R.color.light_green_700));
				paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.line_width));
				paint.setStyle(Paint.Style.FILL_AND_STROKE);
				canvas.drawLine(pre.first, pre.second, centerX, centerY, paint);
			}

			paint.setStrokeWidth(0);
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(getResources().getColor(R.color.light_green_700));
			canvas.drawText(OtherUtils.getString(hourlyWeathers.get(i).tmp) + "°",
					centerX, centerY + (textHeightUnit + normalTxtSize) / 2f, paint);
		}

		centerPairs.clear();
	}

	public void setHourlyWeathers(@NonNull List<HourlyWeatherBean> hourlyWeathers) {
		this.hourlyWeathers = hourlyWeathers;
		size = hourlyWeathers.size();
		for (HourlyWeatherBean hourlyWeather : hourlyWeathers) {
			int tmp = Integer.valueOf(hourlyWeather.tmp);
			if (tmp > maxTemperature) {
				maxTemperature = tmp;
			}

			if (tmp < minTemperature) {
				minTemperature = tmp;
			}
		}

		invalidate();
	}

	public void clearData() {
		hourlyWeathers = null;
		invalidate();
	}
}
