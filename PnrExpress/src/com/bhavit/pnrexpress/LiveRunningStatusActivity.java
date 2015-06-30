package com.bhavit.pnrexpress;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bhavit.pnrexpress.adapters.CustomListAdapterLiveRunningStatus;
import com.bhavit.pnrexpress.model.Availability;
import com.bhavit.pnrexpress.model.LiveTrainRunningStatus;
import com.bhavit.pnrexpress.util.RestClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class LiveRunningStatusActivity extends Activity {

	ListView list;
	int selectedPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_running_status);

		String tnum = getIntent().getStringExtra("tnum");
		String date = getIntent().getStringExtra("date");

		list = (ListView) findViewById(R.id.stations_list);		

		LiveStatusAsyncTask asyncTask = new LiveStatusAsyncTask();
		asyncTask.execute("http://api.pnrexpress.in/LiveStatusService", tnum, date);
	}

	public class LiveStatusAsyncTask extends AsyncTask<String, Void, String>{

		ProgressDialog p;


		@Override
		protected void onPreExecute() {
			p = new ProgressDialog(LiveRunningStatusActivity.this);
			p.show();
			p.setContentView(R.layout.custom_progressdialog);
			p.setCancelable(false);
			p.setCanceledOnTouchOutside(false);

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			RestClient client = new RestClient(params[0]+"?tnum="+params[1]+"&date="+params[2]);
			client.addHeader("Content-Type", "application/x-www-form-urlencoded");
			String result = client.executeGet();
			System.out.println(result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			p.dismiss();
			ArrayList<LiveTrainRunningStatus> array = new ArrayList<LiveTrainRunningStatus>();
			JSONObject root;
			try {
				root = new JSONObject(result);
				boolean flag = true;
				selectedPosition = 0;
				if(root.isNull("error")){
					JSONArray arr = root.getJSONArray("livestatus");
					for(int i = 0; i<arr.length(); i++){

						JSONObject obj = arr.getJSONObject(i);
						String station = obj.getString("station");
						String platform = obj.getString("platform");
						String sArrival = obj.getString("scheduledArrival");
						String sDeparture = obj.getString("scheduledDeparture");
						String aArrival = obj.getString("actualArrival");
						String aDeparture = obj.getString("actualDeparture");
						String trainStatus = obj.getString("trainStatus");
						LiveTrainRunningStatus object = new LiveTrainRunningStatus(station, platform, sArrival, sDeparture, aArrival, aDeparture, trainStatus);

						if(trainStatus.contains("Departed"))
							object.setBackground(R.color.green);

						if(flag){
							if(obj.getString("trainStatus").contains("Waiting") || obj.getString("trainStatus").equals("")){
								object.setTrainIcon(View.VISIBLE);
								selectedPosition = i;
							flag=false;
							}
						}

						array.add(object);

					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			CustomListAdapterLiveRunningStatus adapter = new CustomListAdapterLiveRunningStatus(LiveRunningStatusActivity.this, R.layout.custom_list_item_live_running_status, array);

			list.setAdapter(adapter);
			
			list.smoothScrollToPosition(selectedPosition);

			super.onPostExecute(result);
		}

	}

}
