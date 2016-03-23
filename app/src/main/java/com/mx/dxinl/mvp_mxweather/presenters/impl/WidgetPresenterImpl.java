package com.mx.dxinl.mvp_mxweather.presenters.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.ImageView;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.WidgetPresenter;
import com.mx.dxinl.mvp_mxweather.utils.ImageLoader;

/**
 * Created by DengXinliang on 2016/3/23.
 */
public class WidgetPresenterImpl implements WidgetPresenter {
	@Override
	public Bitmap getImageBitmap(Context context, String code) {
		Bitmap bitmap = ImageLoader.get().getImageBitmap(code);
		if (bitmap == null) {
			return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
		}
		return bitmap;
	}

	@Override
	public Bitmap drawWeatherIconForWidget(Context context, Bitmap bitmap) {
		try {
			Bitmap bkgBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.transparent_circle_bkg_white);
			int width = bkgBmp.getWidth();
			int height = bkgBmp.getHeight();
			Bitmap tmpBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(tmpBmp);

			canvas.drawBitmap(bkgBmp, 0, 0, null);
			canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
					new Rect(width / 6, height / 6, width * 5 / 6, height * 5 / 6), new Paint(Paint.ANTI_ALIAS_FLAG));
			return tmpBmp;
		} catch (Exception e) {
			e.printStackTrace();
			return bitmap;
		}
	}

}
