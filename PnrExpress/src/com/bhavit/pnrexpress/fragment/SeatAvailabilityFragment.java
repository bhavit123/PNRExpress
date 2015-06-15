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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bhavit.pnrexpress.BaseActivity;
import com.bhavit.pnrexpress.R;
import com.bhavit.pnrexpress.TrainsSearchResult;
import com.bhavit.pnrexpress.util.AppConstants;

public class SeatAvailabilityFragment extends BaseFragment implements
		TextWatcher {

	DatePicker date;
	Spinner quota;
	AutoCompleteTextView station1, station2;
	Button calendar;
	EditText dateEd;

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

		final ImageView arrow = (ImageView) rootView
				.findViewById(R.id.imageView2);

		arrow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				arrow.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), R.anim.rotate));

				String temp = station2.getText().toString();

				station2.setText(station1.getText().toString());
				station2.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), R.anim.right_to_left));

				station1.setText(temp);
				station1.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), R.anim.left_to_right));

			}
		});

		ArrayList<String> list = new ArrayList<String>();
		HashMap<String, String> map = new HashMap<String, String>();

		JSONObject stations;
		try {
			stations = new JSONObject(loadJSONFromAsset());
			Iterator<String> names = stations.keys();

			while (names.hasNext()) {
				String stationCode = (String) names.next();
				JSONObject stationDetails = stations.getJSONObject(stationCode);
				String stationName = stationDetails.getString("name");
				list.add(stationCode);
				list.add(stationName);
				map.put(stationCode, stationName);

			}

			// System.out.println(names);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] array = new String[10];
		AppConstants.getQuotas().keySet().toArray(array);
		quota = (Spinner) rootView.findViewById(R.id.spinner2);
		quota.setPrompt("Quota");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, array);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		quota.setAdapter(adapter);

		station1 = (AutoCompleteTextView) rootView
				.findViewById(R.id.autoCompleteTextView1);
		station1.addTextChangedListener(this);
		station1.setTypeface(BaseActivity.tf);
		station1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				Toast.makeText(getActivity(), ((TextView)arg1).getText(), Toast.LENGTH_LONG).show();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		station1.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, list));

		station2 = (AutoCompleteTextView) rootView
				.findViewById(R.id.autoCompleteTextView2);
		station2.addTextChangedListener(this);
		station2.setTypeface(BaseActivity.tf);
		station2.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, list));

		Button go = (Button) rootView.findViewById(R.id.button2);
		go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (date != null) {
					if (!station1.getText().toString()
							.equals(station2.getText().toString())) {
						String url2 = "http://pnrbuddy.com/hauth/seatavailtrains";

						String dateString = (date.getDayOfMonth() < 10 ? "0"
								+ date.getDayOfMonth() : date.getDayOfMonth())
								+ "/"
								+ (date.getMonth() + 1 < 10 ? "0"
										+ (date.getMonth() + 1) : date
										.getMonth() + 1) + "/" + date.getYear();

						ConnectivityManager cm = (ConnectivityManager) getActivity()
								.getSystemService(Context.CONNECTIVITY_SERVICE);
						NetworkInfo networkInfo = cm.getActiveNetworkInfo();
						// if no network is available networkInfo will be null
						// otherwise check if we are connected to internet
						if (networkInfo != null && networkInfo.isConnected()) {

							MyAsyncTask asynctask = new MyAsyncTask();
							asynctask.execute(url2, station1.getText()
									.toString(), station2.getText().toString(),
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
						Toast.makeText(getActivity(), "Please select date.",
								Toast.LENGTH_LONG).show();
					}
				} else {

					Toast.makeText(getActivity(), "Please select date.",
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
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(params[0]);
				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				StringEntity entity;

				entity = new StringEntity("from=" + params[1] + "&to="
						+ params[2] + "&date=" + params[3] + "&class=ZZ"
						+ "&quota=" + params[4], HTTP.UTF_8);

				httpPost.setEntity(entity);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				result = httpClient.execute(httpPost, responseHandler);

			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void res) {

			System.out.println(result);

			p.dismiss();

			if (!result
					.contains("Unable to get availability due to network error")) {

				Intent i = new Intent(getActivity(), TrainsSearchResult.class);
				i.putExtra("result", result);
				i.putExtra("day",
						date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth()
								: "" + date.getDayOfMonth());
				i.putExtra("month", String.valueOf(date.getMonth() + 1));
				i.putExtra("quota", quota.getSelectedItem().toString());
				i.putExtra("from-to", station1.getText().toString() + " to "
						+ station2.getText().toString());
				startActivity(i);

			} else {

				BaseActivity.showAlertDialog(getActivity(), "Error",
						"Network Error Occured. Please try again");
			}
			super.onPostExecute(res);
		}

	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	public String loadJSONFromAsset() {
		String json = null;
		try {

			InputStream is = getActivity().getAssets().open(
					"train_stations.json");

			int size = is.available();

			byte[] buffer = new byte[size];

			is.read(buffer);

			is.close();

			json = new String(buffer, "UTF-8");

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;

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

}
