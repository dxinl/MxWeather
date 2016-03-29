package com.mx.dxinl.mvp_mxweather.vus.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.vus.base.HasOptionsMenuFragment;
import com.mx.dxinl.mvp_mxweather.vus.view.SlidingTabLayout;

/**
 * Created by DengXinliang on 2016/3/13.
 */
public class CitiesTabFragment extends HasOptionsMenuFragment {
	private CitiesListFragment[] fragments;
	private PagerAdapter adapter;
	private ViewPager pager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_cities_tab, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getActivity().setTitle(getString(R.string.add_city));

		pager = (ViewPager) view.findViewById(R.id.pager);

		CitiesListFragment citiesFragment = CitiesListFragment.newInstance("weather");
		CitiesListFragment attractionsFragment = CitiesListFragment.newInstance("attractions");

		fragments = new CitiesListFragment[] {citiesFragment, attractionsFragment};
		final String[] titles = new String[] {getString(R.string.cities), getString(R.string.attractions)};

		adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return fragments[position];
			}

			@Override
			public int getCount() {
				return fragments.length;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return titles[position];
			}
		};
		pager.setAdapter(adapter);

		SlidingTabLayout tabLayout = (SlidingTabLayout) view.findViewById(R.id.pager_title_strip);
		tabLayout.setViewPager(pager);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_cities_tab, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);
		if (searchItem == null) {
			return;
		}

		final SearchView searchView = (SearchView) searchItem.getActionView();
		if (searchView != null) {
			searchView.setQueryHint(getString(R.string.search_hint));
			searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
				@Override
				public boolean onQueryTextSubmit(String query) {
					setCitiesList(query);
					return true;
				}

				@Override
				public boolean onQueryTextChange(String newText) {
					setCitiesList(newText);
					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
					return true;
				}
			});
		}
	}

	private void setCitiesList(String text) {
		fragments[pager.getCurrentItem()].setCitiesList(text);
	}
}
