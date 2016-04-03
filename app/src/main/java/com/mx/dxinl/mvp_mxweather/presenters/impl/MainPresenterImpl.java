package com.mx.dxinl.mvp_mxweather.presenters.impl;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.model.SharedPreferencesHelper;
import com.mx.dxinl.mvp_mxweather.model.bean.CityInfo;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.MainPresenter;
import com.mx.dxinl.mvp_mxweather.utils.AssetsDatabaseHelper;
import com.mx.dxinl.mvp_mxweather.utils.ImageLoader;
import com.mx.dxinl.mvp_mxweather.vus.fragment.CitiesManagerFragment;
import com.mx.dxinl.mvp_mxweather.vus.fragment.CitiesTabFragment;
import com.mx.dxinl.mvp_mxweather.vus.fragment.SettingsFragment;
import com.mx.dxinl.mvp_mxweather.vus.interfaces.IMainView;

import java.util.List;

/**
 * Created by DengXinliang on 2016/3/13.
 */
public class MainPresenterImpl implements MainPresenter {
	private final int BASE_ITEM_ID = 0x0408;

	private String currentCityNum;
	private String currentCityName;
	private String currentCityType;
	private List<CityInfo> citiesInfo;

	private SharedPreferencesHelper spHelper;

	private IMainView view;
	private Context context;

	public MainPresenterImpl(IMainView view) {
		this.view = view;
		this.context = view.getIViewContext();
		spHelper = new SharedPreferencesHelper(context);
	}

	private void setCurrentCityType(CityInfo cityInfo) {
		setCurrentCityInfo(cityInfo.name, cityInfo.num, cityInfo.type);
	}

	private void setCurrentCityInfo(String currentCityName, String currentCityNum, String cityType) {
		this.currentCityNum = currentCityNum;
		this.currentCityName = currentCityName;
		this.currentCityType = cityType;

		spHelper.setCurrentCityInfo(currentCityName, currentCityNum, cityType);
	}

	@Override
	public boolean processDB() {
		return AssetsDatabaseHelper.openDatabase(context);
	}

	@Override
	public void getCurrentCityInfo() {
		String[] currentCityInfo = spHelper.getCurrentCityInfo();
		if (currentCityInfo == null) {
			setCurrentCity("北京", "CN101010100", "weather");
		} else {
			this.currentCityName = currentCityInfo[0];
			this.currentCityNum = currentCityInfo[1];
			this.currentCityType = currentCityInfo[2];
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
	public String getCurrentCityType() {
		return currentCityType;
	}

	@Override
	public void setCurrentCity(String cityName, String cityNum, String cityType) {
		view.hideSoftInputMethod();
		spHelper.addChosenCity(cityName, cityNum, cityType);
		setCurrentCityInfo(cityName, cityNum, cityType);
		initNavigationMenu();
		view.refreshFragment();
	}

	@Override
	public List<CityInfo> getCitiesInfo(boolean isNeedUpdate) {
		if (citiesInfo == null || isNeedUpdate) {
			citiesInfo = spHelper.getCitiesInfo();
		}
		return citiesInfo;
	}

	@Override
	public void setImageBitmap(ImageView view, String code) {
		ImageLoader.get().setImageBitmap(view, code, true);
	}

	@Override
	public void initNavigationMenu() {
		Menu menu = view.getNavigationMenu();
		menu.removeGroup(BASE_ITEM_ID);

		List<CityInfo> citiesInfo = getCitiesInfo(true);
		int index = 1;
		MenuItem checkedItem = null;
		for (CityInfo cityInfo : citiesInfo) {
			menu.add(BASE_ITEM_ID, BASE_ITEM_ID + index, 1, cityInfo.name);
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
			setCurrentCityType(citiesInfo.get(0));
		}
	}

	@Override
	public void onNavigationItemSelected(MenuItem item) {
		if (item.getGroupId() == BASE_ITEM_ID) {
			clearFragmentBackStack();
			int id = item.getItemId();
			int index = id - BASE_ITEM_ID - 1;

			CityInfo currentCityInfo = getCitiesInfo(false).get(index);
			setCurrentCityInfo(currentCityInfo.name, currentCityInfo.num, currentCityInfo.type);
			view.refreshFragment();

			return;
		}

		FragmentManager fm = view.getIViewFragmentManager();
		int id = item.getItemId();
		boolean isFoundFragment = false;
		view.showOthersContentPanel();
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
					transaction.replace(R.id.others_content_panel, citiesTabFragment);
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
					transaction.replace(R.id.others_content_panel, citiesManagerFragment);
					transaction.addToBackStack("manageCity");
					transaction.commit();
				}
				break;

			case R.id.setting:
				for (int i = fm.getBackStackEntryCount() - 1; i >= 0; i--) {
					if (fm.getBackStackEntryAt(i).getName().equals("settings")) {
						fm.popBackStackImmediate("settings", 0);
						isFoundFragment = true;
						break;
					}
				}

				if (!isFoundFragment) {
					SettingsFragment settingsFragment = new SettingsFragment();
					FragmentTransaction transaction = view.getIViewFragmentManager().beginTransaction();
					transaction.replace(R.id.others_content_panel, settingsFragment);
					transaction.addToBackStack("settings");
					transaction.commit();
				}
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
