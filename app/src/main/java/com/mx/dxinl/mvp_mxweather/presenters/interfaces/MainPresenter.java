package com.mx.dxinl.mvp_mxweather.presenters.interfaces;

import android.util.Pair;
import android.view.MenuItem;

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

	void setCurrentCity(String cityName, String cityNum);

	List<Pair<String, String>> getCitiesInfo(boolean isNeedUpdate);
}
