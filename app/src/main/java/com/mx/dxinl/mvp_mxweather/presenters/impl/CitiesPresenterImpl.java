package com.mx.dxinl.mvp_mxweather.presenters.impl;

import com.mx.dxinl.mvp_mxweather.model.DBQueryHelper;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.CitiesPresenter;

import java.util.List;

/**
 * Created by DengXinliang on 2016/3/13.
 */
public class CitiesPresenterImpl implements CitiesPresenter {
	@Override
	public List<String> getCitiesList(String str, String type) {
		return DBQueryHelper.queryCities(str, type);
	}

	@Override
	public String getCurrentCityNum(String city) {
		return DBQueryHelper.queryCityNum(city);
	}
}
