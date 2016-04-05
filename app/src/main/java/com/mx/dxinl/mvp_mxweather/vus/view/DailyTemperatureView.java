package com.mx.dxinl.mvp_mxweather.vus.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.model.bean.DailyWeatherBean;
import com.mx.dxinl.mvp_mxweather.utils.OtherUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DengXinliang on 2016/3/15.
 */
public class DailyTemperatureView extends View {
	private final int MAX_INT = 999999;

	private List<DailyWeatherBean> dailyWeathers;

	private CoordinationPair preDayTmp[];
	private List<CoordinationPair> minControlPoints = new ArrayList<>();
	private List<CoordinationPair> maxControlPoints = new ArrayList<>();

	private int maxTemperature = -MAX_INT;
	private int minTemperature = MAX_INT;
	private Paint paint = new Paint();
	private Path path = new Path();
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
	@SuppressWarnings("DrawAllocation")
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
		paint.setDither(true);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextSize(normalTxtSize);
		paint.setStrokeWidth(0);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(getResources().getColor(R.color.light_green_700));

		for (int i = 0; i < dailyWeathers.size(); i++) {
			DailyWeatherBean dailyWeather = dailyWeathers.get(i);

			float centerX = widthUnit * i + widthUnit / 2f;
			canvas.drawText(dailyWeather.date, centerX, normalTxtSize * 1.5f, paint);

			float maxCenterY = calcCenterY(dailyWeather.maxTmp, circleTop, heightUnit);
			drawCircles(canvas, centerX, maxCenterY, circleRadius);

			float minCenterY = calcCenterY(dailyWeather.minTmp, circleTop, heightUnit);
			drawCircles(canvas, centerX, minCenterY, circleRadius);

			if (!OtherUtils.isBessel() && i > 0) {
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

		if (OtherUtils.isBessel()) {
			minControlPoints.clear();
			maxControlPoints.clear();

			paint.setTextAlign(Paint.Align.CENTER);
			paint.setColor(getResources().getColor(R.color.light_green_700));
			paint.setStrokeWidth(getResources().getDimension(R.dimen.line_width));
			paint.setStyle(Paint.Style.STROKE);
			path.reset();

			// calculate center point of every two temperature point
			for (int i = 1; i < dailyWeathers.size() - 1; i++) {
				DailyWeatherBean preDailyWeather = dailyWeathers.get(i - 1);
				DailyWeatherBean curDailyWeather = dailyWeathers.get(i);
				DailyWeatherBean nextDailyWeather = dailyWeathers.get(i + 1);

				CoordinationPair minPrePoint = new CoordinationPair(
						widthUnit * (i - 1) + widthUnit / 2f, calcCenterY(preDailyWeather.minTmp, circleTop, heightUnit));
				CoordinationPair maxPrePoint = new CoordinationPair(
						widthUnit * (i - 1) + widthUnit / 2f, calcCenterY(preDailyWeather.maxTmp, circleTop, heightUnit));
				CoordinationPair minCurPoint = new CoordinationPair(
						widthUnit * (i) + widthUnit / 2f, calcCenterY(curDailyWeather.minTmp, circleTop, heightUnit));
				CoordinationPair maxCurPoint = new CoordinationPair(
						widthUnit * (i) + widthUnit / 2f, calcCenterY(curDailyWeather.maxTmp, circleTop, heightUnit));
				CoordinationPair minNextPoint = new CoordinationPair(
						widthUnit * (i + 1) + widthUnit / 2f, calcCenterY(nextDailyWeather.minTmp, circleTop, heightUnit));
				CoordinationPair maxNextPoint = new CoordinationPair(
						widthUnit * (i + 1) + widthUnit / 2f, calcCenterY(nextDailyWeather.maxTmp, circleTop, heightUnit));

				CoordinationPair minPreCurCenterPoint = calcCenterPoint(minPrePoint, minCurPoint);
				CoordinationPair maxPreCurCenterPoint = calcCenterPoint(maxPrePoint, maxCurPoint);
				CoordinationPair minCurNextCenterPoint = calcCenterPoint(minCurPoint, minNextPoint);
				CoordinationPair maxCurNextCenterPoint = calcCenterPoint(maxCurPoint, maxNextPoint);

				CoordinationPair minCenterPoint = calcCenterPoint(minPreCurCenterPoint, minCurNextCenterPoint);
				CoordinationPair maxCenterPoint = calcCenterPoint(maxPreCurCenterPoint, maxCurNextCenterPoint);

				CoordinationPair minControlPoint1 = calcControlPoint(minPreCurCenterPoint, minCenterPoint, minCurPoint);
				CoordinationPair minControlPoint2 = calcControlPoint(minCurNextCenterPoint, minCenterPoint, minCurPoint);
				minControlPoints.add(minControlPoint1);
				minControlPoints.add(minControlPoint2);

				CoordinationPair maxControlPoint1 = calcControlPoint(maxPreCurCenterPoint, maxCenterPoint, maxCurPoint);
				CoordinationPair maxControlPoint2 = calcControlPoint(maxCurNextCenterPoint, maxCenterPoint, maxCurPoint);
				maxControlPoints.add(maxControlPoint1);
				maxControlPoints.add(maxControlPoint2);
			}

			for (int i = 1; i < dailyWeathers.size(); i++) {
				DailyWeatherBean preDailyWeather = dailyWeathers.get(i - 1);
				DailyWeatherBean curDailyWeather = dailyWeathers.get(i);

				CoordinationPair minPrePoint = new CoordinationPair(
						widthUnit * (i - 1) + widthUnit / 2f, calcCenterY(preDailyWeather.minTmp, circleTop, heightUnit));
				CoordinationPair maxPrePoint = new CoordinationPair(
						widthUnit * (i - 1) + widthUnit / 2f, calcCenterY(preDailyWeather.maxTmp, circleTop, heightUnit));
				CoordinationPair minCurPoint = new CoordinationPair(
						widthUnit * (i) + widthUnit / 2f, calcCenterY(curDailyWeather.minTmp, circleTop, heightUnit));
				CoordinationPair maxCurPoint = new CoordinationPair(
						widthUnit * (i) + widthUnit / 2f, calcCenterY(curDailyWeather.maxTmp, circleTop, heightUnit));

				int index = (i - 1) * 2;
				if (i == 1 || i == dailyWeathers.size() - 1) {
					if (i == dailyWeathers.size() - 1) {
						index--;
					}
					drawQuadPath(canvas, path, minPrePoint, minControlPoints.get(index), minCurPoint);
					drawQuadPath(canvas, path, maxPrePoint, maxControlPoints.get(index), maxCurPoint);
				} else {
					drawCubicPath(canvas, path, minPrePoint,
							minControlPoints.get(index - 1), minControlPoints.get(index), minCurPoint);
					drawCubicPath(canvas, path, maxPrePoint,
							maxControlPoints.get(index - 1), maxControlPoints.get(index), maxCurPoint);
				}
				canvas.drawPath(path, paint);
			}
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

	private float calcCenterY(String tmp, float circleTop, float heightUnit) {
		int maxUnitIndex = maxTemperature - Integer.valueOf(tmp);
		return circleTop + maxUnitIndex * heightUnit + heightUnit / 2f;
	}

	private CoordinationPair calcCenterPoint(CoordinationPair point1, CoordinationPair point2) {
		return new CoordinationPair((point1.first + point2.first) / 2f, (point1.second + point2.second) / 2f);
	}

	private void drawQuadPath(Canvas canvas, Path path,
	                          CoordinationPair startPoint, CoordinationPair controlPoint, CoordinationPair endPoint) {
		path.moveTo(startPoint.first, startPoint.second);
		path.quadTo(controlPoint.first, controlPoint.second, endPoint.first, endPoint.second);
		canvas.drawPath(path, paint);
	}

	private void drawCubicPath(Canvas canvas, Path path, CoordinationPair startPoint,
	                           CoordinationPair controlPoint1, CoordinationPair controlPoint2, CoordinationPair endPoint) {
		path.moveTo(startPoint.first, startPoint.second);
		path.cubicTo(controlPoint1.first, controlPoint1.second,
				controlPoint2.first, controlPoint2.second, endPoint.first, endPoint.second);
		canvas.drawPath(path, paint);
	}

	private CoordinationPair calcControlPoint(
			CoordinationPair translatingPoint, CoordinationPair normalPoint, CoordinationPair translatedPoint) {
		float first = translatingPoint.first * translatedPoint.first / normalPoint.first;
		float second = translatingPoint.second * translatedPoint.second / normalPoint.second;
		return new CoordinationPair(first, second);
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

	private final class CoordinationPair {
		public float first;
		public float second;

		public CoordinationPair(Float first, Float second) {
			this.first = first;
			this.second = second;
		}
	}
}
