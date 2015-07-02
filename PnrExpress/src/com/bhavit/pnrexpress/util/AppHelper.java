package com.bhavit.pnrexpress.util;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bhavit.pnrexpress.BaseActivity;
import com.bhavit.pnrexpress.R;

public class AppHelper {

	public static EditText inputDialogEditText;
	public static Dialog inputDialog;
	public static Dialog autoCompleteInputDialog;
	public static AutoCompleteTextView inputDialogAutoCompletetv;

	public static boolean isArrayEmpty(Object[][] arr) {

		boolean empty = true;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < arr.length; j++) {
				if (arr[j][i] != null) {
					empty = false;
					break;
				}
			}
		}

		return empty;
	}

	public static void openInputDialog(Context context, int inputType, String heading, String hint, OnClickListener listener){

		inputDialog = new Dialog(context);
		inputDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		inputDialog.setContentView(R.layout.custom_input_dialog);
		inputDialog.setCanceledOnTouchOutside(false);

		TextView headingTxt = (TextView) inputDialog.findViewById(R.id.heading);
		headingTxt.setTypeface(BaseActivity.tf);
		headingTxt.setText(heading);

		inputDialogEditText = (EditText) inputDialog
				.findViewById(R.id.ed_input);
		inputDialogEditText.setTypeface(BaseActivity.tf);
		inputDialogEditText.setHint(hint);
		inputDialogEditText.setRawInputType(inputType);

		Button tick = (Button) inputDialog.findViewById(R.id.btn_tick);
		tick.setOnClickListener(listener);

		inputDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		if (!((Activity)context).isFinishing()) {
			inputDialog.show();
		}

	}

	public static void openAutoCompleteInputDialog(Context context, String heading, String hint, ArrayAdapter<String> adapter, OnClickListener listener){

		autoCompleteInputDialog = new Dialog(context);
		autoCompleteInputDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		autoCompleteInputDialog.setContentView(R.layout.custom_autocomplete_input_dialog);
		autoCompleteInputDialog.setCanceledOnTouchOutside(false);

		TextView headingTxt = (TextView) autoCompleteInputDialog.findViewById(R.id.heading);
		headingTxt.setTypeface(BaseActivity.tf);
		headingTxt.setText(heading);

		inputDialogAutoCompletetv = (AutoCompleteTextView) autoCompleteInputDialog
				.findViewById(R.id.stationAutoCompletetv);
		inputDialogAutoCompletetv.setTypeface(BaseActivity.tf);
		inputDialogAutoCompletetv.setHint(hint);
		inputDialogAutoCompletetv.setAdapter(adapter);
		inputDialogAutoCompletetv.setThreshold(1);
		inputDialogAutoCompletetv.setDropDownAnchor(R.id.listView1);
		
		Button tick = (Button) autoCompleteInputDialog.findViewById(R.id.btn_tick);
		tick.setOnClickListener(listener);

		autoCompleteInputDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		if (!((Activity)context).isFinishing()) {
			autoCompleteInputDialog.show();
		}

	}
	
	public static String loadJSONFromAsset(Context context, String filename) {
		String json = null;
		try {

			InputStream is = context.getAssets().open(filename);

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
	
	public static void toast(Context context, String message){
		
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		
	}

}
