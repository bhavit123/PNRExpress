package com.bhavit.pnrexpress.fragment;

import java.util.Calendar;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bhavit.pnrexpress.BaseActivity;
import com.bhavit.pnrexpress.PnrActionsActivity;
import com.bhavit.pnrexpress.R;
import com.bhavit.pnrexpress.adapters.CustomListViewAdapterCheckedPnrs;
import com.bhavit.pnrexpress.dao.SqlHelper;
import com.bhavit.pnrexpress.service.BackgroundUpdateService;

public class PnrFragment extends BaseFragment {

	EditText pnr;
	ProgressBar bar;
	Dialog dialog;
	public static ListView l;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_pnr, container, false);
		BaseActivity.sqlHelper = new SqlHelper(getActivity().getApplicationContext());
		BaseActivity.tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GOTHIC.TTF");
		BaseActivity.tfBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gothicBold.TTF");

		/** Initialising app Constants***/
		BaseActivity.metrics = getActivity().getResources().getDisplayMetrics();
		BaseActivity.width = BaseActivity.metrics.widthPixels;
		BaseActivity.height = BaseActivity.metrics.heightPixels;
		/****************/

		setDrawerAndSettingsButtons(rootView);

		SharedPreferences pfs = getActivity().getSharedPreferences("pnrexpress", 0);
		boolean isMyServiceRunning = pfs.getBoolean("isServiceRunning", false);

		if (!isMyServiceRunning) { 

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, 5*60);

			Intent intent = new Intent(getActivity(), BackgroundUpdateService.class);

			PendingIntent pintent = PendingIntent
					.getService(getActivity(), 0, intent, 0);

			AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE); // for
			// 60*60*1000
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
					15 * 60 * 1000, pintent);

			pfs.edit().putBoolean("isServiceRunning", true).apply();
		}

		TextView recentlyViewed = (TextView) rootView.findViewById(R.id.textView1);
		recentlyViewed.setTypeface(BaseActivity.tf);
		TextView heading = (TextView) rootView.findViewById(R.id.textView_heading);
		heading.setTypeface(BaseActivity.tf);

		bar = (ProgressBar) rootView.findViewById(R.id.progressBar);

		pnr = (EditText) rootView.findViewById(R.id.editText_pnr);
		pnr.setTypeface(BaseActivity.tf);

		CustomListViewAdapterCheckedPnrs adapter = new CustomListViewAdapterCheckedPnrs(
				getActivity(), R.layout.custom_list_item_pnr_searched,
				BaseActivity.sqlHelper.getPnrDetail());

		l = (ListView) rootView.findViewById(R.id.list_checked_pnrs);

		l.setAdapter(adapter);
		l.setTextFilterEnabled(true);

		l.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				TextView pnrNo = (TextView) arg1.findViewById(R.id.pnr_num);

				Intent i = new Intent(getActivity(), PnrActionsActivity.class);
				i.putExtra("pnrNo", pnrNo.getText().toString().substring(5));
				startActivity(i);

			}
		});

		l.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0,
					final View arg1, int arg2, long arg3) {

				BaseActivity.showPnrDeleteDialog(getActivity(), "CONFIRM",
						"DO you really want to delete this PNR?", arg1, arg2, l);

				return true; // returning true prevents the onItemClick to run
				// after OnItemLongClick
			}
		});

		Button getPnrStatus = (Button) rootView.findViewById(R.id.button_getpnrstatus);
		getPnrStatus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) { 

				if (!pnr.getText().toString().equals("")) {

					if(pnr.getText().toString().length() == 10){

						String pnrNumber = pnr.getText().toString(); // getting the PNR number written by user

						new BaseActivity().getPnrStatus(pnrNumber, getActivity());

					} else {
						BaseActivity.showAlertDialog(getActivity(), "ALERT",
								"Enter correct pnr number.");
					}

				} else {
					BaseActivity.showAlertDialog(getActivity(), "ALERT",
							"Field cannot be null !!");
				}

			}
		});

		return rootView;
	}

	@Override
	public void onPause() {
		if (dialog != null) {
			dialog.dismiss();
		}
		super.onPause();
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}
