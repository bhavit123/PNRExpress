package com.bhavit.pnrexpress;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.bhavit.pnrexpress.adapters.CustomListAdapterLiveRunningStatus;
import com.bhavit.pnrexpress.model.LiveTrainRunningStatus;
import com.bhavit.pnrexpress.util.BaseAsyncTask;
import com.bhavit.pnrexpress.util.BaseAsyncTask.Method;


public class LiveRunningStatusActivity extends BaseActivity {

	ListView list;
	int selectedPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_running_status);

		String tnum = getIntent().getStringExtra("tnum");
		String date = getIntent().getStringExtra("date");

		list = (ListView) findViewById(R.id.stations_list);		

		LiveStatusAsyncTask asyncTask = new LiveStatusAsyncTask(this,Method.GET);
		asyncTask.execute("http://api.pnrexpress.in/LiveStatusService"+"?tnum="+tnum+"&date="+date);
	}

	public class LiveStatusAsyncTask extends BaseAsyncTask{

		public LiveStatusAsyncTask(Context context, Method method) {
			super(context, method);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPostExecute(String result) {

			ArrayList<LiveTrainRunningStatus> array = new ArrayList<LiveTrainRunningStatus>();
			JSONObject root;
			String error = "";
			try {
				root = new JSONObject(result);
				if(!root.has("error")){

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

								if(obj.getString("trainStatus").contains("Waiting"))
									if(obj.getString("trainStatus").contains("Waiting")){

										if(i<(arr.length()-1) && arr.getJSONObject(i+1).getString("trainStatus").contains("Waiting")){


										}

										object.setTrainIcon(View.VISIBLE);
										selectedPosition = i;
										flag=false;
									} else if(obj.getString("trainStatus").equals("")){
										object.setTrainIcon(View.VISIBLE);
										selectedPosition = i;
										flag=false;
									}
							}

							array.add(object);

						}
					}
				} else {
					error = root.getString("error");
					showAlertDialog(LiveRunningStatusActivity.this, "ERROR!", error);
				}
			} catch (JSONException e) {
				showAlertDialog(LiveRunningStatusActivity.this, "ERROR!", "Some error occured, please try again later.");
				e.printStackTrace();
			}

			CustomListAdapterLiveRunningStatus adapter = new CustomListAdapterLiveRunningStatus(LiveRunningStatusActivity.this, R.layout.custom_list_item_live_running_status, array);

			list.setAdapter(adapter);

			list.smoothScrollToPosition(selectedPosition);

			super.onPostExecute(result);
		}

	}

}
