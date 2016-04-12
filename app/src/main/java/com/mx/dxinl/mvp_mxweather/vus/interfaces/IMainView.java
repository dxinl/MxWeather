package com.mx.dxinl.mvp_mxweather.vus.interfaces;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

/**
 * Created by DengXinliang on 2016/3/14.
 */
public interface IMainView {
	void showOthersContentPanel(boolean show);

	void hideSoftInputMethod();

	Menu getNavigationMenu();

	FragmentManager getIViewFragmentManager();

	Context getIViewContext();

	void refreshFragment();
}
