package com.mx.dxinl.mvp_mxweather.vus.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.presenters.impl.CitiesManagerPresenterImpl;
import com.mx.dxinl.mvp_mxweather.presenters.interfaces.CitiesManagerPresenter;
import com.mx.dxinl.mvp_mxweather.vus.MainActivity;
import com.mx.dxinl.mvp_mxweather.vus.base.HasOptionsMenuFragment;
import com.mx.dxinl.mvp_mxweather.vus.interfaces.ICitiesManagerView;

/**
 * Created by DengXinliang on 2016/3/14.
 */
public class CitiesManagerFragment extends HasOptionsMenuFragment implements ICitiesManagerView, View.OnClickListener {
	private RecyclerView citiesListView;
	private CitiesManagerPresenter presenter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_cities_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getActivity().setTitle(getString(R.string.manage_city));
		presenter = new CitiesManagerPresenterImpl(getActivity(), this);

		citiesListView = (RecyclerView) view.findViewById(R.id.cities_list);
		citiesListView.setLayoutManager(new LinearLayoutManager(getContext()));


		ListAdapter adapter = new ListAdapter();
		citiesListView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		int position = citiesListView.getChildLayoutPosition(v);
		presenter.onClickCity(position);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_cities_manager, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		presenter.onOptionsItemSelected(item);
		((MainActivity) getActivity()).invalidateNavigationMenu();
		return true;
	}

	@Override
	public void notifyDataSetChanged() {
		citiesListView.getAdapter().notifyDataSetChanged();
	}

	private final class ListAdapter extends RecyclerView.Adapter<ListHolder> {

		@Override
		public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new ListHolder(LayoutInflater.from(
					CitiesManagerFragment.this.getActivity()).inflate(R.layout.item_manager_cities, parent, false));
		}

		@Override
		public void onBindViewHolder(ListHolder holder, int position) {
			Pair<String, String> cityInfo = presenter.getCitiesListData(false).get(position);
			String text = cityInfo.first + " - " + cityInfo.second;
			holder.cityInfo.setText(text);

			if (presenter.getSelectedCities().contains(cityInfo)) {
				holder.itemView.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
				holder.cityInfo.setTextColor(getResources().getColor(android.R.color.white));
			} else {
				holder.itemView.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
				holder.cityInfo.setTextColor(getResources().getColor(android.R.color.black));
			}
		}

		@Override
		public int getItemCount() {
			return presenter.getCitiesListData(false).size();
		}
	}

	private final class ListHolder extends RecyclerView.ViewHolder {
		TextView cityInfo;
		View itemView;

		public ListHolder(View itemView) {
			super(itemView);
			this.itemView = itemView;
			itemView.setOnClickListener(CitiesManagerFragment.this);
			cityInfo = (TextView) itemView.findViewById(R.id.city_name);
		}
	}
}
