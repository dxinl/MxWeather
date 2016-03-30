package com.mx.dxinl.mvp_mxweather.vus;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.presenters.impl.MainPresenterImpl;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.MainPresenter;
import com.mx.dxinl.mvp_mxweather.vus.fragment.WeatherFragment;
import com.mx.dxinl.mvp_mxweather.vus.interfaces.IMainView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IMainView {
	private final int PERMISSION_REQUEST_CODE = 48;
	private MainPresenter presenter;
	private WeatherFragment weatherFragment;
	private NavigationView navigationView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (Build.VERSION.SDK_INT >= 23) {
			int checkReadExternalPermission = ContextCompat.checkSelfPermission(
					getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
			int checkWriteExternalPermission = ContextCompat.checkSelfPermission(
					getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
			List<String> permissionsRequest = new ArrayList<>();
			if (checkReadExternalPermission == PackageManager.PERMISSION_DENIED) {
				permissionsRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
			}

			if (checkWriteExternalPermission == PackageManager.PERMISSION_DENIED) {
				permissionsRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			}

			if (permissionsRequest.size() != 0) {
				ActivityCompat.requestPermissions(this,
						permissionsRequest.toArray(new String[permissionsRequest.size()]),
						PERMISSION_REQUEST_CODE);
			}
		}
		initActivity();
	}

	private void initActivity() {
		presenter = new MainPresenterImpl(this, this);
		presenter.processDB();

		navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		presenter.getCurrentCityInfo();
		presenter.initNavigationMenu();

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		weatherFragment = new WeatherFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.content_panel, weatherFragment).commit();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_CODE:
				String tips = getString(R.string.write_external_storage_permission_denied);
				if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
					if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
						tips = getString(R.string.read_external_storage_permission_denied);
					}
					Toast.makeText(MainActivity.this, tips, Toast.LENGTH_LONG).show();
				} else if (grantResults.length == 2) {
					if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
						tips = getString(R.string.read_and_write_external_storage_permission_denied);
					} else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
						tips = getString(R.string.read_external_storage_permission_denied);
					} else if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
						tips = getString(R.string.write_external_storage_permission_denied);
					} else {
						tips = "";
					}

					if (tips.length() > 0) {
						Toast.makeText(MainActivity.this, tips, Toast.LENGTH_SHORT).show();
					}
				}

				weatherFragment.onRefresh();
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		weatherFragment.cancelGetWeatherTask();
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		presenter.onNavigationItemSelected(item);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public void hideSoftInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(navigationView.getWindowToken(), 0);
	}

	@Override
	public Menu getNavigationMenu() {
		return navigationView.getMenu();
	}

	@Override
	public FragmentManager getIViewFragmentManager() {
		return getSupportFragmentManager();
	}

	@Override
	public void refreshFragment() {
		weatherFragment.setRefreshing(true);
		weatherFragment.onRefresh();
	}

	@Override
	public void cancelGetWeatherTask() {
		weatherFragment.cancelGetWeatherTask();
	}

	public String getCurrentCityNum() {
		return presenter.getCurrentCityNum();
	}

	public String getCurrentCityName() {
		return presenter.getCurrentCityName();
	}

	public String getCurrentCityType() {
		return presenter.getCurrentCityType();
	}

	public void setCurrentCity(String cityName, String cityNum, String cityType) {
		presenter.setCurrentCity(cityName, cityNum, cityType);
	}

	public void invalidateNavigationMenu() {
		presenter.initNavigationMenu();
	}
}
