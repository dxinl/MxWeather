package com.mx.dxinl.mvp_mxweather.vus.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by DengXinliang on 2016/3/13.
 */
public class HasOptionsMenuFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
}
