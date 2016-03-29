package com.mx.dxinl.mvp_mxweather.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.mx.dxinl.mvp_mxweather.model.bean.CityInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DengXinliang on 2016/3/14.
 */
public class SharedPreferencesHelper {
	private final String CHOSEN_CITIES = "CHOSEN_CITY";
	private final String CURRENT_CITY = "CURRENT_CITY";

	private SharedPreferences sp;
	private Context context;

	public SharedPreferencesHelper(Context context) {
		this.context = context;
	}

	public void setCurrentCityInfo(String currentCityName, String currentCityNum, String currentCityType) {
		if (sp == null) {
			sp = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
		}
		SharedPreferences.Editor spEditor = sp.edit();
		spEditor.putString(CURRENT_CITY, currentCityName + "," + currentCityNum + "," + currentCityType);
		spEditor.commit();
	}

	public String[] getCurrentCityInfo() {
		if (sp == null) {
			sp = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
		}
		String currentCityInfo = sp.getString(CURRENT_CITY, "");
		if (TextUtils.isEmpty(currentCityInfo)) {
			return null;
		}

		return currentCityInfo.split(",");
	}

	public List<CityInfo> getCitiesInfo() {
		List<CityInfo> chosenCities = new ArrayList<>();
		if (sp == null) {
			sp = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
		}
		String chosenCitiesNameAndNum = sp.getString(CHOSEN_CITIES, "");
		if (TextUtils.isEmpty(chosenCitiesNameAndNum)) {
			CityInfo cityInfo = new CityInfo();
			cityInfo.name = "北京";
			cityInfo.num = "CN101010100";
			cityInfo.type = "weather";
			chosenCities.add(cityInfo);
			return chosenCities;
		}

		String[] info = chosenCitiesNameAndNum.split(",");
		for (int i = 0; i < info.length; i += 3) {
			CityInfo cityInfo = new CityInfo();
			cityInfo.name = info[i];
			cityInfo.num = info[i + 1];
			cityInfo.type = info[i + 2];

			chosenCities.add(cityInfo);
		}
		return chosenCities;
	}

	public void setChosenCities(List<CityInfo> chosenCities) {
		if (sp == null) {
			sp = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
		}
		StringBuilder sb = new StringBuilder();
		for (CityInfo cityInfo : chosenCities) {
			if (sb.length() != 0) {
				sb.append(",");
			}

			sb.append(cityInfo.name).append(",").append(cityInfo.num).append(",").append(cityInfo.type);
		}

		SharedPreferences.Editor spEditor = sp.edit();
		spEditor.putString(CHOSEN_CITIES, sb.toString());
		spEditor.commit();
	}

	public void addChosenCity(String newCityName, String newCityNum, String newCityType) {
		if (sp == null) {
			sp = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
		}
		String chosenCitiesNameAndNum = sp.getString(CHOSEN_CITIES, "");
		if (chosenCitiesNameAndNum.contains(newCityName)) {
			return;
		}

		if (TextUtils.isEmpty(chosenCitiesNameAndNum)) {
			chosenCitiesNameAndNum = newCityName + "," + newCityNum + "," + newCityType;
		} else {
			chosenCitiesNameAndNum = chosenCitiesNameAndNum + "," + newCityName + "," + newCityNum + "," + newCityType;
		}
		SharedPreferences.Editor spEditor = sp.edit();
		spEditor.putString(CHOSEN_CITIES, chosenCitiesNameAndNum);
		spEditor.commit();
	}
}
