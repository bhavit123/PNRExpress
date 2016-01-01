package com.bhavit.pnrexpress;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bhavit.pnrexpress.adapters.CustomListViewAdapterCheckedPnrs;
import com.bhavit.pnrexpress.adapters.CustomListViewAdapterPassengers;
import com.bhavit.pnrexpress.dao.SqlHelper;
import com.bhavit.pnrexpress.fragment.PnrFragment;
import com.bhavit.pnrexpress.model.LastStatus;
import com.bhavit.pnrexpress.model.Passenger;
import com.bhavit.pnrexpress.model.PnrDetail;
import com.bhavit.pnrexpress.model.Station;
import com.bhavit.pnrexpress.util.AppHelper;
import com.bhavit.pnrexpress.util.BaseAsyncTask;
import com.bhavit.pnrexpress.util.BaseAsyncTask.Method;
import com.bhavit.pnrexpress.util.RestClient;

public class BaseActivity extends Activity {

	public static String alert = "";
	static public String userId;
	public static Typeface tf;
	public static Typeface tfBold;
	public String deviceModel = android.os.Build.MODEL;
	public String deviceManufacturer = android.os.Build.MANUFACTURER;
	public String osVersion=Build.VERSION.RELEASE;
	String appVersion;
	public static DisplayMetrics metrics;
	public static int width;
	public static int height;
	public static SqlHelper sqlHelper;
	public Context context;

	public static void showAlertDialog(Context context, String titleText, String messageText){

		metrics = context.getResources().getDisplayMetrics();
		width = metrics.widthPixels;
		height = metrics.heightPixels;

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_alert);

		TextView title =  (TextView) dialog.findViewById(R.id.title);
		TextView message =  (TextView) dialog.findViewById(R.id.message);
		Button ok =  (Button) dialog.findViewById(R.id.button1);

		title.setText(titleText);
		title.setTypeface(tf);
		message.setText(messageText);
		message.setTypeface(tf);
		ok.setTypeface(tf);


		int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,  context.getResources().getDisplayMetrics());
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(width-px, LinearLayout.LayoutParams.WRAP_CONTENT);
		lParams.gravity = Gravity.CENTER_HORIZONTAL;
		ok.setLayoutParams(lParams);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(
						android.graphics.Color.TRANSPARENT));

		dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	public static void showAlertDialogTwoButtons(final Context context, String titleText, String messageText, OnClickListener listener){

		metrics = context.getResources().getDisplayMetrics();
		width = metrics.widthPixels;
		height = metrics.heightPixels;

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_alert_twobuttons);

		TextView title =  (TextView) dialog.findViewById(R.id.title);
		TextView message =  (TextView) dialog.findViewById(R.id.message);
		Button ok =  (Button) dialog.findViewById(R.id.button1);
		Button cancel = (Button) dialog.findViewById(R.id.button2);


		title.setText(titleText);
		title.setTypeface(tf);
		message.setText(messageText);
		message.setTypeface(tf);
		ok.setTypeface(tf);
		cancel.setTypeface(tf);
		//ok.setLayoutParams(new LinearLayout.LayoutParams(width-150, LinearLayout.LayoutParams.WRAP_CONTENT));
		ok.setOnClickListener(listener);

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(
						android.graphics.Color.TRANSPARENT));

		dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}


	public static void showPnrDeleteDialog(final Context context, String titleText, String messageText, final View listViewItem, int listItem, final ListView l){

		metrics = context.getResources().getDisplayMetrics();
		width = metrics.widthPixels;
		height = metrics.heightPixels;

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_alert_twobuttons);

		TextView title =  (TextView) dialog.findViewById(R.id.title);
		TextView message =  (TextView) dialog.findViewById(R.id.message);
		Button ok =  (Button) dialog.findViewById(R.id.button1);
		Button cancel = (Button) dialog.findViewById(R.id.button2);


		title.setText(titleText);
		title.setTypeface(tf);
		message.setText(messageText);
		message.setTypeface(tf);
		ok.setTypeface(tf);
		cancel.setTypeface(tf);
		//ok.setLayoutParams(new LinearLayout.LayoutParams(width-150, LinearLayout.LayoutParams.WRAP_CONTENT));
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				TextView pnrNo = (TextView) listViewItem.findViewById(R.id.pnr_num);
				String pnr = pnrNo.getText().toString().substring(5);
				sqlHelper.deleteRow("CHECKED_PNRS", "PNR_NUMBER ='"+pnr+"'");
				sqlHelper.deleteRow("LAST_CHECKED_STATUS", "PNR_NUMBER ='"+pnr+"'");
				sqlHelper.deleteRow("TRAIN_ROUTE", "PNR_NUMBER ='"+pnr+"'");

				CustomListViewAdapterCheckedPnrs adapter = new CustomListViewAdapterCheckedPnrs(
						context,
						R.layout.custom_list_item_pnr_searched,
						sqlHelper.getPnrDetail());

				l.setAdapter(adapter);
				dialog.dismiss();

			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(
						android.graphics.Color.TRANSPARENT));

		dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	/**
	 * This function calls API to get pnr status
	 * @param pnr
	 * @param context
	 */
	public void getPnrStatus(String pnr, Context context){

		this.context = context;
		//String pnrUrl = "http://www.pnrstatus.in/pnr/query.php";
		String pnrUrl = "http://1-dot-pnrexpressservice.appspot.com/PnrStatusService";

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {

			MyAsyncTask obj = new MyAsyncTask(context, Method.GET);
			obj.execute(pnrUrl+"?pnr="+pnr, pnr);


		} else {

			Toast.makeText(context,
					"No internet connection !!", Toast.LENGTH_LONG).show();

		}
	}

	public class MyAsyncTask extends BaseAsyncTask{
		Context context;
		public MyAsyncTask(Context context, Method method) {
			super(context, method);
			this.context = context;			
			// TODO Auto-generated constructor stub
		}

		String resultPnr = null;
		String resultRoute;
		ProgressBar bar;
		Dialog dialog;
		String pnr;

		@Override
		protected String doInBackground(String... params) {

			pnr = params[1];

			try {

				if(!sqlHelper.doesPnrExist(pnr)){
					
					RestClient client = new RestClient(params[0]);
					resultPnr  = client.executeGet();

					JSONObject resultObj = new JSONObject(resultPnr);
					String trainNo = resultObj.getString("train_num");

					//Getting the route train information 
					RestClient clientRoute = new RestClient("http://1-dot-pnrexpressservice.appspot.com/TrainRouteService?trainno="+trainNo);
					Log.i("URL", "http://1-dot-pnrexpressservice.appspot.com/TrainRouteService?trainno="+trainNo);
					resultRoute = clientRoute.executeGet();
					Log.i("RESPONSE", resultRoute);

					JSONObject resultJson = new JSONObject(resultRoute);
					
					JSONArray stations = resultJson.getJSONArray("train_route");

					for(int i = 0 ; i<stations.length() ; i++){

						JSONObject station = stations.getJSONObject(i);
						
						String stationNo = station.getString("stationNo");
						String arrivalTime =station.getString("arrivalTime");
						String departureTime = station.getString("departureTime");
						String stopTime = station.getString("stopTime");
						String day = station.getString("day");
						String distance = station.getString("distance");
						String stationName = station.getString("stationName");
						String stationCode =station.getString("stationCode");

						//JSONObject location = station.getJSONObject("location");
						String latitude = "null";
						String longitude = "null";

						sqlHelper.insertInTrainRoute(new Station(pnr, stationName, stationCode, stationNo, arrivalTime, departureTime, stopTime, day, distance, latitude, longitude));

					}

				}

			} catch (Exception e) {
				
				e.printStackTrace();
			}			
			
			return super.doInBackground(params);
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			if(result != null){

				dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog_pnr_result);
				dialog.setCanceledOnTouchOutside(false);
				try {

					JSONObject resultObj = new JSONObject(result);
					if (resultObj.getString("error").equals("null")) {

						try {

							final String trainNo = resultObj.getString("train_num");
							final String trainName = resultObj.getString("train_name");
							final String doj = resultObj.getString("doj")	;				
							final String fromCode =  resultObj.getJSONObject("from_station").getString("code");
							final String fromName = resultObj.getJSONObject("from_station").getString("name");
							final String toCode = resultObj.getJSONObject("to_station").getString("code");
							final String toName = resultObj.getJSONObject("to_station").getString("name");
							final String reservationUptoCode = resultObj.getJSONObject("reservation_upto").getString("code");
							final String reservationUptoName = resultObj.getJSONObject("reservation_upto").getString("name");

							final String boardingPointCode = resultObj.getJSONObject("boarding_point").getString("code");
							final String boardingPointName = resultObj.getJSONObject("boarding_point").getString("name");
							final String reservationClass = resultObj.getString("class");
							int noOfPassengers =  resultObj.getInt("no_of_passengers");

							final String chart = resultObj.getString("chart_prepared").equals("N")?"Chart not prepared.":"Chart prepared.";				

							String serial;
							String bookingStatus;
							String currentStatus;
							
							JSONArray passengers = resultObj.getJSONArray("passengers");
							
							final List<Passenger> listPassengers = new ArrayList<Passenger>();
							for (int i = 0; i < passengers.length(); i++) {

								JSONObject passenger = passengers.getJSONObject(i);

								serial = passenger.getString("sr");
								bookingStatus = passenger.getString("booking_status");
								currentStatus = passenger.getString("current_status");

								listPassengers.add(new Passenger(serial, bookingStatus,
										currentStatus));
							}

							CustomListViewAdapterPassengers adapter = new CustomListViewAdapterPassengers(
									context,
									R.layout.custom_list_item_pnrsearch_dialog,
									listPassengers);

							ListView l = (ListView) dialog.findViewById(R.id.listView1);

							l.setAdapter(adapter);
							l.setTextFilterEnabled(true);

							TextView pnrNum = (TextView) dialog
									.findViewById(R.id.pnr_number);
							pnrNum.setText("PNR: " + pnr);
							pnrNum.setTypeface(tf);
							TextView trainInfo = (TextView) dialog
									.findViewById(R.id.train_name_number);
							trainInfo.setText(trainName + "(" + trainNo + ")");
							trainInfo.setTypeface(tf);
							TextView boardingStation = (TextView) dialog
									.findViewById(R.id.boardingStation);
							boardingStation.setText(boardingPointName + "( "
									+ boardingPointCode + ")");
							boardingStation.setTypeface(tf);
							TextView reservationUptoStation = (TextView) dialog
									.findViewById(R.id.reservationUpto);
							reservationUptoStation.setText(reservationUptoName + "( "
									+ reservationUptoCode + ")");
							reservationUptoStation.setTypeface(tf);
							TextView departure = (TextView) dialog
									.findViewById(R.id.dateOfJourney);
							departure.setText(AppHelper.changeDateFormat(doj, "yyyy-MM-dd",
									"MMMM dd, yyyy"));
							departure.setTypeface(tf);
							TextView classs = (TextView) dialog
									.findViewById(R.id.classs);
							classs.setText(reservationClass);
							classs.setTypeface(tf);
							TextView chartStatus = (TextView) dialog
									.findViewById(R.id.chart);
							chartStatus.setText(chart);
							chartStatus.setTypeface(tf);

							TextView fromText = (TextView) dialog
									.findViewById(R.id.textView1);
							TextView totext = (TextView) dialog
									.findViewById(R.id.textView3);
							TextView departureText = (TextView) dialog
									.findViewById(R.id.textView5);
							TextView classText = (TextView) dialog
									.findViewById(R.id.textView7);
							fromText.setTypeface(tf);
							totext.setTypeface(tf);
							departureText.setTypeface(tf);
							classText.setTypeface(tf);

							String lastStatus = "";

							for (int j = 0; j < noOfPassengers; j++) {

								JSONObject passenger = passengers.getJSONObject(j);

								if (j != noOfPassengers - 1)
									lastStatus = lastStatus
									+ passenger.getString("current_status")
									+ ", ";
								else
									lastStatus = lastStatus
									+ passenger.getString("current_status");

							}

							if (!sqlHelper.doesPnrExistInLastCheckedStatus(pnr))
								sqlHelper.insertInLastCheckedStatus(new LastStatus(pnr,
										lastStatus));
							else
								sqlHelper.updateInLastCheckedStatus(new LastStatus(pnr,
										lastStatus));

							Button bt = (Button) dialog.findViewById(R.id.diaogclose);
							bt.setOnClickListener(new View.OnClickListener() {

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
											"STATUS FOR PNR NO:" + pnr);

									// build the body of the message to be shared
									String shareMessage = 
											 trainName + "(" + trainNo + ")"
											+ "\nFrom: " + boardingPointName + "( "
											+ boardingPointCode + ")\nTo: "
											+ reservationUptoName + "( "
											+ reservationUptoCode + ") \nDeparture: "
											+ doj + "\nClass: " + reservationClass;

									for (Passenger p : listPassengers) {
										shareMessage = shareMessage + "\nPassenger"
												+ p.getName() + ":- Initial Status:"
												+ p.getStatusBefore()
												+ "   Current Status:"
												+ p.getStatusAfter();
									}

									shareMessage = shareMessage
											+ "\n\n"+chart;


									// add the message
									shareIntent.putExtra(
											android.content.Intent.EXTRA_TEXT,
											shareMessage);

									// start the chooser for sharing
									context.startActivity(Intent.createChooser(shareIntent,
											"Share your PNR Status"));

								}
							});

							dialog.getWindow().setBackgroundDrawable(
									new ColorDrawable(
											android.graphics.Color.TRANSPARENT));

							if (!((Activity) context).isFinishing()) {
								dialog.show();
							}

							if (!sqlHelper.doesPnrExist(pnr)) {

								sqlHelper.insertInCheckedPnrs(new PnrDetail(pnr,
										trainName, trainNo, fromName, fromCode, toName,
										toCode, boardingPointName, boardingPointCode,
										reservationUptoName, reservationUptoCode, doj,
										reservationClass));
							}

							CustomListViewAdapterCheckedPnrs adapter1 = new CustomListViewAdapterCheckedPnrs(
									context,
									R.layout.custom_list_item_pnr_searched,
									sqlHelper.getPnrDetail());

							PnrFragment.l.setAdapter(adapter1);

						}catch (Exception e) {
							showAlertDialog(context, "ERROR!","Some error occured ! Please try again after some time.");
							e.printStackTrace();
						}

					} else {

						showAlertDialog(context, "ERROR!", resultObj.getString("error"));
					}

				} catch (Exception e){
					e.printStackTrace();
					showAlertDialog(context, "Error", "Network Error Occured. Please try again");
				}

			}else{

				showAlertDialog(context, "Error", "Network Error Occured. Please try again");

			}

		}

	}

	public String timeformat24to12(String time){
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
			final Date dateObj = sdf.parse(time);
			System.out.println(dateObj);
			return (new SimpleDateFormat("hh:mm aa").format(dateObj));
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11 ) {
			month = months[num];
		}
		return month;
	}

}
