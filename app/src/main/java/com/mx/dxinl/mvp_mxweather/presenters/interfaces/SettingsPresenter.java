package com.mx.dxinl.mvp_mxweather.presenters.interfaces;

/**
 * Created by dxinl on 2016/4/3.
 */
public interface SettingsPresenter {
	void setUpdateWidgetInterval(long interval);

	long getUpdateWidgetInterval();

	void setShowBessel(boolean isShow);

	boolean getShowBessel();
}
