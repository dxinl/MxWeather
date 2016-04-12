package com.mx.dxinl.mvp_mxweather.vus.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.presenters.impl.SettingsPresenterImpl;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.SettingsPresenter;
import com.mx.dxinl.mvp_mxweather.utils.OtherUtils;
import com.mx.dxinl.mvp_mxweather.vus.MainActivity;
import com.mx.dxinl.mvp_mxweather.vus.interfaces.ISettingsView;
import com.mx.dxinl.mvp_mxweather.vus.widget.Widget2_1;

/**
 * Created by DengXinliang on 2016/3/31.
 */
public class SettingsFragment extends Fragment implements ISettingsView {
	private SettingsPresenter presenter;
	private Dialog choseIntervalDialog;
	private TextView intervalDesc;

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

		final View intervalLayout = view.findViewById(R.id.interval);
		intervalLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});

		long interval = presenter.getUpdateWidgetInterval();
		String intervalStr;
		if (OtherUtils.isDebug()) {
			switch (String.valueOf(interval)) {
				case "1000":
					intervalStr = getString(R.string.thirty_minute);
					break;
				case "2000":
					intervalStr = getString(R.string.an_hour);
					break;
				case "4000":
					intervalStr = getString(R.string.four_hour);
					break;
				case "5000":
					intervalStr = getString(R.string.six_hour);
					break;
				case "6000":
					intervalStr = getString(R.string.twelve_hour);
					break;
				case "7000":
					intervalStr = getString(R.string.a_day);
					break;
				case "3000":
				default:
					intervalStr = getString(R.string.two_hour);
					break;
			}
		} else {
			switch (String.valueOf(interval)) {
				case "1800000":
					intervalStr = getString(R.string.thirty_minute);
					break;
				case "3600000":
					intervalStr = getString(R.string.an_hour);
					break;
				case "14400000":
					intervalStr = getString(R.string.four_hour);
					break;
				case "21600000":
					intervalStr = getString(R.string.six_hour);
					break;
				case "43200000":
					intervalStr = getString(R.string.twelve_hour);
					break;
				case "86400000":
					intervalStr = getString(R.string.a_day);
					break;
				case "7200000":
				default:
					intervalStr = getString(R.string.two_hour);
					break;
			}
		}
		intervalDesc = (TextView) view.findViewById(R.id.interval_desc);
		intervalDesc.setText(intervalStr);

		final TextView showBesselDesc = (TextView) view.findViewById(R.id.show_bessel_desc);
		SwitchCompat toggleShowBessel = (SwitchCompat) view.findViewById(R.id.toggle_show_bessel);
		toggleShowBessel.setChecked(presenter.getShowBessel());
		toggleShowBessel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				((MainActivity) getActivity()).setShowBessel(isChecked);
				presenter.setShowBessel(isChecked);
				if (isChecked) {
					showBesselDesc.setText(getString(R.string.show_bessel));
				} else {
					showBesselDesc.setText(getString(R.string.show_line));
				}
			}
		});
	}

	private void showDialog() {
		if (choseIntervalDialog == null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				choseIntervalDialog = new Dialog(getActivity(), android.R.style.Theme_Material_Light_Dialog);
			} else {
				choseIntervalDialog = new Dialog(getActivity());
			}
			choseIntervalDialog.setTitle(getString(R.string.chose_interval));
			choseIntervalDialog.setContentView(R.layout.dialog_chose_interval);
		}

		RadioGroup choseInterval = (RadioGroup) choseIntervalDialog.findViewById(R.id.chose_interval);
		choseInterval.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				String intervalStr;
				switch (checkedId) {
					case R.id.thirty_minute:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(1000);
						} else {
							presenter.setUpdateWidgetInterval(30 * 60 * 1000);
						}
						intervalStr = getString(R.string.thirty_minute);
						break;

					case R.id.an_hour:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(2000);
						} else {
							presenter.setUpdateWidgetInterval(60 * 60 * 1000);
						}
						intervalStr = getString(R.string.an_hour);
						break;

					case R.id.four_hour:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(4000);
						} else {
							presenter.setUpdateWidgetInterval(240 * 60 * 1000);
						}
						intervalStr = getString(R.string.four_hour);
						break;

					case R.id.six_hour:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(5000);
						} else {
							presenter.setUpdateWidgetInterval(360 * 60 * 1000);
						}
						intervalStr = getString(R.string.six_hour);
						break;

					case R.id.twelve_hour:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(6000);
						} else {
							presenter.setUpdateWidgetInterval(720 * 60 * 1000);
						}
						intervalStr = getString(R.string.twelve_hour);
						break;

					case R.id.a_day:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(7000);
						} else {
							presenter.setUpdateWidgetInterval(1440 * 60 * 1000);
						}
						intervalStr = getString(R.string.a_day);
						break;

					case R.id.two_hour:
					default:
						if (OtherUtils.isDebug()) {
							presenter.setUpdateWidgetInterval(3000);
						} else {
							presenter.setUpdateWidgetInterval(120 * 60 * 1000);
						}
						intervalStr = getString(R.string.two_hour);
						break;
				}
				intervalDesc.setText(intervalStr);
				choseIntervalDialog.dismiss();
				Intent intent = new Intent();
				intent.setAction(Widget2_1.MODIFY_UPDATE_INTERVAL);
				getActivity().sendBroadcast(intent);
			}
		});

		long interval = presenter.getUpdateWidgetInterval();
		RadioButton checkedBtn;
		if (OtherUtils.isDebug()) {
			switch (String.valueOf(interval)) {
				case "1000":
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.thirty_minute);
					break;
				case "2000":
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.an_hour);
					break;
				case "4000":
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.four_hour);
					break;
				case "5000":
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.six_hour);
					break;
				case "6000":
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.twelve_hour);
					break;
				case "7000":
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.a_day);
					break;
				case "3000":
				default:
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.two_hour);
					break;
			}
		} else {
			switch (String.valueOf(interval)) {
				case "1800000":
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.thirty_minute);
					break;
				case "3600000":
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.an_hour);
					break;
				case "14400000":
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.four_hour);
					break;
				case "21600000":
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.six_hour);
					break;
				case "43200000":
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.twelve_hour);
					break;
				case "86400000":
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.a_day);
					break;
				case "7200000":
				default:
					checkedBtn = (RadioButton) choseIntervalDialog.findViewById(R.id.two_hour);
					break;
			}
		}
		checkedBtn.setChecked(true);

		choseIntervalDialog.show();
	}

	@Override
	public Context getIViewContext() {
		return getActivity().getApplicationContext();
	}
}