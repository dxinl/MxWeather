package com.mx.dxinl.mvp_mxweather.presenters.interfaces;

import android.view.MenuItem;

import com.mx.dxinl.mvp_mxweather.model.bean.CityInfo;

import java.util.List;

/**
 * Created by DengXinliang on 2016/3/14.
 */
public interface CitiesManagerPresenter {
	List<CityInfo> getCitiesListData(boolean isNeedUpdate);

	List<CityInfo> getSelectedCities();

	void onClickCity(int position);

	void onOptionsItemSelected(MenuItem item);
}
