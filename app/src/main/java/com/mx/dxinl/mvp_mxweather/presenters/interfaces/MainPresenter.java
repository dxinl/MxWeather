package com.mx.dxinl.mvp_mxweather.presenters.interfaces;

import android.view.MenuItem;

import com.mx.dxinl.mvp_mxweather.model.bean.CityInfo;

import java.util.List;

/**
 * Created by DengXinliang on 2016/3/13.
 */
public interface MainPresenter {
	boolean processDB();

	void getCurrentCityInfo();

	void initNavigationMenu();

	void onNavigationItemSelected(MenuItem item);

	String getCurrentCityNum();

	String getCurrentCityName();

	String getCurrentCityType();

	void setCurrentCity(String cityName, String cityNum, String cityType);

	List<CityInfo> getCitiesInfo(boolean isNeedUpdate);
}
