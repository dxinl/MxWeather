package com.mx.dxinl.mvp_mxweather.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.mx.dxinl.mvp_mxweather.utils.AssetsDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dxinl on 2016/1/6.
 */
public class DBQueryHelper {
	public static List<String> queryCities(String str, String type) {
		SQLiteDatabase db = AssetsDatabaseHelper.getDB();
		List<String> cities = new ArrayList<>();
		String sql;
		if (type != null && type.equals("cities")) {
			sql = "SELECT name FROM cities WHERE enname like \"%" + str + "%\" or name like \"%" + str + "%\" ORDER BY enname";
		} else {
			sql = "SELECT name FROM attractions WHERE name like \"%" + str + "%\" ORDER BY province";
		}
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			cities.add(cursor.getString(cursor.getColumnIndex("name")));
		}
		cursor.close();
		return cities;
	}

	public static String queryCityNum(String city) {
		SQLiteDatabase db = AssetsDatabaseHelper.getDB();
		String cityNum = "";
		String sql = "SELECT num FROM cities WHERE name=\"" + city + "\"";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			cityNum = cursor.getString(cursor.getColumnIndex("num"));
		}
		cursor.close();

		if (!TextUtils.isEmpty(cityNum)) {
			return cityNum;
		}

		sql = "SELECT num FROM cities WHERE name=\"" + city + "\"";
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			cityNum = cursor.getString(cursor.getColumnIndex("num"));
		}
		cursor.close();

		if (!TextUtils.isEmpty(cityNum)) {
			return cityNum;
		} else {
			return "";
		}
	}
}
