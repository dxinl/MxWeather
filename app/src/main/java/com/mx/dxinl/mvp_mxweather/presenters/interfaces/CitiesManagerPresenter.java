package com.mx.dxinl.mvp_mxweather.presenters.interfaces;

import android.util.Pair;
import android.view.MenuItem;

import java.util.List;

/**
 * Created by DengXinliang on 2016/3/14.
 */
public interface CitiesManagerPresenter {
	List<Pair<String, String>> getCitiesListData(boolean isNeedUpdate);

	List<Pair<String, String>> getSelectedCities();

	void onClickCity(int position);

	void onOptionsItemSelected(MenuItem item);
}
