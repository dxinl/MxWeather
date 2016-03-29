package com.mx.dxinl.mvp_mxweather.vus.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.model.bean.DailyWeatherBean;
import com.mx.dxinl.mvp_mxweather.utils.OtherUtils;

import java.util.List;

/**
 * Created by DengXinliang on 2016/3/15.
 */
public class DailyTemperatureView extends View {
	private final int MAX_INT = 999999;
	private List<DailyWeatherBean> dailyWeathers;
	private CoordinationPair preDayTmp[];
	private int maxTemperature = -MAX_INT;
	private int minTemperature = MAX_INT;
	private Paint paint = new Paint();
	private RectF rectF = new RectF();
	private Rect imgRect = new Rect();

	public DailyTemperatureView(Context context) {
		super(context);
	}

	public DailyTemperatureView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DailyTemperatureView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public DailyTemperatureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
			height = OtherUtils.getScreenHeight(getContext()) / 4;
		}

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (dailyWeathers == null || dailyWeathers.size() == 0) {
			rectF.set(0, 0, getWidth(), getHeight());
			paint.setColor(Color.WHITE);
			canvas.drawRect(rectF, paint);
			return;
		}

		float smallPadding = getResources().getDimension(R.dimen.small_padding);
		float middlePadding = getResources().getDimension(R.dimen.middle_padding);
		float normalTxtSize = getResources().getDimension(R.dimen.normal_txt_size);
		float sumPadding = middlePadding * 2 + smallPadding + normalTxtSize * 2;

		float widthUnit = getMeasuredWidth() / (float) dailyWeathers.size();
		float heightUnit = (getMeasuredHeight() - sumPadding) / (float) (maxTemperature - minTemperature + 2);
		float circleTop = heightUnit + smallPadding + normalTxtSize * 2;
		float circleRadius = getResources().getDimension(R.dimen.circle_radius);

		paint.setAntiAlias(true);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextSize(normalTxtSize);
		paint.setStrokeWidth(0);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(getResources().getColor(R.color.light_green_700));

		for (int i = 0; i < dailyWeathers.size(); i++) {
			DailyWeatherBean dailyWeather = dailyWeathers.get(i);

			float centerX = widthUnit * i + widthUnit / 2f;
			canvas.drawText(dailyWeather.date, centerX, normalTxtSize * 1.5f, paint);

			int maxUnitIndex = maxTemperature - Integer.valueOf(dailyWeather.maxTmp);
			float maxCenterY = circleTop + maxUnitIndex * heightUnit + heightUnit / 2f;
			drawCircles(canvas, centerX, maxCenterY, circleRadius);

			int minUnitIndex = maxTemperature - Integer.valueOf(dailyWeather.minTmp);
			float minCenterY = circleTop + minUnitIndex * heightUnit + heightUnit / 2f;
			drawCircles(canvas, centerX, minCenterY, circleRadius);

			if (i > 0) {
				drawLines(canvas, centerX, maxCenterY, preDayTmp[0]);
				drawLines(canvas, centerX, minCenterY, preDayTmp[1]);
			}
			if (preDayTmp == null) {
				preDayTmp = new CoordinationPair[2];
			}
			preDayTmp[0] = new CoordinationPair(centerX, maxCenterY);
			preDayTmp[1] = new CoordinationPair(centerX, minCenterY);

			drawTexts(canvas, centerX, maxCenterY, normalTxtSize, circleRadius, dailyWeather.maxTmp, true);
			drawTexts(canvas, centerX, minCenterY, normalTxtSize, circleRadius, dailyWeather.minTmp, false);
		}
	}

	private void drawCircles(Canvas canvas, float centerX, float centerY, float radius) {
		paint.setColor(getResources().getColor(R.color.light_green_700));
		paint.setStrokeWidth(0);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(centerX, centerY, radius, paint);

		paint.setColor(getResources().getColor(R.color.circle));
		paint.setStrokeWidth(radius);
		paint.setStyle(Paint.Style.STROKE);
		float strokeRadius = radius * 2f - paint.getStrokeWidth() / 2;
		canvas.drawCircle(centerX, centerY, strokeRadius, paint);
	}

	private void drawLines(Canvas canvas, float centerX, float centerY, CoordinationPair preTmp) {
		paint.setColor(getResources().getColor(R.color.light_green_700));
		paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.line_width));
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawLine(preTmp.first, preTmp.second, centerX, centerY, paint);
	}

	private void drawTexts(Canvas canvas, float centerX, float centerY, float textSize, float circleRadius, String tmp, boolean drawMaxTmp) {
		paint.setStrokeWidth(0);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(getResources().getColor(R.color.light_green_700));
		if (drawMaxTmp) {
			canvas.drawText(OtherUtils.getString(tmp) + "°", centerX, centerY - textSize / 2, paint);
		} else {
			canvas.drawText(OtherUtils.getString(tmp) + "°", centerX, centerY + circleRadius * 2 + textSize, paint);
		}
	}

	public void setData(List<DailyWeatherBean> dailyWeathers) {
		minTemperature = MAX_INT;
		maxTemperature = -MAX_INT;
		this.dailyWeathers = dailyWeathers;
		for (DailyWeatherBean dailyWeather : dailyWeathers) {
			if (Integer.valueOf(dailyWeather.minTmp) < minTemperature) {
				minTemperature = Integer.valueOf(dailyWeather.minTmp);
			}

			if (Integer.valueOf(dailyWeather.maxTmp) > maxTemperature) {
				maxTemperature = Integer.valueOf(dailyWeather.maxTmp);
			}
		}

		invalidate();
	}

	public void clearData() {
		dailyWeathers = null;
		invalidate();
	}

	private final class CoordinationPair extends Pair<Float, Float> {

		/**
		 * Constructor for a Pair.
		 *
		 * @param first  the first object in the Pair
		 * @param second the second object in the pair
		 */
		public CoordinationPair(Float first, Float second) {
			super(first, second);
		}
	}
}
