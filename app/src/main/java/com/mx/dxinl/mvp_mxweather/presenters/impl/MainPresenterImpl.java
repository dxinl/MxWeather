package com.mx.dxinl.mvp_mxweather.presenters.impl;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.model.SharedPreferencesHelper;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.MainPresenter;
import com.mx.dxinl.mvp_mxweather.utils.AssetsDatabaseHelper;
import com.mx.dxinl.mvp_mxweather.vus.fragment.CitiesManagerFragment;
import com.mx.dxinl.mvp_mxweather.vus.fragment.CitiesTabFragment;
import com.mx.dxinl.mvp_mxweather.vus.interfaces.IMainView;

import java.util.List;

/**
 * Created by DengXinliang on 2016/3/13.
 */
public class MainPresenterImpl implements MainPresenter {
	private final int BASE_ITEM_ID = 0x0408;

	private String currentCityNum;
	private String currentCityName;
	private List<Pair<String, String>> citiesInfo;

	private SharedPreferencesHelper spHelper;

	private IMainView view;
	private Context context;

	public MainPresenterImpl(Context context, IMainView view) {
		this.context = context;
		this.view = view;
		spHelper = new SharedPreferencesHelper(context);
	}

	private void setCurrentCityInfo(Pair<String, String> cityInfo) {
		setCurrentCityInfo(cityInfo.first, cityInfo.second);
	}

	private void setCurrentCityInfo(String currentCityName, String currentCityNum) {
		this.currentCityNum = currentCityNum;
		this.currentCityName = currentCityName;

		spHelper.setCurrentCityInfo(currentCityName, currentCityNum);
	}

	@Override
	public boolean processDB() {
		return AssetsDatabaseHelper.openDatabase(context);
	}

	@Override
	public void getCurrentCityInfo() {
		String[] nameOrNum = spHelper.getCurrentCityInfo();
		if (nameOrNum == null) {
			setCurrentCity("北京", "CN101010100");
		} else {
			this.currentCityName = nameOrNum[0];
			this.currentCityNum = nameOrNum[1];
		}
	}

	@Override
	public String getCurrentCityNum() {
		return currentCityNum;
	}

	@Override
	public String getCurrentCityName() {
		return currentCityName;
	}

	@Override
	public void setCurrentCity(String cityName, String cityNum) {
		spHelper.addChosenCity(cityName, cityNum);
		setCurrentCityInfo(cityName, cityNum);
		initNavigationMenu();
		clearFragmentBackStack();
	}

	@Override
	public List<Pair<String, String>> getCitiesInfo(boolean isNeedUpdate) {
		if (citiesInfo == null || isNeedUpdate) {
			citiesInfo = spHelper.getCitiesInfo();
		}
		return citiesInfo;
	}

	@Override
	public void initNavigationMenu() {
		Menu menu = view.getNavigationMenu();
		menu.removeGroup(BASE_ITEM_ID);

		List<Pair<String, String>> citiesInfo = getCitiesInfo(true);
		int index = 1;
		MenuItem checkedItem = null;
		for (Pair<String, String> cityInfo : citiesInfo) {
			menu.add(BASE_ITEM_ID, BASE_ITEM_ID + index, 1, cityInfo.first);
			MenuItem item = menu.findItem(BASE_ITEM_ID + index);
			if (item.getTitle().equals(getCurrentCityName())) {
				checkedItem = item;
			}
			index++;
		}

		menu.setGroupCheckable(BASE_ITEM_ID, true, true);
		if (checkedItem != null) {
			checkedItem.setChecked(true);
		} else {
			menu.findItem(BASE_ITEM_ID + 1).setChecked(true);
			setCurrentCityInfo(citiesInfo.get(0));
		}
	}

	@Override
	public void onNavigationItemSelected(MenuItem item) {
		if (item.getGroupId() == BASE_ITEM_ID) {
			clearFragmentBackStack();
			int id = item.getItemId();
			int index = id - BASE_ITEM_ID - 1;

			Pair<String, String> currentCityInfo = getCitiesInfo(false).get(index);
			setCurrentCityInfo(currentCityInfo.first, currentCityInfo.second);
			view.refreshFragment();

			return;
		}

		FragmentManager fm = view.getIViewFragmentManager();
		int id = item.getItemId();
		boolean isFoundFragment = false;
		view.cancelGetWeatherTask();
		switch (id) {
			case R.id.add_city:
				for (int i = fm.getBackStackEntryCount() - 1; i >= 0; i--) {
					if (fm.getBackStackEntryAt(i).getName().equals("addCity")) {
						fm.popBackStackImmediate("addCity", 0);
						isFoundFragment = true;
						break;
					}
				}

				if (!isFoundFragment) {
					CitiesTabFragment citiesTabFragment = new CitiesTabFragment();
					FragmentTransaction transaction = view.getIViewFragmentManager().beginTransaction();
					transaction.replace(R.id.content_panel, citiesTabFragment);
					transaction.addToBackStack("addCity");
					transaction.commit();
				}
				break;

			case R.id.manage_city:
				for (int i = fm.getBackStackEntryCount() - 1; i >= 0; i--) {
					if (fm.getBackStackEntryAt(i).getName().equals("manageCity")) {
						fm.popBackStackImmediate("manageCity", 0);
						isFoundFragment = true;
						break;
					}
				}

				if (!isFoundFragment) {
					CitiesManagerFragment citiesManagerFragment = new CitiesManagerFragment();
					FragmentTransaction transaction = view.getIViewFragmentManager().beginTransaction();
					transaction.replace(R.id.content_panel, citiesManagerFragment);
					transaction.addToBackStack("manageCity");
					transaction.commit();
				}
				break;

			case R.id.setting:
				break;
		}
	}

	private void clearFragmentBackStack() {
		FragmentManager fm = view.getIViewFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStackImmediate(fm.getBackStackEntryAt(0).getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
	}
}
