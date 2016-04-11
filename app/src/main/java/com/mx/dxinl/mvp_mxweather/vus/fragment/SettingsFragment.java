package com.mx.dxinl.mvp_mxweather.vus.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.model.SharedPreferencesHelper;
import com.mx.dxinl.mvp_mxweather.presenters.impl.SettingsPresenterImpl;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.SettingsPresenter;
import com.mx.dxinl.mvp_mxweather.utils.OtherUtils;
import com.mx.dxinl.mvp_mxweather.vus.MainActivity;
import com.mx.dxinl.mvp_mxweather.vus.interfaces.ISettingsView;
import com.mx.dxinl.mvp_mxweather.vus.widget.Widget2_1;

import java.util.EnumMap;
import java.util.HashMap;

/**
 * Created by DengXinliang on 2016/3/31.
 */
public class SettingsFragment extends Fragment implements ISettingsView {
	private SettingsPresenter presenter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(getString(R.string.setting));
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_settings, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		presenter = new SettingsPresenterImpl(this);

		RadioGroup intervals = (RadioGroup) view.findViewById(R.id.chose_interval);
		intervals.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.thirty_minute:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(1000);
						} else {
							presenter.setUpdateWidgetInterval(30 * 60 * 1000);
						}
						break;

					case R.id.an_hour:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(2000);
						} else {
							presenter.setUpdateWidgetInterval(60 * 60 * 1000);
						}
						break;

					case R.id.two_hour:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(3000);
						} else {
							presenter.setUpdateWidgetInterval(120 * 60 * 1000);
						}
						break;

					case R.id.four_hour:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(4000);
						} else {
							presenter.setUpdateWidgetInterval(240 * 60 * 1000);
						}
						break;

					case R.id.six_hour:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(5000);
						} else {
							presenter.setUpdateWidgetInterval(360 * 60 * 1000);
						}
						break;

					case R.id.twelve_hour:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(6000);
						} else {
							presenter.setUpdateWidgetInterval(720 * 60 * 1000);
						}
						break;

					case R.id.a_day:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(7000);
						} else {
							presenter.setUpdateWidgetInterval(1440 * 60 * 1000);
						}
						break;
				}
				Intent intent = new Intent();
				intent.setAction(Widget2_1.MODIFY_UPDATE_INTERVAL);
				getActivity().sendBroadcast(intent);
			}
		});

		long interval = presenter.getUpdateWidgetInterval();
		if (OtherUtils.isDebug()) {
			if (interval == 1000) {
				((RadioButton) view.findViewById(R.id.thirty_minute)).setChecked(true);
			} else if (interval == 2000) {
				((RadioButton) view.findViewById(R.id.an_hour)).setChecked(true);
			} else if (interval == 3000) {
				((RadioButton) view.findViewById(R.id.two_hour)).setChecked(true);
			} else if (interval == 4000) {
				((RadioButton) view.findViewById(R.id.four_hour)).setChecked(true);
			} else if (interval == 5000) {
				((RadioButton) view.findViewById(R.id.six_hour)).setChecked(true);
			} else if (interval == 6000) {
				((RadioButton) view.findViewById(R.id.twelve_hour)).setChecked(true);
			} else if (interval == 7000) {
				((RadioButton) view.findViewById(R.id.a_day)).setChecked(true);
			} else {
				((RadioButton) view.findViewById(R.id.two_hour)).setChecked(true);
			}
		} else {
			if (interval == 30 * 60 * 1000) {
				((RadioButton) view.findViewById(R.id.thirty_minute)).setChecked(true);
			} else if (interval == 60 * 60 * 1000) {
				((RadioButton) view.findViewById(R.id.an_hour)).setChecked(true);
			} else if (interval == 2 * 60 * 60 * 1000) {
				((RadioButton) view.findViewById(R.id.two_hour)).setChecked(true);
			} else if (interval == 4 * 60 * 60 * 1000) {
				((RadioButton) view.findViewById(R.id.four_hour)).setChecked(true);
			} else if (interval == 6 * 60 * 60 * 1000) {
				((RadioButton) view.findViewById(R.id.six_hour)).setChecked(true);
			} else if (interval == 12 * 60 * 60 * 1000) {
				((RadioButton) view.findViewById(R.id.twelve_hour)).setChecked(true);
			} else if (interval == 24 * 60 * 60 * 1000) {
				((RadioButton) view.findViewById(R.id.a_day)).setChecked(true);
			} else {
				((RadioButton) view.findViewById(R.id.two_hour)).setChecked(true);
			}
		}

		CheckBox showBessel = (CheckBox) view.findViewById(R.id.check_show_bessel);
		showBessel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				((MainActivity) getActivity()).setShowBessel(isChecked);
				presenter.setShowBessel(isChecked);
			}
		});

		if (presenter.getShowBessel()) {
			showBessel.setChecked(true);
		} else {
			showBessel.setChecked(false);
		}
	}

	@Override
	public Context getIViewContext() {
		return getActivity().getApplicationContext();
	}
}