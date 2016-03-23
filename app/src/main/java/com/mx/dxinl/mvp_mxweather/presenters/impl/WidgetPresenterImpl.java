package com.mx.dxinl.mvp_mxweather.presenters.impl;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.mx.dxinl.mvp_mxweather.presenters.interfaces.WidgetPresenter;
import com.mx.dxinl.mvp_mxweather.utils.ImageLoader;

/**
 * Created by DengXinliang on 2016/3/23.
 */
public class WidgetPresenterImpl implements WidgetPresenter {
	@Override
	public Bitmap getImageBitmap(String code) {
		return ImageLoader.get().getImageBitmap(code);
	}
}
