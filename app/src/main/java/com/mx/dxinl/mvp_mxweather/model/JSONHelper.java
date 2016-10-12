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
	private static boolean debug = false;
	private final String HEADER = "HeWeather data service 3.0";
	private JSONObject jsonObject;

	public JSONHelper(JSONObject object) {
		jsonObject = object;
	}

	public boolean checkJSONObject() {
        JSONArray header = getJSONArray(jsonObject, HEADER);
        String status = getJSONString(getJSONObject(header, 0), "status");
        if (debug) {
            System.out.println(status);
        }
        return status.equalsIgnoreCase("OK");
	}

	public List<HourlyWeatherBean> getHourlyWeather() {
        List<HourlyWeatherBean> hourlyWeathers = new ArrayList<>();
        JSONObject header = getJSONObject(getJSONArray(jsonObject, HEADER), 0);
        JSONArray hourlyWeatherJSON = getJSONArray(header, "hourly_forecast");
        int index = 0;
        while (true) {
            JSONObject hourlyWeatherObject;

            hourlyWeatherObject = getJSONObject(hourlyWeatherJSON, index);
            // avoid "index out of JSONArray range";
            if (hourlyWeatherObject == null) {
                break;
            }

            HourlyWeatherBean hourlyWeather = new HourlyWeatherBean();
            hourlyWeather.tmp = getJSONString(hourlyWeatherObject, "tmp");
            hourlyWeather.pop = getJSONString(hourlyWeatherObject, "pop");

            String date = getJSONString(hourlyWeatherObject, "date");
            date = date.substring(date.length() - 5, date.length());
            hourlyWeather.date = date;

            JSONObject windObject = getJSONObject(hourlyWeatherObject, "wind");
            hourlyWeather.dir = getJSONString(windObject, "dir");
            hourlyWeather.sc = getJSONString(windObject, "sc");
            hourlyWeathers.add(hourlyWeather);

            index++;
        }
        return hourlyWeathers;
	}

	public NowWeatherBean getNowWeather() {
        NowWeatherBean nowWeather = new NowWeatherBean();
        JSONObject header = getJSONObject(getJSONArray(jsonObject, HEADER), 0);
        JSONObject nowWeatherJSON = getJSONObject(header, "now");

        nowWeather.code = getJSONString(getJSONObject(nowWeatherJSON, "cond"), "code");
        nowWeather.txt = getJSONString(getJSONObject(nowWeatherJSON, "cond"), "txt");
        nowWeather.fl = getJSONString(nowWeatherJSON, "fl");
        nowWeather.hum = getJSONString(nowWeatherJSON, "hum");
        nowWeather.pcpn = getJSONString(nowWeatherJSON, "pcpn");
        nowWeather.pres = getJSONString(nowWeatherJSON, "pres");
        nowWeather.tmp = getJSONString(nowWeatherJSON, "tmp");
        nowWeather.vis = getJSONString(nowWeatherJSON, "vis");
        nowWeather.deg = getJSONString(getJSONObject(nowWeatherJSON, "wind"), "deg");
        nowWeather.dir = getJSONString(getJSONObject(nowWeatherJSON, "wind"), "dir");
        nowWeather.sc = getJSONString(getJSONObject(nowWeatherJSON, "wind"), "sc");
        nowWeather.spd = getJSONString(getJSONObject(nowWeatherJSON, "wind"), "spd");

        return nowWeather;
	}

	public AirQualityBean getAirQuality() {
        AirQualityBean airQuality = new AirQualityBean();
        JSONObject header = getJSONObject(getJSONArray(jsonObject, HEADER), 0);
        JSONObject airQualityJSON = getJSONObject(getJSONObject(header, "aqi"), "city");

        airQuality.aqi = getJSONString(airQualityJSON, "aqi");
        airQuality.co = getJSONString(airQualityJSON, "co");
        airQuality.no2 = getJSONString(airQualityJSON, "no2");
        airQuality.o3 = getJSONString(airQualityJSON, "o3");
        airQuality.pm10 = getJSONString(airQualityJSON, "pm10");
        airQuality.pm25 = getJSONString(airQualityJSON, "pm25");
        airQuality.qlty = getJSONString(airQualityJSON, "qlty");
        airQuality.so2 = getJSONString(airQualityJSON, "so2");

        return airQuality;
	}

	public List<DailyWeatherBean> getDailyWeathers() {
        List<DailyWeatherBean> dailyWeathers = new ArrayList<>();
        JSONObject header = getJSONObject(getJSONArray(jsonObject, HEADER), 0);
        JSONArray dailyWeatherJSON = getJSONArray(header, "daily_forecast");

        int index = 0;
        while (true) {
            JSONObject dailyWeatherJsonObject;

            dailyWeatherJsonObject = getJSONObject(dailyWeatherJSON, index);

            // avoid "index out of JSONArray range";
            if (dailyWeatherJsonObject == null) {
                break;
            }

            DailyWeatherBean dailyWeather = new DailyWeatherBean();
            String date = getJSONString(dailyWeatherJsonObject, "date");
            int tmpIndex = date.indexOf("-");
            if (tmpIndex != -1) {
                date = date.substring(tmpIndex + 1);
            }
            dailyWeather.date = date;


            dailyWeather.hum = getJSONString(dailyWeatherJsonObject, "hum");
            dailyWeather.pcpn = getJSONString(dailyWeatherJsonObject, "pcpn");
            dailyWeather.pop = getJSONString(dailyWeatherJsonObject, "pop");
            dailyWeather.pres = getJSONString(dailyWeatherJsonObject, "pres");
            dailyWeather.vis = getJSONString(dailyWeatherJsonObject, "vis");

            JSONObject astroJSONObject = getJSONObject(dailyWeatherJsonObject, "astro");
            dailyWeather.sr = getJSONString(astroJSONObject, "sr");
            dailyWeather.ss = getJSONString(astroJSONObject, "ss");

            JSONObject tmpJSONObject = getJSONObject(dailyWeatherJsonObject, "tmp");
            dailyWeather.maxTmp = getJSONString(tmpJSONObject, "max");
            dailyWeather.minTmp = getJSONString(tmpJSONObject, "min");

            JSONObject condJSONObject = getJSONObject(dailyWeatherJsonObject, "cond");
            dailyWeather.code_d = getJSONString(condJSONObject, "code_d");
            dailyWeather.code_n = getJSONString(condJSONObject, "code_n");
            dailyWeather.txt_d = getJSONString(condJSONObject, "txt_d");
            dailyWeather.txt_n = getJSONString(condJSONObject, "txt_n");

            JSONObject windJSONObject = getJSONObject(dailyWeatherJsonObject, "wind");
            dailyWeather.deg = getJSONString(windJSONObject, "deg");
            dailyWeather.dir = getJSONString(windJSONObject, "dir");
            dailyWeather.sc = getJSONString(windJSONObject, "sc");
            dailyWeather.spd = getJSONString(windJSONObject, "spd");

            dailyWeathers.add(dailyWeather);
            index++;
        }

        return dailyWeathers;
	}

	public SuggestionBean getSuggestion() {
		SuggestionBean suggestion = new SuggestionBean();
		JSONObject header = getJSONObject(getJSONArray(jsonObject, HEADER), 0);
		JSONObject suggestionJSON = getJSONObject(header, "suggestion");

		JSONObject summaryJSON = getJSONObject(suggestionJSON, "comf");
		suggestion.sumBrf = getJSONString(summaryJSON, "brf");
		suggestion.sumTxt = getJSONString(summaryJSON, "txt");

		JSONObject cwJSON = getJSONObject(suggestionJSON, "cw");
		suggestion.cwBrf = getJSONString(cwJSON, "brf");
		suggestion.cwTxt = getJSONString(cwJSON, "txt");

		JSONObject drsgJSON = getJSONObject(suggestionJSON, "drsg");
		suggestion.drsgBrf = getJSONString(drsgJSON, "brf");
		suggestion.drsgTxt = getJSONString(drsgJSON, "txt");

		JSONObject fluJSON = getJSONObject(suggestionJSON, "flu");
		suggestion.fluBrf = getJSONString(fluJSON, "brf");
		suggestion.fluTxt = getJSONString(fluJSON, "txt");

		JSONObject sportJSON = getJSONObject(suggestionJSON, "sport");
		suggestion.sportBrf = getJSONString(sportJSON, "brf");
		suggestion.sportTxt = getJSONString(sportJSON, "txt");

		JSONObject travJSON = getJSONObject(suggestionJSON, "trav");
		suggestion.travBrf = getJSONString(travJSON, "brf");
		suggestion.travTxt = getJSONString(travJSON, "txt");

		JSONObject uvJSON = getJSONObject(suggestionJSON, "uv");
		suggestion.uvBrf = getJSONString(uvJSON, "brf");
		suggestion.uvTxt = getJSONString(uvJSON, "txt");

		return suggestion;
	}

	private JSONArray getJSONArray(JSONObject json, String name) {
		if (json == null) {
			return null;
		}
		try {
			return json.getJSONArray(name);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private JSONObject getJSONObject(JSONObject json, String name) {
		if (json == null) {
			return null;
		}
		try {
			return json.getJSONObject(name);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private JSONObject getJSONObject(JSONArray json, int index) {
		if (json == null) {
			return null;
		}
		try {
			return json.getJSONObject(index);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getJSONString(JSONObject json, String name) {
		if (json == null) {
			return "";
		}
		try {
			return json.getString(name);
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}
}
