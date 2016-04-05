package com.mx.dxinl.mvp_mxweather.vus.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.presenters.impl.CitiesPresenterImpl;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.CitiesPresenter;
import com.mx.dxinl.mvp_mxweather.vus.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DengXinliang on 2016/1/5.
 */
public class CitiesListFragment extends Fragment implements View.OnClickListener {
	private CitiesPresenter presenter;
	private List<String> cities = null;
	private String type;

	private RecyclerView citiesList;
	private CitiesListAdapter adapter;

	public static CitiesListFragment newInstance(String type) {
		Bundle args = new Bundle();

		CitiesListFragment fragment = new CitiesListFragment();
		fragment.setArguments(args);
		fragment.type = type;
		fragment.presenter = new CitiesPresenterImpl();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_cities_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		citiesList = (RecyclerView) view.findViewById(R.id.cities_list);
		citiesList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
		cities = presenter.getCitiesList("", type);
		adapter = new CitiesListAdapter();
		citiesList.setAdapter(adapter);
	}

	public void setCitiesList(String query) {
		if (cities != null) {
			cities.clear();
		} else {
			cities = new ArrayList<>();
		}
		cities.addAll(presenter.getCitiesList(query, type));
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		int position = citiesList.getChildLayoutPosition(v);
		String cityName = cities.get(position);
		String cityNum = presenter.getCurrentCityNum(cityName, type);
		MainActivity activity = (MainActivity) getActivity();
		activity.setCurrentCity(cityName, cityNum, type);
		activity.onBackPressed();
		activity.refreshFragment();
	}

	private final class CitiesListAdapter extends RecyclerView.Adapter<CitiesListHolder> {

		@Override
		public CitiesListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new CitiesListHolder(
					LayoutInflater.from(CitiesListFragment.this.getContext())
							.inflate(R.layout.item_city, parent, false));
		}

		@Override
		public void onBindViewHolder(CitiesListHolder holder, int position) {
			holder.cityName.setText(cities.get(position));
		}

		@Override
		public int getItemCount() {
			return cities.size();
		}
	}

	private final class CitiesListHolder extends RecyclerView.ViewHolder {
		TextView cityName;

		public CitiesListHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(CitiesListFragment.this);
			cityName = (TextView) itemView.findViewById(R.id.city_name);
		}
	}
}
