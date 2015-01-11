package com.bhavit.pnrexpress.broadcastreceiver;

import java.util.Calendar;

import com.bhavit.pnrexpress.service.BackgroundUpdateService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 30);

		Intent intentService = new Intent(context, BackgroundUpdateService.class);

		PendingIntent pintent = PendingIntent
				.getService(context, 0, intentService, 0);

		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // for
		// 60*60*1000
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				15 * 60 * 1000, pintent);
    }

	
}
