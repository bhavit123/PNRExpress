package com.bhavit.pnrexpress;

import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.bhavit.pnrexpress.fragment.SeatAvailabilityFragment.MyAsyncTask;
import com.bhavit.pnrexpress.model.Availability;
import com.bhavit.pnrexpress.model.Passenger;
import com.bhavit.pnrexpress.model.Station;
import com.bhavit.pnrexpress.model.Train;
import com.bhavit.pnrexpress.util.AppConstants;
import com.bhavit.pnrexpress.util.CustomListViewAdapterAvailability;
import com.bhavit.pnrexpress.util.CustomListViewAdapterTrainsList;
import com.google.android.gms.cast.Cast.ApplicationConnectionResult;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;
import com.jaunt.component.Table;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class TrainsSearchResult extends BaseActivity {

	ArrayList<Train> trainsList;
	ListView searchResultTrains;
	String day;
	String month;
	String classs, quota;
	String from_to;
	TextView date,heading;


	@SuppressLint("SetJavaScriptEnabled") 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trains_search_result);

		searchResultTrains = (ListView) findViewById(R.id.lv_trainsList);

		trainsList = new ArrayList<Train>();
		String result = getIntent().getExtras().getString("result");
		quota = getIntent().getExtras().getString("quota");
		day = getIntent().getExtras().getString("day");
		month = getIntent().getExtras().getString("month");
		from_to = getIntent().getExtras().getString("from-to");

		heading = (TextView)findViewById(R.id.textView_heading);
		heading.setTypeface(BaseActivity.tf);
		heading.setText(from_to);

		date = (TextView)findViewById(R.id.textView1);
		date.setText(day+" "+getMonthForInt(Integer.parseInt(month)-1));
		date.setTypeface(tf);

		UserAgent userAgent = new UserAgent();
		try {

			userAgent.openContent(result);

			Table table = userAgent.doc.getTable("<table>");  //find table element

			Element eTable = userAgent.doc.findFirst("<table>");

			Elements trs = eTable.findEach("<tr>");
			Elements tableHeadings = table.getRow(0);

			for(int i = 1 ; i<trs.size() ; i++){

				Elements elements = table.getRow(i);
				Element anchor = elements.findFirst("<a href='#'>");
				String trainNo = elements.getElement(0).innerHTML();
				String trainName = elements.getElement(1).innerHTML().trim().substring(1);
				String fromStation = elements.getElement(2).innerHTML().trim();
				String departureTime = elements.getElement(3).innerHTML().trim();
				String toStation = elements.getElement(4).innerHTML().trim();
				String arrivalTime = elements.getElement(5).innerHTML().trim();
				String travelTime = elements.getElement(6).innerHTML().trim();
				String runOn = elements.getElement(7).innerHTML().trim()+elements.getElement(8).innerHTML().trim()+elements.getElement(9).innerHTML().trim()+elements.getElement(10).innerHTML().trim()+elements.getElement(11).innerHTML().trim()+elements.getElement(12).innerHTML().trim()+elements.getElement(13).innerHTML().trim();
				String classes = "";

				for(int j = 14 ; j<=21; j++){
					if(!elements.getElement(j).innerHTML().trim().equals("-")){
						classes = classes +tableHeadings.getElement(j).innerHTML().trim()+"|";
					}
				}

				String searchQuery =  anchor.getAt("onclick").substring(12, 34);
				trainsList.add(new Train(trainName, trainNo, fromStation, toStation, timeformat24to12(departureTime), timeformat24to12(arrivalTime), travelTime.split("\\:")[0]+"Hours "+travelTime.split("\\:")[1]+"mins", runOn, classes, searchQuery));
			}

		} catch (ResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}

		CustomListViewAdapterTrainsList adapter = new CustomListViewAdapterTrainsList(this, R.layout.custom_list_item_trains_list, trainsList);
		searchResultTrains.setAdapter(adapter);

		searchResultTrains.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				String url = "http://pnrbuddy.com/hauth/seatavail";

				ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = cm.getActiveNetworkInfo();
				// if no network is available networkInfo will be null
				// otherwise check if we are connected to internet
				if (networkInfo != null && networkInfo.isConnected()) {
					MyAsyncTask asynctask = new MyAsyncTask();
					asynctask.execute(url, String.valueOf(arg2), day, month,classs, AppConstants.getQuotaValue(quota));

				} else {

					Toast.makeText(context,
							"No internet connection !!", Toast.LENGTH_LONG).show();

				}
			}
		});

	}

	public class MyAsyncTask extends AsyncTask<String, Void, Void> implements OnItemSelectedListener{

		String result;
		String fromSt;
		String toSt;
		String quotaName;
		ProgressDialog p;
		Dialog dialog;
		String param0, param1, param2, param3;
		TextView trainName, trainNo, from_to;
		Spinner classs, quota;
		ListView availabilities;
		ArrayList<Availability> allAvails;
		Train train;

		@Override
		protected void onPreExecute() {

			p = new ProgressDialog(TrainsSearchResult.this);
			p.show();
			p.setContentView(R.layout.custom_progressdialog);
			p.setCancelable(false);
			p.setCanceledOnTouchOutside(false);

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {

			param0 = params[0];
			param1 = params[1];
			param2 = params[2];
			param3 = params[3];

			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(params[0]);
				httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
				StringEntity entity;
				train = trainsList.get(Integer.parseInt(params[1]));

				entity = new StringEntity("traindtl="+train.getSearchQuery()+"&day="+params[2]+"&month="+params[3]+"&quota="+AppConstants.getQuotaValue(TrainsSearchResult.this.quota)+"&seatclass="+train.getClasses().split("\\|")[0]+"&classopt=ZZ", HTTP.UTF_8);

				httpPost.setEntity(entity);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				result = httpClient.execute(httpPost, responseHandler);

			}catch(Exception e){


			}

			return null;
		}

		@Override
		protected void onPostExecute(Void res) {

			p.dismiss();
			System.out.println(result);

			if(!result.contains("Unable to get availability due to network error")){

				dialog = new Dialog(TrainsSearchResult.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog_seatavailability_result);
				dialog.setCanceledOnTouchOutside(false);

				UserAgent userAgent = new UserAgent();
				allAvails = new ArrayList<Availability>();
				
				try {

					userAgent.openContent(result);
					Table table1 = userAgent.doc.getTable(0);

					Elements row = table1.getRow(1);
					System.out.println(row.getElement(0).innerHTML()+" "+row.getElement(1).innerHTML());

					trainName = (TextView) dialog.findViewById(R.id.train_name);
					trainName.setText(row.getElement(1).innerHTML().trim());
					trainName.setTypeface(tf);

					trainNo = (TextView) dialog.findViewById(R.id.train_no);
					trainNo.setText("("+row.getElement(0).innerHTML()+")");
					trainNo.setTypeface(tf);

					from_to = (TextView) dialog.findViewById(R.id.from_to);
					from_to.setText(row.getElement(3).innerHTML().trim()+ " to "+row.getElement(4).innerHTML().trim());
					from_to.setTypeface(tf);

					quotaName = row.getElement(5).innerHTML().trim();

					Table table2 = userAgent.doc.getTable(1);
					Elements tables = userAgent.doc.findEach("<table>");
					Element table = tables.getElement(1);
					Elements trs = table.findEach("<tr>");


					for(int i = 1 ; i<trs.size() ; i++){

						Elements elements = table2.getRow(i);
						String doj = elements.getElement(1).innerHTML();
						String availability = elements.getElement(2).innerHTML();

						allAvails.add(new Availability(doj, availability));

					}

					classs = (Spinner)dialog.findViewById(R.id.classs);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(TrainsSearchResult.this, android.R.layout.simple_spinner_item, train.getClasses().split("\\|"));
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					classs.setAdapter(adapter);

					classs.setPrompt("Class");

					int pos = 7;
					for(int i = 0 ; i<train.getClasses().split("\\|").length ; i++){

						if(classs.getItemAtPosition(i).toString().equals(train.getClasses().split("\\|")[0])){
							pos = i;
						}
					}

					classs.setSelection(pos, true);
					classs.setOnItemSelectedListener(this);
					//.setOnItem

					String[] array = new String[10];	
					AppConstants.getQuotas().keySet().toArray(array);
					quota = (Spinner)dialog.findViewById(R.id.quota);
					quota.setPrompt("Quota");
					adapter = new ArrayAdapter<String>(TrainsSearchResult.this, android.R.layout.simple_spinner_item, array);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					quota.setAdapter(adapter);
					
					for(int i = 0 ; i<10 ; i++){

						if(quota.getItemAtPosition(i).toString().equals(TrainsSearchResult.this.quota)){
							pos = i;
						}
					}
					quota.setSelection(pos, true);
					quota.setOnItemSelectedListener(this);

					availabilities = (ListView)dialog.findViewById(R.id.listView1);
					CustomListViewAdapterAvailability customAdapter = new CustomListViewAdapterAvailability(TrainsSearchResult.this, R.layout.custom_list_item_availabilities, allAvails);
					availabilities.setAdapter(customAdapter);	

				} catch (ResponseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFound e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Button close = (Button)dialog.findViewById(R.id.dialogclose);
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
								"Availability for " + trainName.getText() + "\n");

						// build the body of the message to be shared
						String shareMessage = trainName.getText()+""+trainNo.getText()+""
								+ "\n" + from_to.getText() + "\nQuota: "+quotaName+
								"\n\nAVAILABILITY ("+classs.getSelectedItem().toString()+") : ";

						for (Availability a : allAvails) {
							shareMessage = shareMessage + "\n"
									+ a.getDate() +"  "
									+ "("+a.getAvailability()+")";
						}

						// add the message
						shareIntent.putExtra(
								android.content.Intent.EXTRA_TEXT,
								shareMessage);

						// start the chooser for sharing
						TrainsSearchResult.this.startActivity(Intent.createChooser(shareIntent,
								"Share your PNR Status"));

					}
				});

				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(
								android.graphics.Color.TRANSPARENT));

				if (!TrainsSearchResult.this.isFinishing()) {
					dialog.show();
				}

			} else {

				showAlertDialog(TrainsSearchResult.this, "Error", "Network Error Occured. Please try again");
			}
			super.onPostExecute(res);
		}

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			p.show();

			allAvails.clear();
			
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			// if no network is available networkInfo will be null
			// otherwise check if we are connected to internet
			if (networkInfo != null && networkInfo.isConnected()) {

				new Thread(new Runnable() {

					@Override
					public void run() {

						try{
							HttpClient httpClient = new DefaultHttpClient();
							HttpPost httpPost = new HttpPost(param0);
							httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
							StringEntity entity;
							Train train = trainsList.get(Integer.parseInt(param1));
							entity = new StringEntity("traindtl="+train.getSearchQuery()+"&day="+param2+"&month="+param3+"&quota="+AppConstants.getQuotaValue(quota.getSelectedItem().toString())+"&seatclass="+classs.getSelectedItem().toString()+"&classopt=ZZ", HTTP.UTF_8);

							httpPost.setEntity(entity);
							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							result = httpClient.execute(httpPost, responseHandler);

							UserAgent userAgent = new UserAgent();
							try {
								userAgent.openContent(result);
								
								Table table1 = userAgent.doc.getTable(0);
								Elements row = table1.getRow(1);
								quotaName = row.getElement(5).innerHTML().trim();
								
								Table table2 = userAgent.doc.getTable(1);
								Elements tables = userAgent.doc.findEach("<table>");
								Element table = tables.getElement(1);
								Elements trs = table.findEach("<tr>");

								for(int i = 1 ; i<trs.size() ; i++){

									Elements elements = table2.getRow(i);
									String doj = elements.getElement(1).innerHTML();
									String availability = elements.getElement(2).innerHTML();

									allAvails.add(new Availability(doj, availability));

								}
							} catch (ResponseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}catch(Exception e){


						}

						runOnUiThread(new Runnable() {
							public void run() {

								CustomListViewAdapterAvailability adapter = new CustomListViewAdapterAvailability(TrainsSearchResult.this, R.layout.custom_list_item_availabilities, allAvails);
								availabilities.setAdapter(adapter);	
								p.dismiss();
							}
						});



					}
				}).start();

			}else {

				Toast.makeText(context,
						"No internet connection !!", Toast.LENGTH_LONG).show();

			}
		} 

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}


	}

}
