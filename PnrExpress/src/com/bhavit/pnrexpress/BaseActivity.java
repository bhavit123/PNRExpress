package com.bhavit.pnrexpress;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bhavit.pnrexpress.dao.SqlHelper;
import com.bhavit.pnrexpress.fragment.PnrFragment;
import com.bhavit.pnrexpress.model.LastStatus;
import com.bhavit.pnrexpress.model.Station;
import com.bhavit.pnrexpress.model.Passenger;
import com.bhavit.pnrexpress.model.PnrDetail;
import com.bhavit.pnrexpress.util.CustomListViewAdapterCheckedPnrs;
import com.bhavit.pnrexpress.util.CustomListViewAdapterPassengers;
import com.bhavit.pnrexpress.util.HMACGenarator;
import com.bhavit.pnrexpress.util.RestClient;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;
import com.jaunt.component.Table;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

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

	public void showAlertDialogTwoButtons(final Context context, String titleText, String messageText, final View listViewItem, int listItem, final ListView l){

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
		String pnrUrl = "http://pnrbuddy.com/hauth/pnrstatus";

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {

			MyAsyncTask obj = new MyAsyncTask();
			obj.execute(pnrUrl, pnr);


		} else {

			Toast.makeText(context,
					"No internet connection !!", Toast.LENGTH_LONG).show();

		}
	}

	public class MyAsyncTask extends AsyncTask<String, Void, Void> {
		String resultPnr;
		String resultRoute;
		ProgressBar bar;
		Dialog dialog;
		String pnr;
		ProgressDialog p;

		@Override
		protected void onPreExecute() {
			//bar = (ProgressBar) findViewById(R.id.progressBar);
			//bar.setVisibility(ProgressBar.VISIBLE);
			p = new ProgressDialog(context);
			p.show();
			p.setContentView(R.layout.custom_progressdialog);
			p.setCancelable(false);
			p.setCanceledOnTouchOutside(false);

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {

			pnr = params[1];

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(params[0]);
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			try {
				StringEntity entity = new StringEntity("pnr="+params[1], HTTP.UTF_8);
				httpPost.setEntity(entity);
				resultPnr = httpClient.execute(httpPost, responseHandler);
				/*JSONObject obj = new JSONObject(resultPnr);
				JSONArray journey = obj.getJSONArray("Journey");
				String trainNo = ((JSONArray)journey.get(0)).get(1).toString();*/

				UserAgent userAgentPnr = new UserAgent();
				userAgentPnr.openContent(resultPnr);

				Element anchor = userAgentPnr.doc.findFirst("<a>");
				String trainNo = anchor.innerHTML();


				if(!sqlHelper.doesPnrExist(pnr)){
					//Getting the route train information 
					httpClient = new DefaultHttpClient();
					httpPost = new HttpPost("http://pnrbuddy.com/hauth/trainroute");
					httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
					entity = new StringEntity("trainno="+trainNo, HTTP.UTF_8);
					httpPost.setEntity(entity);

					resultRoute = httpClient.execute(httpPost, responseHandler);


					UserAgent userAgent = new UserAgent();
					userAgent.openContent(resultRoute);

					Table table = userAgent.doc.getTable("<table class=default-table>");  //find table element

					Element eTable = userAgent.doc.findFirst("<table class=default-table>");  //find table element
					Elements trs = eTable.findEach("<tr>");


					for(int i = 1 ; i<trs.size() ; i++){

						Elements elements = table.getRow(i);
						String stationNo = elements.getElement(0).innerHTML();
						String arrivalTime = elements.getElement(2).innerHTML().trim();
						String departureTime = elements.getElement(3).innerHTML().trim();
						String stopTime = elements.getElement(4).innerHTML().trim();
						String day = "";
						String distance = elements.getElement(5).innerHTML().trim();

						String station= elements.getElement(1).innerHTML();
						String stationName = station.split("\\(")[0].trim();
						String stationCode = station.split("\\(")[1].split("\\)")[0].trim();

						//JSONObject location = station.getJSONObject("location");
						String latitude = "null";
						String longitude = "null";


						sqlHelper.insertInTrainRoute(new Station(pnr, stationName, stationCode, stationNo, arrivalTime, departureTime, stopTime, day, distance, latitude, longitude));

					}

				}

			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			p.dismiss();
			//bar.setVisibility(ProgressBar.INVISIBLE);
			// Toast.makeText(getApplicationContext(), this.result+"",
			// Toast.LENGTH_LONG).show();

			if(!resultPnr.contains("Unable to get availability due to network error") && !resultPnr.contains("Network Error. Please try again after some time")){

				dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog_pnr_result);
				dialog.setCanceledOnTouchOutside(false);
				String err = null;
				try {
					JSONObject obj = new JSONObject(resultPnr);
					if(!obj.isNull("Error")){

						err = obj.getString("Error");
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (err == null) {

					try {

						UserAgent userAgentPnr = new UserAgent();
						userAgentPnr.openContent(resultPnr);
						Table table = userAgentPnr.doc.getTable(0);  //find table element

						Elements elements = table.getRow(1);

						final String trainNo = elements.getElement(0).findFirst("<a>").innerHTML().trim();
						final String trainName = elements.getElement(1).findFirst("<a>").innerHTML().trim();
						final String doj = elements.getElement(2).innerHTML().split("\\:")[1].trim();

						elements = table.getRow(2);

						final String fromCode =  elements.getElement(0).findFirst("<a>").innerHTML().split("\\(")[0].trim();
						final String fromName =  elements.getElement(0).findFirst("<a>").innerHTML().split("\\(")[1].split("\\)")[0];
						final String toCode = elements.getElement(1).findFirst("<a>").innerHTML().split("\\(")[0].trim();
						final String toName = elements.getElement(1).findFirst("<a>").innerHTML().split("\\(")[1].split("\\)")[0];
						final String reservationUptoCode = elements.getElement(2).findFirst("<a>").innerHTML().split("\\(")[0].trim();
						final String reservationUptoName = elements.getElement(2).findFirst("<a>").innerHTML().split("\\(")[1].split("\\)")[0];

						elements = table.getRow(3);

						final String boardingPointCode = elements.getElement(0).findFirst("<a>").innerHTML().split("\\(")[0].trim();
						final String boardingPointName = elements.getElement(0).findFirst("<a>").innerHTML().split("\\(")[1].split("\\)")[0];
						final String reservationClass = elements.getElement(1).innerHTML().split("\\:")[1].trim();
						int noOfPassengers =  Integer.parseInt(elements.getElement(2).innerHTML().split("\\:")[1].trim());

						elements = table.getRow(4);

						final String chart = elements.getElement(0).innerHTML().split("\\:")[1].trim();

						table = userAgentPnr.doc.getTable(1);					

						String serial;
						String bookingStatus;
						String currentStatus;
						final List<Passenger> listPassengers = new ArrayList<Passenger>();
						for (int i = 1; i <= noOfPassengers; i++) {

							elements = table.getRow(i);

							serial = (i)+"";
							bookingStatus = elements.getElement(1).innerHTML().trim();
							currentStatus = elements.getElement(2).innerHTML().trim();

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
						departure.setText(doj);
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

							//JSONObject passenger = passengers.getJSONObject(j+1);

							if (j != noOfPassengers - 1)
								lastStatus = lastStatus
								+ elements.getElement(2).innerHTML().trim()
								+ ", ";
							else
								lastStatus = lastStatus
								+ elements.getElement(2).innerHTML().trim();

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
										"STATUS FOR PNR NO:" + pnr + "\n");

								// build the body of the message to be shared
								String shareMessage = "PNR: " + pnr + "\n"
										+ trainName + "(" + trainNo + ")"
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

					showAlertDialog(context, "ERROR!", err);
				}

				super.onPostExecute(result);
			
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
