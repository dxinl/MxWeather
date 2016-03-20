package com.mx.dxinl.mvp_mxweather.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Pair;

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

	public void setCurrentCityInfo(String currentCityName, String currentCityNum) {
		if (sp == null) {
			sp = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
		}
		SharedPreferences.Editor spEditor = sp.edit();
		spEditor.putString(CURRENT_CITY, currentCityName + "," + currentCityNum);
		spEditor.commit();
	}

	public String[] getCurrentCityInfo() {
		if (sp == null) {
			sp = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
		}
		String currentCityNameAndNum = sp.getString(CURRENT_CITY, "");
		if (TextUtils.isEmpty(currentCityNameAndNum)) {
			return null;
		}

		return currentCityNameAndNum.split(",");
	}

	public List<Pair<String, String>> getCitiesInfo() {
		List<Pair<String, String>> chosenCities = new ArrayList<>();
		if (sp == null) {
			sp = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
		}
		String chosenCitiesNameAndNum = sp.getString(CHOSEN_CITIES, "");
		if (TextUtils.isEmpty(chosenCitiesNameAndNum)) {
			Pair<String, String> nameNumPair = new Pair<>("北京", "CN101010100");
			chosenCities.add(nameNumPair);
			return chosenCities;
		}

		String[] nameOrNum = chosenCitiesNameAndNum.split(",");
		for (int i = 0; i < nameOrNum.length; i += 2) {
			Pair<String, String> nameNumPair = new Pair<>(nameOrNum[i], nameOrNum[i + 1]);
			chosenCities.add(nameNumPair);
		}
		return chosenCities;
	}

	public void setChosenCities(List<Pair<String, String>> chosenCities) {
		if (sp == null) {
			sp = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
		}
		StringBuilder sb = new StringBuilder();
		for (Pair<String, String> cityInfo : chosenCities) {
			if (sb.length() != 0) {
				sb.append(",");
			}

			sb.append(cityInfo.first).append(",").append(cityInfo.second);
		}

		SharedPreferences.Editor spEditor = sp.edit();
		spEditor.putString(CHOSEN_CITIES, sb.toString());
		spEditor.commit();
	}

	public void addChosenCity(String newCityName, String newCityNum) {
		if (sp == null) {
			sp = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
		}
		String chosenCitiesNameAndNum = sp.getString(CHOSEN_CITIES, "");
		if (chosenCitiesNameAndNum.contains(newCityName)) {
			return;
		}

		if (TextUtils.isEmpty(chosenCitiesNameAndNum)) {
			chosenCitiesNameAndNum = newCityName + "," + newCityNum;
		} else {
			chosenCitiesNameAndNum = chosenCitiesNameAndNum + "," + newCityName + "," + newCityNum;
		}
		SharedPreferences.Editor spEditor = sp.edit();
		spEditor.putString(CHOSEN_CITIES, chosenCitiesNameAndNum);
		spEditor.commit();
	}
}
