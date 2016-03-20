package com.mx.dxinl.mvp_mxweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dxinl on 2016/1/4.
 */
public class AssetsDatabaseHelper {
	private static final String TAG = AssetsDatabaseHelper.class.getSimpleName();
	private static final String DB_FOLDER = "databases";
	private static final String DB_NAME = "cities.db";
	private static final String SEPARATOR = "/";
	private static final String VERSION = "version";
	private static SQLiteDatabase DB = null;

	public static boolean copyDBFromAssets(Context context) throws IOException {
		String dir = context.getFilesDir().getPath();

		// make dirs
		String path = dir + SEPARATOR + DB_FOLDER;
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			if (!file.mkdirs()) {
				return false;
			}
		}

		path = path + SEPARATOR + DB_NAME;
		file = new File(path);
		if (!file.exists() || !file.isFile()) {
			if (!file.createNewFile()) {
				return false;
			}
		}

		InputStream is = context.getAssets().open(DB_NAME);
		OutputStream os = new FileOutputStream(file);
		int length;
		byte[] buffer = new byte[1024];
		while ((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}
		os.flush();
		os.close();
		is.close();

		return true;
	}

	public static boolean openDatabase(Context context) {
		if (DB == null) {
			try {
				String pkgName = context.getPackageName();
				SharedPreferences sp = context.getSharedPreferences(pkgName, Context.MODE_PRIVATE);
				String curVersionName = sp.getString(VERSION, VERSION);
				PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
				String versionName = pkgInfo.versionName;

				String dir = context.getFilesDir().getPath();
				String path = dir + SEPARATOR + DB_FOLDER + SEPARATOR + DB_NAME;
								File file = new File(path);
				if (!file.exists() || !file.isFile() || !curVersionName.equals(versionName)) {
					if (!copyDBFromAssets(context)) {
						return false;
					}

					if (!curVersionName.equals(versionName)) {
						sp.edit().putString(VERSION, versionName).apply();
					}
				}

				DB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			} catch (IOException | PackageManager.NameNotFoundException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public static SQLiteDatabase getDB() {
		return DB;
	}
}
