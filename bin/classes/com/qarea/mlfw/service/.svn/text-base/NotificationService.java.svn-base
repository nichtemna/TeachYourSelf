package com.qarea.mlfw.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.Extras;
import com.qarea.mlfw.R;
import com.qarea.mlfw.activity.CheckingKnowledgeActivity;
import com.qarea.mlfw.util.LocalDataProvider;

public class NotificationService extends Service {

	private int timeoutHours;
	private long timeoutMills = BaseActivity.MILLSINHOUR;
	private MyBinder binder;
	private AlarmManager am;
	private Intent alarmIntent;
	private PendingIntent pendingIntent;

	int count = 0;
	Calendar cal;

	public int onStartCommand(Intent intent, int flags, int startId) {
		binder = new MyBinder();
		LocalDataProvider.getInstance(this);
		Log.d("tag", "onStartCommand ");
		getFromPreferences();
		return START_STICKY;
	}

	/*
	 * checking if isWords() and sending notifications with timeout
	 */
	private void schedule() {
		alarmIntent = new Intent(this, CheckKnowledgeReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.cancel(pendingIntent);
		cal = Calendar.getInstance(Locale.getDefault());
		am.setRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + timeoutMills, timeoutMills,
				pendingIntent);
	}

	public void getFromPreferences() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		timeoutHours = settings.getInt(BaseActivity.TIMEOUT, 1);
		if (timeoutHours < 1) {
			timeoutMills = BaseActivity.MILLSINHOUR;
		}
		schedule();
	}

	/*
	 * bind this service to activities
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public class MyBinder extends Binder {
		public NotificationService getService() {
			return NotificationService.this;
		}
	}

	@Override
	public void onDestroy() {
		Log.d("tag", "service destroyed");
	}

}
