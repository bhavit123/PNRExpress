package com.bhavit.pnrexpress;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bhavit.pnrexpress.adapters.CustomListViewAdapterAvailability;
import com.bhavit.pnrexpress.adapters.CustomListViewAdapterTrainsList;
import com.bhavit.pnrexpress.model.Availability;
import com.bhavit.pnrexpress.model.Train;
import com.bhavit.pnrexpress.util.AppConstants;
import com.bhavit.pnrexpress.util.BaseAsyncTask;
import com.bhavit.pnrexpress.util.BaseAsyncTask.Method;

public class TrainsSearchResult extends BaseActivity{

	ArrayList<Train> trainsList;
	ListView searchResultTrains;
	String day;
	String month;
	String year;
	String selectedQuota;
	String from_to;
	TextView date, heading;
	String trainNumber;
	Train train;
	ArrayList<Availability> allAvails;
	Spinner classs, quota;
	ListView availabilities;
	Button getAvail;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trains_search_result);

		searchResultTrains = (ListView) findViewById(R.id.lv_trainsList);

		trainsList = new ArrayList<Train>();
		String result = getIntent().getExtras().getString("result");
		selectedQuota = getIntent().getExtras().getString("quota");
		day = getIntent().getExtras().getString("day");
		month = getIntent().getExtras().getString("month");
		year = getIntent().getExtras().getString("year");
		from_to = getIntent().getExtras().getString("from-to");


		heading = (TextView) findViewById(R.id.textView_heading);
		heading.setTypeface(BaseActivity.tf);
		heading.setText(from_to);

		date = (TextView) findViewById(R.id.textView1);
		date.setText(day + " " + getMonthForInt(Integer.parseInt(month) - 1));
		date.setTypeface(tf);

		try {

			JSONObject resultObj = new JSONObject(result);
			JSONArray trains = resultObj.getJSONArray("trains");

			for (int i = 0; i < trains.length(); i++) {

				JSONObject train = trains.getJSONObject(i);

				String trainNo = train.getString("number");
				String trainName = train.getString("name");
				// String fromStation =
				// elements.getElement(2).innerHTML().trim();
				String departureTime = train.getString("src_departure_time");

				// String toStation = elements.getElement(4).innerHTML().trim();
				String arrivalTime = train.getString("dest_arrival_time");
				// String travelTime =
				// elements.getElement(6).innerHTML().trim();

				JSONArray runsOnArr = train.getJSONArray("days");

				String runOn = "";
				for (int j = 0; j < runsOnArr.length(); j++) {

					runOn = runOn
							+ runsOnArr.getJSONObject(j).getString("runs");
				}

				int[] runOnColor = new int[7];

				if (runOn.charAt(0) == 'Y')
					runOnColor[0] = getResources().getColor(R.color.green);
				else
					runOnColor[0] = Color.parseColor("#FF0000");
				if (runOn.charAt(1) == 'Y')
					runOnColor[1] = getResources().getColor(R.color.green);
				else
					runOnColor[1] = Color.parseColor("#FF0000");
				if (runOn.charAt(2) == 'Y')
					runOnColor[2] = getResources().getColor(R.color.green);
				else
					runOnColor[2] = Color.parseColor("#FF0000");
				if (runOn.charAt(3) == 'Y')
					runOnColor[3] = getResources().getColor(R.color.green);
				else
					runOnColor[3] = Color.parseColor("#FF0000");
				if (runOn.charAt(4) == 'Y')
					runOnColor[4] = getResources().getColor(R.color.green);
				else
					runOnColor[4] = Color.parseColor("#FF0000");
				if (runOn.charAt(5) == 'Y')
					runOnColor[5] = getResources().getColor(R.color.green);
				else
					runOnColor[5] = Color.parseColor("#FF0000");
				if (runOn.charAt(6) == 'Y')
					runOnColor[6] = getResources().getColor(R.color.green);
				else
					runOnColor[6] = Color.parseColor("#FF0000");

				String classes = "";

				JSONArray classesArr = train.getJSONArray("classes");
				for (int j = 0; j < classesArr.length(); j++) {
					if (!classesArr.getJSONObject(j).getString("available").equals("N")) {

						classes = classes
								+ (!classes.equals("")?"|":"") +classesArr.getJSONObject(j).getString("class-code");
					}
				}

				String searchQuery = trainNo
						+ "|"
						+ from_to.split(" to ")[0]
								+ "|"
								+ from_to.split(" to ")[1]
										+ "|"
										+ day
										+ "-"
										+ (Integer.parseInt(month)<10?"0"+month:month)
										+ "-"
										+ year;

				trainsList.add(new Train(trainName, trainNo, "", "",
						timeformat24to12(departureTime),
						timeformat24to12(arrivalTime), "", runOn, runOnColor,
						classes, searchQuery));
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CustomListViewAdapterTrainsList adapter = new CustomListViewAdapterTrainsList(
				this, R.layout.custom_list_item_trains_list, trainsList);
		searchResultTrains.setAdapter(adapter);

		searchResultTrains
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				String url = "http://1-dot-pnrexpressservice.appspot.com/SeatAvailabilityService";

				ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = cm.getActiveNetworkInfo();
				// if no network is available networkInfo will be null
				// otherwise check if we are connected to internet
				if (networkInfo != null && networkInfo.isConnected()) {

					train = trainsList.get(position);
					String details = train.getSearchQuery();

					MyAsyncTask asynctask = new MyAsyncTask(TrainsSearchResult.this, Method.GET);
					asynctask.execute(url
							+ "?tnum="
							+ details.split("\\|")[0]
									+ "&from="
									+ details.split("\\|")[1]
											+ "&to="
											+ details.split("\\|")[2]
													+ "&date="
													+ details.split("\\|")[3]
															+ "&class="
															+ train.getClasses().split("\\|")[train
														                                        .getClasses().split("\\|").length - 1]
																	+ "&quota="
																	+ AppConstants
																	.getQuotaValue(TrainsSearchResult.this.selectedQuota), String.valueOf(position));


				} else {

					Toast.makeText(TrainsSearchResult.this,
							"No internet connection !!",
							Toast.LENGTH_LONG).show();

				}
			}
		});

	}

	public class MyAsyncTask extends BaseAsyncTask {

		public MyAsyncTask(Context context, Method method,
				boolean showLoadingDialog) {
			super(context, method, showLoadingDialog);
			// TODO Auto-generated constructor stub
		}
		public MyAsyncTask(Context context, Method method) {
			super(context, method);
			// TODO Auto-generated constructor stub
		}

		String fromSt;
		String toSt;
		String quotaName;
		ProgressDialog p;
		Dialog dialog;
		String param0, param1, param2, param3;
		TextView trainName, trainNo, from_to;		

		@Override
		protected String doInBackground(String... params) {

			param1 = params[1];

			return super.doInBackground(params);
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			if (result != null) {

				dialog = new Dialog(TrainsSearchResult.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog_seatavailability_result);
				dialog.setCanceledOnTouchOutside(false);

				allAvails = new ArrayList<Availability>();

				try {

					JSONObject resultObj = new JSONObject(result);
					JSONObject availabilityObj = resultObj.getJSONObject("availability");
					trainName = (TextView) dialog
							.findViewById(R.id.train_name);
					trainName.setText(availabilityObj.getString("train_name"));
					trainName.setTypeface(tf);
					trainName.setOnClickListener(new View.OnClickListener() {				
						@Override
						public void onClick(View v) {
							
							Intent i = new Intent(TrainsSearchResult.this, LocationActivity.class);
							i.putExtra("tnum", trainNumber);
							startActivity(i);
							
						}
					});

					trainNo = (TextView) dialog.findViewById(R.id.train_no);
					trainNo.setText("(" + availabilityObj.getString("train_number")
							+ ")");
					trainNo.setTypeface(tf);
					trainNumber = availabilityObj.getString("train_number");

					from_to = (TextView) dialog.findViewById(R.id.from_to);
					from_to.setText(availabilityObj.getJSONObject("from").getString("code")
							+ " to " + availabilityObj.getJSONObject("to").getString("code"));
					from_to.setTypeface(tf);

					quotaName = availabilityObj.getJSONObject("quota").getString("quota_name");

					JSONArray availabilitiesArr = availabilityObj.getJSONArray("availability_status");

					for (int i = 0; i < availabilitiesArr.length(); i++) {

						JSONObject availability = availabilitiesArr.getJSONObject(i);

						String doj = availability.getString("date");
						String status = availability.getString("status");

						allAvails.add(new Availability(doj, status));

					}

					classs = (Spinner) dialog.findViewById(R.id.classs);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							TrainsSearchResult.this,
							android.R.layout.simple_spinner_item, train
							.getClasses().split("\\|"));
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					classs.setAdapter(adapter);

					classs.setPrompt("Class");

					int pos = 0;
					for (int i = 0; i < train.getClasses().split("\\|").length; i++) {

						if (classs
								.getItemAtPosition(i)
								.toString()
								.equals(train.getClasses().split("\\|")[train
								                                        .getClasses().split("\\|").length - 1])) {
							pos = i;
						}
					}

					classs.setSelection(pos, true);

					String[] array = new String[2];
					AppConstants.getQuotas().keySet().toArray(array);
					quota = (Spinner) dialog.findViewById(R.id.quota);
					quota.setPrompt("Quota");
					adapter = new ArrayAdapter<String>(
							TrainsSearchResult.this,
							android.R.layout.simple_spinner_item, array);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					quota.setAdapter(adapter);

					pos = 0;
					for (int i = 0; i < 2; i++) {

						if (quota.getItemAtPosition(i).toString()
								.equals(TrainsSearchResult.this.selectedQuota)) {
							pos = i;
						}
					}
					quota.setSelection(pos);

					availabilities = (ListView) dialog
							.findViewById(R.id.listView1);
					CustomListViewAdapterAvailability customAdapter = new CustomListViewAdapterAvailability(
							TrainsSearchResult.this,
							R.layout.custom_list_item_availabilities,
							allAvails);
					availabilities.setAdapter(customAdapter);


				getAvail = (Button) dialog
						.findViewById(R.id.btn_getAvail);
				getAvail.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						String details = train.getSearchQuery();
						
						ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
						NetworkInfo networkInfo = cm.getActiveNetworkInfo();
						// if no network is available networkInfo will be null
						// otherwise check if we are connected to internet
						if (networkInfo != null && networkInfo.isConnected()) {

						LocalAsyncTaskSpinnersChanged task = new LocalAsyncTaskSpinnersChanged(TrainsSearchResult.this, Method.GET);
						task.execute("http://1-dot-pnrexpressservice.appspot.com/SeatAvailabilityService"
								+ "?tnum="
								+ details.split("\\|")[0]
										+ "&from="
										+ details.split("\\|")[1]
												+ "&to="
												+ details.split("\\|")[2]
														+ "&date="
														+ details.split("\\|")[3]
																+ "&class="
																+ classs.getSelectedItem().toString()
																+ "&quota="
																+ AppConstants.getQuotaValue(quota
																		.getSelectedItem().toString()));
						}else {

							Toast.makeText(context, "No internet connection !!",
									Toast.LENGTH_LONG).show();

						}
					}
				});

				Button close = (Button) dialog
						.findViewById(R.id.dialogclose);
				close.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				});

				Button share = (Button) dialog.findViewById(R.id.share);
				share.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// create the send intent
						Intent shareIntent = new Intent(
								android.content.Intent.ACTION_SEND);

						// set the type
						shareIntent.setType("text/plain");

						// add a subject
						shareIntent.putExtra(
								android.content.Intent.EXTRA_SUBJECT,
								"Availability for " + trainName.getText()
								+ "\n");

						// build the body of the message to be shared
						String shareMessage = trainName.getText() + ""
								+ trainNo.getText() + "" + "\n"
								+ from_to.getText() + "\nQuota: "
								+ quotaName + "\n\nAVAILABILITY ("
								+ classs.getSelectedItem().toString()
								+ ") : ";

						for (Availability a : allAvails) {
							shareMessage = shareMessage + "\n"
									+ a.getDate() + "  " + "("
									+ a.getAvailability() + ")";
						}

						// add the message
						shareIntent.putExtra(
								android.content.Intent.EXTRA_TEXT,
								shareMessage);

						// start the chooser for sharing
						TrainsSearchResult.this.startActivity(Intent
								.createChooser(shareIntent,
										"Share your PNR Status"));

					}
				});

				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(
								android.graphics.Color.TRANSPARENT));

				if (!TrainsSearchResult.this.isFinishing()) {
					dialog.show();
				}

				} catch (JSONException e) {
					showAlertDialog(TrainsSearchResult.this, "Error",
							"Some Error Occured. Please try again later.");
					e.printStackTrace();
				}
			} else {
				showAlertDialog(TrainsSearchResult.this, "Error",
						"Could not connect to server. Please try again");
			}
		}

	}

	public class LocalAsyncTaskSpinnersChanged extends BaseAsyncTask {

		public LocalAsyncTaskSpinnersChanged(Context context, Method method) {
			super(context, method);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			allAvails.clear();

				try{

					JSONObject resultObj = new JSONObject(result);

					JSONArray availabilitiesArr = resultObj.getJSONObject("availability").getJSONArray("availability_status");

					for (int i = 0; i < availabilitiesArr.length(); i++) {

						JSONObject availability = availabilitiesArr.getJSONObject(i);

						String doj = availability.getString("date");
						String status = availability.getString("status");

						allAvails.add(new Availability(doj, status));

					}

				} catch (Exception e) {
					Toast.makeText(TrainsSearchResult.this, "Nework error occured !",
							Toast.LENGTH_LONG).show();
				}

				CustomListViewAdapterAvailability adapter = new CustomListViewAdapterAvailability(
						TrainsSearchResult.this,
						R.layout.custom_list_item_availabilities,
						allAvails);
				availabilities.setAdapter(adapter);

			} 

		}


}
