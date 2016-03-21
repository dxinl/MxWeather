package com.mx.dxinl.mvp_mxweather.vus.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mx.dxinl.mvp_mxweather.model.bean.DailyWeatherBean;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.WeatherPresenter;

import java.util.List;

/**
 * Created by DengXinliang on 2016/3/17.
 */
public class WeatherIconsFragment extends Fragment {
	private WeatherPresenter presenter;
	private List<DailyWeatherBean> dailyWeathers;
	private boolean showNightWeather;

	public static WeatherIconsFragment newInstance(WeatherPresenter presenter, List<DailyWeatherBean> dailyWeathers) {
		return newInstance(presenter, dailyWeathers, false);
	}

	public static WeatherIconsFragment newInstance(WeatherPresenter presenter,
	                                               List<DailyWeatherBean> dailyWeathers, boolean showNightWeather) {
		Bundle args = new Bundle();
		WeatherIconsFragment fragment = new WeatherIconsFragment();
		fragment.setArguments(args);
		fragment.presenter = presenter;
		fragment.dailyWeathers = dailyWeathers;
		fragment.showNightWeather = showNightWeather;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout iconsLayout = new LinearLayout(getContext());
		iconsLayout.setOrientation(LinearLayout.HORIZONTAL);
		return iconsLayout;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (dailyWeathers == null) {
			return;
		}

		((LinearLayout) view).setWeightSum(dailyWeathers.size());
		for (DailyWeatherBean dailyWeather : dailyWeathers) {
			ImageView imageView = new ImageView(getContext());
			imageView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
			if (!showNightWeather) {
				presenter.setImageBitmap(imageView, dailyWeather.code_d);
			} else {
				presenter.setImageBitmap(imageView, dailyWeather.code_n);
			}
			((LinearLayout) view).addView(imageView);
		}
	}
}
