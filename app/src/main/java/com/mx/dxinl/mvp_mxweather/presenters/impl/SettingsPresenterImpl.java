package com.mx.dxinl.mvp_mxweather.presenters.impl;

import com.mx.dxinl.mvp_mxweather.model.SharedPreferencesHelper;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.SettingsPresenter;
import com.mx.dxinl.mvp_mxweather.vus.interfaces.ISettingsView;

/**
 * Created by dxinl on 2016/4/3.
 */
public class SettingsPresenterImpl implements SettingsPresenter {
	private ISettingsView view;
	private SharedPreferencesHelper spHelper;

	public SettingsPresenterImpl(ISettingsView view) {
		this.view = view;
		spHelper = new SharedPreferencesHelper(view.getIViewContext());
	}

	@Override
	public void setUpdateWidgetInterval(long interval) {
		spHelper.setUpdateWidgetInterval(interval);
	}
}
