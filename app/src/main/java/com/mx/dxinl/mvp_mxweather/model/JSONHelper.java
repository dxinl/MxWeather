package com.mx.dxinl.mvp_mxweather.model;

import com.mx.dxinl.mvp_mxweather.model.bean.AirQualityBean;
import com.mx.dxinl.mvp_mxweather.model.bean.DailyWeatherBean;
import com.mx.dxinl.mvp_mxweather.model.bean.HourlyWeatherBean;
import com.mx.dxinl.mvp_mxweather.model.bean.NowWeatherBean;
import com.mx.dxinl.mvp_mxweather.model.bean.SuggestionBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DengXinliang on 2016/1/29.
 */
public class JSONHelper {
	private final String HEADER = "HeWeather data service 3.0";
	private JSONObject jsonObject;

	public JSONHelper(JSONObject object) {
		jsonObject = object;
	}

	public boolean checkJSONObject() {
		try {
			JSONArray header = jsonObject.getJSONArray(HEADER);
			String status = header.getJSONObject(0).getString("status");
			System.out.println(status);
			return status.equalsIgnoreCase("OK");
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<HourlyWeatherBean> getHourlyWeather() {
		try {
			List<HourlyWeatherBean> hourlyWeathers = new ArrayList<>();
			JSONObject header = jsonObject.getJSONArray(HEADER).getJSONObject(0);
			JSONArray hourlyWeatherJSON = header.getJSONArray("hourly_forecast");
			int index = 0;
			while (true) {
				JSONObject hourlyWeatherObject;

				// avoid "index out of JSONArray range";
				try {
					hourlyWeatherObject = hourlyWeatherJSON.getJSONObject(index);
				} catch (Exception e) {
					break;
				}
				HourlyWeatherBean hourlyWeather = new HourlyWeatherBean();
				hourlyWeather.tmp = hourlyWeatherObject.getString("tmp");
				hourlyWeather.pop = hourlyWeatherObject.getString("pop");

				String date = hourlyWeatherObject.getString("date");
				date = date.substring(date.length() - 5, date.length());
				hourlyWeather.date = date;

				JSONObject windObject = hourlyWeatherObject.getJSONObject("wind");
				hourlyWeather.dir = windObject.getString("dir");
				hourlyWeather.sc = windObject.getString("sc");
				hourlyWeathers.add(hourlyWeather);

				index++;
			}
			return hourlyWeathers;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public NowWeatherBean getNowWeather() {
		try {
			NowWeatherBean nowWeather = new NowWeatherBean();
			JSONObject header = jsonObject.getJSONArray(HEADER).getJSONObject(0);
			JSONObject nowWeatherJSON = header.getJSONObject("now");

			nowWeather.code = nowWeatherJSON.getJSONObject("cond").getString("code");
			nowWeather.txt = nowWeatherJSON.getJSONObject("cond").getString("txt");
			nowWeather.fl = nowWeatherJSON.getString("fl");
			nowWeather.hum = nowWeatherJSON.getString("hum");
			nowWeather.pcpn = nowWeatherJSON.getString("pcpn");
			nowWeather.pres = nowWeatherJSON.getString("pres");
			nowWeather.tmp = nowWeatherJSON.getString("tmp");
			nowWeather.vis = nowWeatherJSON.getString("vis");
			nowWeather.deg = nowWeatherJSON.getJSONObject("wind").getString("deg");
			nowWeather.dir = nowWeatherJSON.getJSONObject("wind").getString("dir");
			nowWeather.sc = nowWeatherJSON.getJSONObject("wind").getString("sc");
			nowWeather.spd = nowWeatherJSON.getJSONObject("wind").getString("spd");

			return nowWeather;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public AirQualityBean getAirQuality() {
		try {
			AirQualityBean airQuality = new AirQualityBean();
			JSONObject header = jsonObject.getJSONArray(HEADER).getJSONObject(0);
			JSONObject airQualityJSON = header.getJSONObject("aqi").getJSONObject("city");

			airQuality.aqi = airQualityJSON.getString("aqi");
			airQuality.co = airQualityJSON.getString("co");
			airQuality.no2 = airQualityJSON.getString("no2");
			airQuality.o3 = airQualityJSON.getString("o3");
			airQuality.pm10 = airQualityJSON.getString("pm10");
			airQuality.pm25 = airQualityJSON.getString("pm25");
			airQuality.qlty = airQualityJSON.getString("qlty");
			airQuality.so2 = airQualityJSON.getString("so2");

			return airQuality;
		} catch (JSONException e) {
			return null;
		}
	}

	public List<DailyWeatherBean> getDailyWeathers() {
		try {
			List<DailyWeatherBean> dailyWeathers = new ArrayList<>();
			JSONObject header = jsonObject.getJSONArray(HEADER).getJSONObject(0);
			JSONArray dailyWeatherJSON = header.getJSONArray("daily_forecast");

			int index = 0;
			while (true) {
				JSONObject DailyWeatherJsonObject;

				try {
					DailyWeatherJsonObject = dailyWeatherJSON.getJSONObject(index);
				} catch (Exception e) {
					break;
				}

				DailyWeatherBean dailyWeather = new DailyWeatherBean();
				String date = DailyWeatherJsonObject.getString("date");
				int tmpIndex = date.indexOf("-");
				if (tmpIndex != -1) {
					date = date.substring(tmpIndex + 1);
				}
				dailyWeather.date = date;


				dailyWeather.hum = DailyWeatherJsonObject.getString("hum");
				dailyWeather.pcpn = DailyWeatherJsonObject.getString("pcpn");
				dailyWeather.pop = DailyWeatherJsonObject.getString("pop");
				dailyWeather.pres = DailyWeatherJsonObject.getString("pres");
				dailyWeather.vis = DailyWeatherJsonObject.getString("vis");

				JSONObject astroJSONObject = DailyWeatherJsonObject.getJSONObject("astro");
				dailyWeather.sr = astroJSONObject.getString("sr");
				dailyWeather.ss = astroJSONObject.getString("ss");

				JSONObject tmpJSONObject = DailyWeatherJsonObject.getJSONObject("tmp");
				dailyWeather.maxTmp = tmpJSONObject.getString("max");
				dailyWeather.minTmp = tmpJSONObject.getString("min");

				JSONObject condJSONObject = DailyWeatherJsonObject.getJSONObject("cond");
				dailyWeather.code_d = condJSONObject.getString("code_d");
				dailyWeather.code_n = condJSONObject.getString("code_n");
				dailyWeather.txt_d = condJSONObject.getString("txt_d");
				dailyWeather.txt_n = condJSONObject.getString("txt_n");

				JSONObject windJSONObject = DailyWeatherJsonObject.getJSONObject("wind");
				dailyWeather.deg = windJSONObject.getString("deg");
				dailyWeather.dir = windJSONObject.getString("dir");
				dailyWeather.sc = windJSONObject.getString("sc");
				dailyWeather.spd = windJSONObject.getString("spd");

				dailyWeathers.add(dailyWeather);
				index++;
			}

			return dailyWeathers;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SuggestionBean getSuggestion() {
		try {
			SuggestionBean suggestion = new SuggestionBean();
			List<DailyWeatherBean> dailyWeathers = new ArrayList<>();
			JSONObject header = jsonObject.getJSONArray(HEADER).getJSONObject(0);
			JSONObject suggestionJSON = header.getJSONObject("suggestion");

			JSONObject summaryJSON = suggestionJSON.getJSONObject("comf");
			suggestion.sumBrf = summaryJSON.getString("brf");
			suggestion.sumTxt = summaryJSON.getString("txt");

			JSONObject cwJSON = suggestionJSON.getJSONObject("cw");
			suggestion.cwBrf = cwJSON.getString("brf");
			suggestion.cwTxt = cwJSON.getString("txt");

			JSONObject drsgJSON = suggestionJSON.getJSONObject("drsg");
			suggestion.drsgBrf = drsgJSON.getString("brf");
			suggestion.drsgTxt = drsgJSON.getString("txt");

			JSONObject fluJSON = suggestionJSON.getJSONObject("flu");
			suggestion.fluBrf = fluJSON.getString("brf");
			suggestion.fluTxt = fluJSON.getString("txt");

			JSONObject sportJSON = suggestionJSON.getJSONObject("sport");
			suggestion.sportBrf = sportJSON.getString("brf");
			suggestion.sportTxt = sportJSON.getString("txt");

			JSONObject travJSON = suggestionJSON.getJSONObject("trav");
			suggestion.travBrf = travJSON.getString("brf");
			suggestion.travTxt = travJSON.getString("txt");

			JSONObject uvJSON = suggestionJSON.getJSONObject("uv");
			suggestion.uvBrf = uvJSON.getString("brf");
			suggestion.uvTxt = uvJSON.getString("txt");

			return suggestion;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
