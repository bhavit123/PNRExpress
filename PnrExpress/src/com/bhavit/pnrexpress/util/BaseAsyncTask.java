package com.bhavit.pnrexpress.util;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class BaseAsyncTask extends AsyncTask<String, Void, String> {

	public enum Method {
		GET, POST
	};

	ProgressDialog dialog;
	Context context;
	private String response;
	boolean cancelled;
	boolean showLoadingDialog = true;
	Method method = Method.GET;

	public BaseAsyncTask(Context context, Method method, boolean showLoadingDialog) {
		super();
		this.context = context;
		cancelled = false;
		this.showLoadingDialog = showLoadingDialog;
		this.method = method;
	}
	
	public BaseAsyncTask(Context context, Method method) {
		super();
		this.context = context;
		cancelled = false;
		this.method = method;
	}


	@Override
	protected void onPreExecute() {

		if (showLoadingDialog) {
			dialog = new ProgressDialog(context);
			dialog.setMessage("Loading...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();

		}
	}

	@Override
	protected String doInBackground(String... params) {

		if (method == Method.GET) {

			RestClient rClient = new RestClient(params[0]);
			Log.i("URL", params[0]);
			response = rClient.executeGet();
			Log.i("Response", response, null);
			
		} else {

			RestClient rClient = new RestClient(params[0]);
			rClient.addStringBody(params[1]);
			Log.i("Request", params[1], null);
			response = rClient.executePost();
			Log.i("Response", response, null);
		}

		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		dialog.dismiss();
		super.onPostExecute(result);
	}
	
}
