package com.bhavit.pnrexpress.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.bhavit.pnrexpress.MainActivity;
import com.bhavit.pnrexpress.R;
import com.bhavit.pnrexpress.dao.SqlHelper;
import com.bhavit.pnrexpress.fragment.PnrFragment;
import com.bhavit.pnrexpress.model.LastStatus;
import com.bhavit.pnrexpress.model.PnrDetail;
import com.bhavit.pnrexpress.util.AppHelper;
import com.bhavit.pnrexpress.util.RestClient;

public class BackgroundUpdateService extends Service {

	SqlHelper sqlhelper;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		sqlhelper = new SqlHelper(getApplicationContext());
		//Toast.makeText(getApplicationContext(), "Service Created", 1).show();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "Service Destroy", 1).show();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		List<PnrDetail> list = sqlhelper.getUpcomingPnrDetails();

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		String url = "";
		String pnrs = "";
		// if no network is available networkInfo will be null
		// otherwise check if we are connected to internet
		if (networkInfo != null && networkInfo.isConnected()) {
			for(int i = 0 ; i<list.size() ; i++){
				PnrDetail obj = list.get(i);
				url = "http://pnrbuddy.com/hauth/pnrstatus";

				if(i!=list.size()-1){
					pnrs = pnrs+obj.getPnrNumber()+",,";
				} else{
					pnrs = pnrs+obj.getPnrNumber();
				}
			}

		}

		if(!pnrs.equals("")){

			MyAsyncTask myAsyncTask = new MyAsyncTask();
			myAsyncTask.execute("http://1-dot-pnrexpressservice.appspot.com/PnrStatusService", pnrs);

		}

		return super.onStartCommand(intent, flags, startId);
	}

	public class MyAsyncTask extends AsyncTask<String, Void, Void> {

		String resultPnr;
		String result[][] = null;
		String resultJSON;

		// Call after onPreExecute method
		protected Void doInBackground(String... urls) {

			String url = urls[0];
			String[] pnrArray = urls[1].split(",,");

			result = new String[pnrArray.length][2];

			for(int i = 0;i<pnrArray.length; i++){
				
				RestClient client = new RestClient(url+"?pnr="+pnrArray[i]);
				client.addHeader("Content-Type", "application/x-www-form-urlencoded");

				try {

					resultPnr = client.executeGet();
					JSONObject resultObj = new JSONObject(resultPnr);

					int noOfPassengers =  resultObj.getInt("no_of_passengers");

					String currentStatus = "";

					JSONArray passengers = resultObj.getJSONArray("passengers");

					for (int j = 1; j <= noOfPassengers; j++) {

						JSONObject passenger = passengers.getJSONObject(i);

						if(j!=noOfPassengers)
							currentStatus = currentStatus+passenger.getString("current_status")+", ";
						else
							currentStatus = currentStatus+passenger.getString("current_status");

					}

					result[i][0] = pnrArray[i];
					result[i][1] = currentStatus;

				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}	

			return null;
		}

		protected void onPostExecute(Void unused) {
			int flag = 0;
			Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			NotificationCompat.Builder mBuilder =
					new NotificationCompat.Builder(BackgroundUpdateService.this);

			Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
					R.drawable.notificaion);
			mBuilder.setLargeIcon(icon);
			mBuilder.setSmallIcon(R.drawable.notificaion);
			mBuilder.setContentTitle("PNR STATUS UPDATED");
			mBuilder.setSound(soundUri);

			CharSequence body = "Statuses of following pnrs are updated";

			try{

				if(!AppHelper.isArrayEmpty(result)){
					for(int i =0; i<result.length; i++){
						String before = sqlhelper.getLastStatus(result[i][0]);
						if(!result[i][1].equals(before)){
							flag = 1;
							body = body+"\nPNR:"+result[i][0]+"  Last Checked:"+before+"  Current Status:"+result[i][1];
							sqlhelper.updateInLastCheckedStatus(new LastStatus(result[i][0], result[i][1]));
						}
					}
				}

			} catch(Exception e){e.printStackTrace();}
			mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));

			if(flag!=0){

				Intent resultIntent = new Intent(BackgroundUpdateService.this, PnrFragment.class);

				// The stack builder object will contain an artificial back stack for the
				// started Activity.
				// This ensures that navigating backward from the Activity leads out of
				// your application to the Home screen.
				TaskStackBuilder stackBuilder = TaskStackBuilder.create(BackgroundUpdateService.this);
				// Adds the back stack for the Intent (but not the Intent itself)
				stackBuilder.addParentStack(MainActivity.class);
				// Adds the Intent that starts the Activity to the top of the stack
				stackBuilder.addNextIntent(resultIntent);
				PendingIntent resultPendingIntent =
						stackBuilder.getPendingIntent(
								0,
								PendingIntent.FLAG_UPDATE_CURRENT
								);
				mBuilder.setContentIntent(resultPendingIntent);
				NotificationManager mNotificationManager =
						(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				// mId allows you to update the notification later on.
				mNotificationManager.notify(1234, mBuilder.build());

			}

		}

	}

}
