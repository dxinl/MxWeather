package com.mx.dxinl.mvp_mxweather.model.bean;

import android.graphics.Bitmap;

/**
 * Created by DengXinliang on 2016/1/26.
 */
public class DailyWeatherBean {
	public String date;	    // 当地日期
	public String astro;	// 天文数值
	public String sr;	    // 日出时间
	public String ss;	    // 日落时间
	public String tmp;	    // 温度
	public String maxTmp;	// 最该温度(摄氏度)
	public String minTmp;   // 最低温度(摄氏度)
	public String wind;	    // 风力状况
	public String spd;	    // 风速(Kmph)
	public String sc;	    // 风力等级
	public String deg;	    // 风向(角度)
	public String dir;	    // 风向(方向)
	public String cond;	    // 天气状况
	public String code_d;	// 白天天气代码
	public String txt_d;	// 白天天气描述
	public String code_n;	// 夜间天气代码
	public String txt_n;	// 夜间天气描述
	public String pcpn;	    // 降雨量(mm)
	public String pop;	    // 降水概率
	public String hum;	    // 湿度(%)
	public String pres;	    // 气压
	public String vis;	    // 能见度(km)
	public Bitmap dayBmp;      // 天气图片
	public Bitmap nightBmp;
}
