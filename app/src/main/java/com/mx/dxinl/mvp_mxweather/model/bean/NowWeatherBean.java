package com.mx.dxinl.mvp_mxweather.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DengXinliang on 2016/1/7.
 */
public class NowWeatherBean implements Parcelable {
	public String code; // 天气代码
	public String txt;  // 天气描述
	public String tmp;	// 当前温度(摄氏度)
	public String fl;	// 体感温度
	public String spd;	// 风速(Kmph)
	public String sc;	// 风力等级
	public String deg;	// 风向(角度)
	public String dir;	// 风向(方向)
	public String pcpn;	// 降雨量(mm)
	public String hum;	// 湿度(%)
	public String pres;	// 气压
	public String vis;	// 能见度(km)

	public NowWeatherBean() {}

	private NowWeatherBean(Parcel source) {
		code = source.readString();
		txt = source.readString();
		tmp = source.readString();
		fl = source.readString();
		spd = source.readString();
		sc = source.readString();
		deg = source.readString();
		dir = source.readString();
		pcpn = source.readString();
		hum = source.readString();
		pres = source.readString();
		vis = source.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(code);
		dest.writeString(txt);
		dest.writeString(tmp);
		dest.writeString(fl);
		dest.writeString(spd);
		dest.writeString(sc);
		dest.writeString(deg);
		dest.writeString(dir);
		dest.writeString(pcpn);
		dest.writeString(hum);
		dest.writeString(pres);
		dest.writeString(vis);
	}

	public static final Parcelable.Creator<NowWeatherBean> CREATOR = new Parcelable.Creator<NowWeatherBean>() {

		@Override
		public NowWeatherBean createFromParcel(Parcel source) {
			return new NowWeatherBean(source);
		}

		@Override
		public NowWeatherBean[] newArray(int size) {
			return new NowWeatherBean[0];
		}
	};
}
