package com.mx.dxinl.mvp_mxweather.presenters.interfaces;

import java.util.List;

/**
 * Created by DengXinliang on 2016/3/13.
 */
public interface CitiesPresenter {
	List<String> getCitiesList(String str, String type);

	String getCurrentCityNum(String city);
}
