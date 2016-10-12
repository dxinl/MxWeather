package com.mx.dxinl.mvp_mxweather.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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

	private static final String KEY = "Your Key";
	private static final String HTTP_API = "https://api.heweather.com/x3/%s?cityid=%s&key=%s";

	private NetworkHelper() {}

	public static NetworkHelper get() {
		if (INSTANCE == null) {
			INSTANCE = new NetworkHelper();
		}

		return INSTANCE;
	}

	public JSONObject getJSON() throws JSONException {
		return new JSONObject("{\n" +
				"  \"HeWeather data service 3.0\": [\n" +
				"    {\n" +
				"      \"aqi\": {\n" +
				"        \"city\": {\n" +
				"          \"aqi\": \"182\",\n" +
				"          \"co\": \"1\",\n" +
				"          \"no2\": \"91\",\n" +
				"          \"o3\": \"10\",\n" +
				"          \"pm10\": \"152\",\n" +
				"          \"pm25\": \"137\",\n" +
				"          \"qlty\": \"中度污染\",\n" +
				"          \"so2\": \"27\"\n" +
				"        }\n" +
				"      },\n" +
				"      \"basic\": {\n" +
				"        \"city\": \"广州\",\n" +
				"        \"cnty\": \"中国\",\n" +
				"        \"id\": \"CN101280101\",\n" +
				"        \"lat\": \"23.108000\",\n" +
				"        \"lon\": \"113.265000\",\n" +
				"        \"update\": {\n" +
				"          \"loc\": \"2016-04-01 09:49\",\n" +
				"          \"utc\": \"2016-04-01 01:49\"\n" +
				"        }\n" +
				"      },\n" +
				"      \"daily_forecast\": [\n" +
				"        {\n" +
				"          \"astro\": {\n" +
				"            \"sr\": \"06:18\",\n" +
				"            \"ss\": \"18:42\"\n" +
				"          },\n" +
				"          \"cond\": {\n" +
				"            \"code_d\": \"501\",\n" +
				"            \"code_n\": \"501\",\n" +
				"            \"txt_d\": \"雾\",\n" +
				"            \"txt_n\": \"雾\"\n" +
				"          },\n" +
				"          \"date\": \"2016-04-01\",\n" +
				"          \"hum\": \"54\",\n" +
				"          \"pcpn\": \"0.0\",\n" +
				"          \"pop\": \"18\",\n" +
				"          \"pres\": \"1013\",\n" +
				"          \"tmp\": {\n" +
				"            \"max\": \"27\",\n" +
				"            \"min\": \"19\"\n" +
				"          },\n" +
				"          \"vis\": \"10\",\n" +
				"          \"wind\": {\n" +
				"            \"deg\": \"174\",\n" +
				"            \"dir\": \"无持续风向\",\n" +
				"            \"sc\": \"微风\",\n" +
				"            \"spd\": \"7\"\n" +
				"          }\n" +
				"        },\n" +
				"        {\n" +
				"          \"astro\": {\n" +
				"            \"sr\": \"06:17\",\n" +
				"            \"ss\": \"18:42\"\n" +
				"          },\n" +
				"          \"cond\": {\n" +
				"            \"code_d\": \"501\",\n" +
				"            \"code_n\": \"104\",\n" +
				"            \"txt_d\": \"雾\",\n" +
				"            \"txt_n\": \"阴\"\n" +
				"          },\n" +
				"          \"date\": \"2016-04-02\",\n" +
				"          \"hum\": \"54\",\n" +
				"          \"pcpn\": \"0.0\",\n" +
				"          \"pop\": \"0\",\n" +
				"          \"pres\": \"1014\",\n" +
				"          \"tmp\": {\n" +
				"            \"max\": \"26\",\n" +
				"            \"min\": \"19\"\n" +
				"          },\n" +
				"          \"vis\": \"10\",\n" +
				"          \"wind\": {\n" +
				"            \"deg\": \"182\",\n" +
				"            \"dir\": \"无持续风向\",\n" +
				"            \"sc\": \"微风\",\n" +
				"            \"spd\": \"2\"\n" +
				"          }\n" +
				"        },\n" +
				"        {\n" +
				"          \"astro\": {\n" +
				"            \"sr\": \"06:16\",\n" +
				"            \"ss\": \"18:43\"\n" +
				"          },\n" +
				"          \"cond\": {\n" +
				"            \"code_d\": \"305\",\n" +
				"            \"code_n\": \"305\",\n" +
				"            \"txt_d\": \"小雨\",\n" +
				"            \"txt_n\": \"小雨\"\n" +
				"          },\n" +
				"          \"date\": \"2016-04-03\",\n" +
				"          \"hum\": \"63\",\n" +
				"          \"pcpn\": \"0.2\",\n" +
				"          \"pop\": \"29\",\n" +
				"          \"pres\": \"1014\",\n" +
				"          \"tmp\": {\n" +
				"            \"max\": \"25\",\n" +
				"            \"min\": \"20\"\n" +
				"          },\n" +
				"          \"vis\": \"10\",\n" +
				"          \"wind\": {\n" +
				"            \"deg\": \"198\",\n" +
				"            \"dir\": \"无持续风向\",\n" +
				"            \"sc\": \"微风\",\n" +
				"            \"spd\": \"6\"\n" +
				"          }\n" +
				"        },\n" +
				"        {\n" +
				"          \"astro\": {\n" +
				"            \"sr\": \"06:15\",\n" +
				"            \"ss\": \"18:43\"\n" +
				"          },\n" +
				"          \"cond\": {\n" +
				"            \"code_d\": \"104\",\n" +
				"            \"code_n\": \"104\",\n" +
				"            \"txt_d\": \"阴\",\n" +
				"            \"txt_n\": \"阴\"\n" +
				"          },\n" +
				"          \"date\": \"2016-04-04\",\n" +
				"          \"hum\": \"53\",\n" +
				"          \"pcpn\": \"4.9\",\n" +
				"          \"pop\": \"73\",\n" +
				"          \"pres\": \"1012\",\n" +
				"          \"tmp\": {\n" +
				"            \"max\": \"25\",\n" +
				"            \"min\": \"21\"\n" +
				"          },\n" +
				"          \"vis\": \"10\",\n" +
				"          \"wind\": {\n" +
				"            \"deg\": \"197\",\n" +
				"            \"dir\": \"无持续风向\",\n" +
				"            \"sc\": \"微风\",\n" +
				"            \"spd\": \"7\"\n" +
				"          }\n" +
				"        },\n" +
				"        {\n" +
				"          \"astro\": {\n" +
				"            \"sr\": \"06:15\",\n" +
				"            \"ss\": \"18:44\"\n" +
				"          },\n" +
				"          \"cond\": {\n" +
				"            \"code_d\": \"104\",\n" +
				"            \"code_n\": \"101\",\n" +
				"            \"txt_d\": \"阴\",\n" +
				"            \"txt_n\": \"多云\"\n" +
				"          },\n" +
				"          \"date\": \"2016-04-05\",\n" +
				"          \"hum\": \"58\",\n" +
				"          \"pcpn\": \"0.2\",\n" +
				"          \"pop\": \"13\",\n" +
				"          \"pres\": \"1012\",\n" +
				"          \"tmp\": {\n" +
				"            \"max\": \"26\",\n" +
				"            \"min\": \"22\"\n" +
				"          },\n" +
				"          \"vis\": \"10\",\n" +
				"          \"wind\": {\n" +
				"            \"deg\": \"179\",\n" +
				"            \"dir\": \"无持续风向\",\n" +
				"            \"sc\": \"微风\",\n" +
				"            \"spd\": \"4\"\n" +
				"          }\n" +
				"        },\n" +
				"        {\n" +
				"          \"astro\": {\n" +
				"            \"sr\": \"06:14\",\n" +
				"            \"ss\": \"18:44\"\n" +
				"          },\n" +
				"          \"cond\": {\n" +
				"            \"code_d\": \"101\",\n" +
				"            \"code_n\": \"101\",\n" +
				"            \"txt_d\": \"多云\",\n" +
				"            \"txt_n\": \"多云\"\n" +
				"          },\n" +
				"          \"date\": \"2016-04-06\",\n" +
				"          \"hum\": \"51\",\n" +
				"          \"pcpn\": \"0.1\",\n" +
				"          \"pop\": \"37\",\n" +
				"          \"pres\": \"1011\",\n" +
				"          \"tmp\": {\n" +
				"            \"max\": \"27\",\n" +
				"            \"min\": \"22\"\n" +
				"          },\n" +
				"          \"vis\": \"10\",\n" +
				"          \"wind\": {\n" +
				"            \"deg\": \"188\",\n" +
				"            \"dir\": \"无持续风向\",\n" +
				"            \"sc\": \"微风\",\n" +
				"            \"spd\": \"0\"\n" +
				"          }\n" +
				"        },\n" +
				"        {\n" +
				"          \"astro\": {\n" +
				"            \"sr\": \"06:13\",\n" +
				"            \"ss\": \"18:44\"\n" +
				"          },\n" +
				"          \"cond\": {\n" +
				"            \"code_d\": \"101\",\n" +
				"            \"code_n\": \"306\",\n" +
				"            \"txt_d\": \"多云\",\n" +
				"            \"txt_n\": \"中雨\"\n" +
				"          },\n" +
				"          \"date\": \"2016-04-07\",\n" +
				"          \"hum\": \"68\",\n" +
				"          \"pcpn\": \"0.5\",\n" +
				"          \"pop\": \"47\",\n" +
				"          \"pres\": \"1012\",\n" +
				"          \"tmp\": {\n" +
				"            \"max\": \"27\",\n" +
				"            \"min\": \"20\"\n" +
				"          },\n" +
				"          \"vis\": \"10\",\n" +
				"          \"wind\": {\n" +
				"            \"deg\": \"183\",\n" +
				"            \"dir\": \"无持续风向\",\n" +
				"            \"sc\": \"微风\",\n" +
				"            \"spd\": \"8\"\n" +
				"          }\n" +
				"        }\n" +
				"      ],\n" +
				"      \"hourly_forecast\": [\n" +
				"        {\n" +
				"          \"date\": \"2016-04-01 10:00\",\n" +
				"          \"hum\": \"68\",\n" +
				"          \"pop\": \"0\",\n" +
				"          \"pres\": \"1015\",\n" +
				"          \"tmp\": \"28\",\n" +
				"          \"wind\": {\n" +
				"            \"deg\": \"163\",\n" +
				"            \"dir\": \"东南风\",\n" +
				"            \"sc\": \"微风\",\n" +
				"            \"spd\": \"8\"\n" +
				"          }\n" +
				"        },\n" +
				"        {\n" +
				"          \"date\": \"2016-04-01 13:00\",\n" +
				"          \"hum\": \"57\",\n" +
				"          \"pop\": \"0\",\n" +
				"          \"pres\": \"1014\",\n" +
				"          \"tmp\": \"30\",\n" +
				"          \"wind\": {\n" +
				"            \"deg\": \"172\",\n" +
				"            \"dir\": \"南风\",\n" +
				"            \"sc\": \"微风\",\n" +
				"            \"spd\": \"12\"\n" +
				"          }\n" +
				"        },\n" +
				"        {\n" +
				"          \"date\": \"2016-04-01 16:00\",\n" +
				"          \"hum\": \"59\",\n" +
				"          \"pop\": \"12\",\n" +
				"          \"pres\": \"1012\",\n" +
				"          \"tmp\": \"29\",\n" +
				"          \"wind\": {\n" +
				"            \"deg\": \"165\",\n" +
				"            \"dir\": \"东南风\",\n" +
				"            \"sc\": \"3-4\",\n" +
				"            \"spd\": \"18\"\n" +
				"          }\n" +
				"        },\n" +
				"        {\n" +
				"          \"date\": \"2016-04-01 19:00\",\n" +
				"          \"hum\": \"69\",\n" +
				"          \"pop\": \"6\",\n" +
				"          \"pres\": \"1012\",\n" +
				"          \"tmp\": \"26\",\n" +
				"          \"wind\": {\n" +
				"            \"deg\": \"159\",\n" +
				"            \"dir\": \"东南风\",\n" +
				"            \"sc\": \"3-4\",\n" +
				"            \"spd\": \"23\"\n" +
				"          }\n" +
				"        },\n" +
				"        {\n" +
				"          \"date\": \"2016-04-01 22:00\",\n" +
				"          \"hum\": \"76\",\n" +
				"          \"pop\": \"0\",\n" +
				"          \"pres\": \"1014\",\n" +
				"          \"tmp\": \"23\",\n" +
				"          \"wind\": {\n" +
				"            \"deg\": \"159\",\n" +
				"            \"dir\": \"东南风\",\n" +
				"            \"sc\": \"3-4\",\n" +
				"            \"spd\": \"21\"\n" +
				"          }\n" +
				"        }\n" +
				"      ],\n" +
				"      \"now\": {\n" +
				"        \"cond\": {\n" +
				"          \"code\": \"104\",\n" +
				"          \"txt\": \"阴\"\n" +
				"        },\n" +
				"        \"fl\": \"25\",\n" +
				"        \"hum\": \"78\",\n" +
				"        \"pcpn\": \"0\",\n" +
				"        \"pres\": \"1014\",\n" +
				"        \"tmp\": \" " + (System.currentTimeMillis() / 1000 % 2 == 0 ? 22 : 18) + "\",\n" +
				"        \"vis\": \"1\",\n" +
				"        \"wind\": {\n" +
				"          \"deg\": \"100\",\n" +
				"          \"dir\": \"东南风\",\n" +
				"          \"sc\": \"3-4\",\n" +
				"          \"spd\": \"14\"\n" +
				"        }\n" +
				"      },\n" +
				"      \"status\": \"ok\",\n" +
				"      \"suggestion\": {\n" +
				"        \"comf\": {\n" +
				"          \"brf\": \"较舒适\",\n" +
				"          \"txt\": \"白天天气阴沉，同时较大的空气湿度，会使您感到有点儿闷热，但大部分人还是完全可以接受。\"\n" +
				"        },\n" +
				"        \"cw\": {\n" +
				"          \"brf\": \"不宜\",\n" +
				"          \"txt\": \"不宜洗车，未来24小时内有雾，如果在此期间洗车，会弄脏您的爱车。\"\n" +
				"        },\n" +
				"        \"drsg\": {\n" +
				"          \"brf\": \"舒适\",\n" +
				"          \"txt\": \"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。\"\n" +
				"        },\n" +
				"        \"flu\": {\n" +
				"          \"brf\": \"少发\",\n" +
				"          \"txt\": \"各项气象条件适宜，无明显降温过程，发生感冒机率较低。\"\n" +
				"        },\n" +
				"        \"sport\": {\n" +
				"          \"brf\": \"较不宜\",\n" +
				"          \"txt\": \"有雾或霾天气，建议适当停止户外运动，选择在室内进行运动，以避免吸入过多雾中有害物质，损害健康。\"\n" +
				"        },\n" +
				"        \"trav\": {\n" +
				"          \"brf\": \"较不宜\",\n" +
				"          \"txt\": \"空气质量差，不适宜旅游\"\n" +
				"        },\n" +
				"        \"uv\": {\n" +
				"          \"brf\": \"最弱\",\n" +
				"          \"txt\": \"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。\"\n" +
				"        }\n" +
				"      }\n" +
				"    }\n" +
				"  ]\n" +
				"}");
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
		reader.close();
		is.close();
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
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
		is.close();
		return bitmap;
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
