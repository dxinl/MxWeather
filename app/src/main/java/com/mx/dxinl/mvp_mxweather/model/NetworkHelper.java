package com.mx.dxinl.mvp_mxweather.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeoutException;

/**
 * Created by DengXinliang on 2016/1/29.
 */
public class NetworkHelper {
	private static final boolean DEBUG = false;
	private static NetworkHelper INSTANCE = null;

	private final String KEY = "29a97c76564c46769d05279c3105cc4c";
	private final String HTTP_API = "https://api.heweather.com/x3/%s?cityid=%s&key=%s";

	private NetworkHelper() {}

	public static NetworkHelper get() {
		if (INSTANCE == null) {
			INSTANCE = new NetworkHelper();
		}

		return INSTANCE;
	}

	public JSONObject getJSONFromNetwork(int timeOut, String city, String apiType) throws IOException, JSONException, TimeoutException {
		String urlStr = String.format(HTTP_API, apiType, city, KEY);
		InputStream is = getInputStreamFromNetwork(timeOut, urlStr);
		if (is == null) {
			return null;
		}

		InputStreamReader reader = new InputStreamReader(is);
		StringBuilder sbd = new StringBuilder();
		char[] buffer = new char[1024];
		int length;
		while ((length = reader.read(buffer)) > 0) {
			sbd.append(buffer, 0, length);
		}
		if (DEBUG) {
			System.out.println(sbd.toString());
		}
		return new JSONObject(sbd.toString());
	}

	public Bitmap getBitmapFromNetwork(int timeOut, String code) throws IOException, TimeoutException {
		String urlStr = "http://files.heweather.com/cond_icon/" + code + ".png";
		InputStream is = getInputStreamFromNetwork(timeOut, urlStr);
		if (is == null) {
			return null;
		}

		int sampleSize = 1;
		int length = is.available() / 1024 / 100;
		while (length / 2 > 1) {
			sampleSize ++;
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = sampleSize;
		return BitmapFactory.decodeStream(is, null, options);
	}

	private InputStream getInputStreamFromNetwork(int timeOut, String urlStr) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(timeOut);
		try {
			return connection.getInputStream();
		} catch (Exception e) {
			return null;
		}
	}
}
