package com.mx.dxinl.mvp_mxweather.presenters.impl;

import android.content.Context;
import android.util.Pair;
import android.view.MenuItem;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.model.SharedPreferencesHelper;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.CitiesManagerPresenter;
import com.mx.dxinl.mvp_mxweather.vus.interfaces.ICitiesManagerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DengXinliang on 2016/3/14.
 */
public class CitiesManagerPresenterImpl implements CitiesManagerPresenter {
	private List<Pair<String, String>> citiesListData;
	private List<Pair<String, String>> selectedCities = new ArrayList<>();
	private ICitiesManagerView view;
	private SharedPreferencesHelper spHelper;

	public CitiesManagerPresenterImpl(Context context, ICitiesManagerView view) {
		this.view = view;
		spHelper = new SharedPreferencesHelper(context);
	}

	@Override
	public List<Pair<String, String>> getCitiesListData(boolean isNeedUpdate) {
		if (citiesListData == null || isNeedUpdate) {
			citiesListData = spHelper.getCitiesInfo();
		}
		return citiesListData;
	}

	@Override
	public List<Pair<String, String>> getSelectedCities() {
		return selectedCities;
	}

	@Override
	public void onClickCity(int position) {
		Pair<String, String> cityInfo = citiesListData.get(position);
		if (selectedCities.contains(cityInfo)) {
			selectedCities.remove(cityInfo);
		} else {
			selectedCities.add(cityInfo);
		}

		view.notifyDataSetChanged();
	}

	@Override
	public void onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.remove_cities:
				citiesListData.removeAll(selectedCities);
				selectedCities.clear();
				spHelper.setChosenCities(citiesListData);
				view.notifyDataSetChanged();
				break;
		}
	}
}