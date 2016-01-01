package com.bhavit.pnrexpress;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bhavit.pnrexpress.model.PnrDetail;

public class PnrActionsActivity extends BaseActivity {

	String pnrNo;
	Button liveRunningStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pnr_actions);
		
		liveRunningStatus = (Button) findViewById(R.id.btn_livestatus);

		pnrNo = getIntent().getExtras().getString("pnrNo");
		PnrDetail obj = BaseActivity.sqlHelper.getPnrDetail(pnrNo);
		
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		String doj = obj.getDateOfJourney();
		Calendar today = Calendar.getInstance();

		try {
		    Date date1 = myFormat.parse(today.get(Calendar.YEAR)+"-"+(today.get(Calendar.MONTH)+1)+"-"+(today.get(Calendar.DAY_OF_MONTH)));
		    Date date2 = myFormat.parse(doj);
		    long diff = date2.getTime() - date1.getTime();
		    System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
		    
		    if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) < 1){
		    	liveRunningStatus.setVisibility(View.VISIBLE);
		    }
		} catch (ParseException e) {
		    e.printStackTrace();
		}

		TextView heading = (TextView) findViewById(R.id.textView_heading);
		heading.setTypeface(tf);

		Button trainRoute = (Button) findViewById(R.id.btn_get_route);
		trainRoute.setTypeface(tf);
		trainRoute.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				try {

					Intent i = new Intent(PnrActionsActivity.this,
							LocationActivity.class);

					i.putExtra("pnr", pnrNo);
					startActivity(i);
				} catch (Exception e) {
					showAlertDialog(PnrActionsActivity.this, "ERROR",
							"A problem occured while getting route. Please try later.");
				}

			}
		});

		Button pnrStatus = (Button) findViewById(R.id.btn_pnr_status);
		pnrStatus.setTypeface(tf);
		pnrStatus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new BaseActivity().getPnrStatus(pnrNo, PnrActionsActivity.this);

			}
		});

		
		liveRunningStatus.setTypeface(tf);
		liveRunningStatus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(PnrActionsActivity.this,
						LiveRunningStatusActivity.class);

				PnrDetail obj = BaseActivity.sqlHelper.getPnrDetail(pnrNo);
				i.putExtra("tnum", obj.getTrainNumber());
				i.putExtra("tname", obj.getTrainName());
				i.putExtra(
						"date",
						obj.getDateOfJourney());

				startActivity(i);

			}
		});

	}

}
