package com.mx.dxinl.mvp_mxweather.vus.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mx.dxinl.mvp_mxweather.R;
import com.mx.dxinl.mvp_mxweather.model.bean.SuggestionBean;

/**
 * Created by dxinl on 2016/3/17.
 */
public class SuggestionFragment extends Fragment {
	private final int FIRST_ID = 0x0307;
	private SuggestionBean suggestion;

	public static SuggestionFragment newInstance(SuggestionBean suggestion) {
		Bundle args = new Bundle();
		SuggestionFragment fragment = new SuggestionFragment();
		fragment.setArguments(args);
		fragment.suggestion = suggestion;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_suggestion, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (suggestion == null) {
			return;
		}

		LinearLayout firstLine = (LinearLayout) view.findViewById(R.id.first_line);
		LinearLayout secondLine = (LinearLayout) view.findViewById(R.id.second_line);
		LinearLayout thirdLine = (LinearLayout) view.findViewById(R.id.third_line);
		LinearLayout fourthLine = (LinearLayout) view.findViewById(R.id.fourth_line);

		LinearLayout layouts[] = new LinearLayout[]{firstLine, secondLine, thirdLine, fourthLine};
		int titleIds[] = new int[]{R.string.suggestion_comfort, R.string.suggestion_dress, R.string.suggestion_fluent,
				R.string.suggestion_sport, R.string.suggestion_travel, R.string.suggestion_uv, R.string.suggestion_cw};
		String contents[] = new String[]{suggestion.sumTxt, suggestion.drsgBrf, suggestion.fluBrf,
				suggestion.sportBrf, suggestion.travBrf, suggestion.uvBrf, suggestion.cwBrf};
		int drawableIds[] = new int[]{R.drawable.weather, R.drawable.clothes, R.drawable.doctor,
				R.drawable.football, R.drawable.airplane, R.drawable.umbrella, R.drawable.car};

		for (int i = 0; i < layouts.length; i++) {
			int j = 0;
			if (i == 0) {
				j = 1;
			}
			layouts[i].setWeightSum(2 - j);
			for (; j < 2; j++) {
				RelativeLayout childLayout = new RelativeLayout(getContext());
				ImageView suggestionImg = new ImageView(getContext());
				suggestionImg.setId(FIRST_ID + 6 * i + j * 3);
				RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				childLayout.addView(suggestionImg, imgParams);
				suggestionImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), drawableIds[i * 2 + j - 1]));

				TextView suggestionTitle = new TextView(getContext());
				suggestionTitle.setId(FIRST_ID + 6 * i + 1 + j * 3);
				suggestionTitle.setTextColor(getResources().getColor(R.color.light_green_700));
				suggestionTitle.setTextSize(16);
				RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				titleParams.addRule(RelativeLayout.RIGHT_OF, suggestionImg.getId());
				titleParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.middle_margin);
				childLayout.addView(suggestionTitle, titleParams);
				suggestionTitle.setText(titleIds[i * 2 + j - 1]);

				TextView suggestionContent = new TextView(getContext());
				suggestionContent.setId(FIRST_ID + 6 * i + 2 + j * 3);
				suggestionContent.setTextColor(getResources().getColor(R.color.light_green_700));
				suggestionContent.setTextSize(14);
				RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				contentParams.addRule(RelativeLayout.RIGHT_OF, suggestionImg.getId());
				contentParams.addRule(RelativeLayout.BELOW, suggestionTitle.getId());
				contentParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
				childLayout.addView(suggestionContent, contentParams);
				suggestionContent.setText(contents[i * 2 + j - 1]);

				layouts[i].addView(childLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
			}
		}
	}
}
