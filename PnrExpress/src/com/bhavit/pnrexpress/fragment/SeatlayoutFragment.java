package com.bhavit.pnrexpress.fragment;

import com.bhavit.pnrexpress.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class SeatlayoutFragment extends BaseFragment {

	public SeatlayoutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_seat_layout,
				container, false);

		setDrawerAndSettingsButtons(rootView);

		WebView seatLayout = (WebView) rootView
				.findViewById(R.id.webView_seatlayout);

		seatLayout.getSettings().setJavaScriptEnabled(true);

		seatLayout.loadUrl("file:///android_asset/Seat Layout.html");

		return rootView;
	}
}
