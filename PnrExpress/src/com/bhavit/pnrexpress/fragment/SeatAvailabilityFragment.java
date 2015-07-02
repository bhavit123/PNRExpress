package com.bhavit.pnrexpress.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bhavit.pnrexpress.BaseActivity;
import com.bhavit.pnrexpress.R;
import com.bhavit.pnrexpress.TrainsSearchResult;
import com.bhavit.pnrexpress.adapters.CustomListAdapterSearchHistory;
import com.bhavit.pnrexpress.model.SearchHistory;
import com.bhavit.pnrexpress.util.AppConstants;
import com.bhavit.pnrexpress.util.AppHelper;
import com.bhavit.pnrexpress.util.RestClient;

public class SeatAvailabilityFragment extends BaseFragment implements
OnClickListener {

	DatePicker date;
	Spinner quota;
	LinearLayout station1, station2;
	TextView stationName1, stationCode1, stationName2, stationCode2;
	Button calendar, searchHistory;
	EditText dateEd;
	ArrayList<String> list;

	public SeatAvailabilityFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_seat_availability,
				container, false);

		setDrawerAndSettingsButtons(rootView);

		TextView heading = (TextView) rootView
				.findViewById(R.id.textView_heading);
		heading.setTypeface(BaseActivity.tf);

		searchHistory = (Button) rootView.findViewById(R.id.btn_searchHistory);
		searchHistory.setTypeface(BaseActivity.tf);
		searchHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final Dialog dialog;
				dialog = new Dialog(getActivity());
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog_search_history);

				TextView heading = (TextView) dialog.findViewById(R.id.heading);
				heading.setTypeface(BaseActivity.tf);

				ListView searchList = (ListView) dialog
						.findViewById(R.id.listView1);
				CustomListAdapterSearchHistory adapter = new CustomListAdapterSearchHistory(
						getActivity(),
						R.layout.custom_list_item_search_history,
						BaseActivity.sqlHelper.getSearchHistory());
				searchList.setAdapter(adapter);

				searchList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						TextView fromStationName = (TextView) arg1
								.findViewById(R.id.textView4);
						TextView fromStationCode = (TextView) arg1
								.findViewById(R.id.textView5);
						TextView toStationName = (TextView) arg1
								.findViewById(R.id.textView6);
						TextView toStationCode = (TextView) arg1
								.findViewById(R.id.textView7);

						stationName1.setText(fromStationName.getText());
						stationCode1.setText(fromStationCode.getText());
						stationCode1.setVisibility(View.VISIBLE);
						stationName2.setText(toStationName.getText());
						stationCode2.setText(toStationCode.getText());
						stationCode2.setVisibility(View.VISIBLE);

						dialog.dismiss();

					}

				});

				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));

				if (!getActivity().isFinishing()) {
					dialog.show();
				}

			}
		});

		final ImageView arrow = (ImageView) rootView
				.findViewById(R.id.imageView2);
		arrow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				arrow.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), R.anim.rotate));

				String tempName = stationName2.getText().toString();
				String tempCode = stationCode2.getText().toString();

				stationName2.setText(stationName1.getText().toString());
				stationCode2.setText(stationCode1.getText().toString());
				station2.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), R.anim.right_to_left));

				stationName1.setText(tempName);
				stationCode1.setText(tempCode);
				station1.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), R.anim.left_to_right));

			}
		});

		list = new ArrayList<String>();

		JSONObject stations;
		try {
			stations = new JSONObject(AppHelper.loadJSONFromAsset(getActivity(), "train_stations.json"));
			Iterator<String> names = stations.keys();

			while (names.hasNext()) {
				String stationCode = (String) names.next();
				JSONObject stationDetails = stations.getJSONObject(stationCode);
				String stationName = stationDetails.getString("name");
				list.add(stationName + "- " + stationCode);

			}

			// System.out.println(names);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] array = new String[2];
		AppConstants.getQuotas().keySet().toArray(array);
		quota = (Spinner) rootView.findViewById(R.id.spinner2);
		quota.setPrompt("Quota");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, array);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		quota.setAdapter(adapter);
		quota.setSelection(1);

		station1 = (LinearLayout) rootView
				.findViewById(R.id.autoCompleteTextView1);
		station1.setOnClickListener(this);

		stationName1 = (TextView) rootView.findViewById(R.id.textView1);
		stationCode1 = (TextView) rootView.findViewById(R.id.textView2);

		stationCode1.setTypeface(BaseActivity.tf);
		stationName1.setTypeface(BaseActivity.tf);

		station2 = (LinearLayout) rootView
				.findViewById(R.id.autoCompleteTextView2);
		station2.setOnClickListener(this);

		stationName2 = (TextView) rootView.findViewById(R.id.textView3);
		stationCode2 = (TextView) rootView.findViewById(R.id.textView4);

		stationCode2.setTypeface(BaseActivity.tf);
		stationName2.setTypeface(BaseActivity.tf);

		Button go = (Button) rootView.findViewById(R.id.button2);
		go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!stationCode1.getText().toString().equals("")
						&& !stationCode1.getText().toString().equals("")) {
					if (date != null) {
						if (!stationCode1.getText().toString()
								.equals(stationCode2.getText().toString())) {
							String url2 = "http://pnrbuddy.com/hauth/seatavailtrains";

							String dateString = (date.getDayOfMonth() < 10 ? "0"
									+ date.getDayOfMonth()
									: date.getDayOfMonth())
									+ "/"
									+ (date.getMonth() + 1 < 10 ? "0"
											+ (date.getMonth() + 1) : date
											.getMonth() + 1)
											+ "/"
											+ date.getYear();

							ConnectivityManager cm = (ConnectivityManager) getActivity()
									.getSystemService(
											Context.CONNECTIVITY_SERVICE);
							NetworkInfo networkInfo = cm.getActiveNetworkInfo();
							// if no network is available networkInfo will be
							// null
							// otherwise check if we are connected to internet
							if (networkInfo != null
									&& networkInfo.isConnected()) {

								MyAsyncTask asynctask = new MyAsyncTask();
								asynctask.execute(url2, stationCode1.getText()
										.toString().trim(), stationCode2
										.getText().toString().trim(),
										dateString, AppConstants
										.getQuotaValue(quota
												.getSelectedItem()
												.toString()));
							} else {
								Toast.makeText(getActivity(),
										"No internet connection !!",
										Toast.LENGTH_LONG).show();

							}
						} else {
							Toast.makeText(getActivity(),
									"Please select different stations.",
									Toast.LENGTH_LONG).show();
						}
					} else {

						Toast.makeText(getActivity(), "Please select date.",
								Toast.LENGTH_LONG).show();

					}
				} else {
					Toast.makeText(getActivity(), "Please select station.",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		date = (DatePicker) rootView.findViewById(R.id.datePicker1);

		return rootView;
	}

	public class MyAsyncTask extends AsyncTask<String, Void, Void> {

		String result;
		String fromSt;
		String toSt;
		ProgressDialog p;

		@Override
		protected void onPreExecute() {

			p = new ProgressDialog(getActivity());
			p.show();
			p.setContentView(R.layout.custom_progressdialog);
			p.setCancelable(false);
			p.setCanceledOnTouchOutside(false);

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {

			try {

				RestClient client = new RestClient(params[0]);
				client.addHeader("Content-Type",
						"application/x-www-form-urlencoded");
				client.addStringBody("from=" + params[1] + "&to=" + params[2]
						+ "&date=" + params[3] + "&class=ZZ" + "&quota="
						+ params[4]);
				result = client.executePost();

			} catch (Exception e) {

				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void res) {

			System.out.println(result);

			p.dismiss();

			if (result != null) {
				if (!result
						.contains("Unable to get availability due to network error")) {

					boolean alreadyExist = BaseActivity.sqlHelper.searchHistoryExist(
							stationName1.getText().toString(), stationCode1.getText().toString(),
							stationName2.getText().toString(), stationCode2.getText().toString());

					if(!alreadyExist){
						BaseActivity.sqlHelper
						.insertInSearchHistory(new SearchHistory(
								stationName1.getText() + "- "
										+ stationCode1.getText(),
										stationName2.getText() + "- "
												+ stationCode2.getText()));
					}
					Intent i = new Intent(getActivity(),
							TrainsSearchResult.class);
					i.putExtra("result", result);
					i.putExtra(
							"day",
							date.getDayOfMonth() < 10 ? "0"
									+ date.getDayOfMonth() : ""
									+ date.getDayOfMonth());
					i.putExtra("month", String.valueOf(date.getMonth() + 1));
					i.putExtra("quota", quota.getSelectedItem().toString());
					i.putExtra("from-to", stationCode1.getText().toString()
							+ " to " + stationCode2.getText().toString());
					startActivity(i);

				} else {

					BaseActivity.showAlertDialog(getActivity(), "Sorry!",
							"Unable to get Availability.");
				}

			} else {
				BaseActivity.showAlertDialog(getActivity(), "Error!",
						"Could not connect to server. Please try again");
			}
			super.onPostExecute(res);
		}

	}

	

	public class DatePickerFragment extends DialogFragment implements
	DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {

			dateEd.setText(day + "/" + month + 1 + "/" + year);

			date = view;
		}
	}

	@Override
	public void onClick(final View v) {
				
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, list);

		AppHelper.openAutoCompleteInputDialog(getActivity(), "Select Station", "Enter station name or code", adapter, new OnClickListener() {

			@Override
			public void onClick(View view) {

				if (AppHelper.inputDialogAutoCompletetv.getText().toString().contains("-")){
					if (!AppHelper.inputDialogAutoCompletetv.getText().toString().equals("")) {
						if (v.getId() == R.id.autoCompleteTextView1) {
							stationName1.setText(AppHelper.inputDialogAutoCompletetv.getText().toString()
									.split("-")[0]);
							stationCode1.setText(AppHelper.inputDialogAutoCompletetv.getText().toString()
									.split("-")[1].trim());
							stationCode1.setVisibility(View.VISIBLE);

						} else {
							stationName2.setText(AppHelper.inputDialogAutoCompletetv.getText().toString()
									.split("-")[0]);
							stationCode2.setText(AppHelper.inputDialogAutoCompletetv.getText().toString()
									.split("-")[1].trim());
							stationCode2.setVisibility(View.VISIBLE);
						}
					}
				}
				AppHelper.inputDialogAutoCompletetv.clearFocus();

				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(AppHelper.inputDialogAutoCompletetv.getWindowToken(),
						InputMethodManager.HIDE_IMPLICIT_ONLY);

				AppHelper.autoCompleteInputDialog.dismiss();

			}
		});
	}

}
