package com.qarea.mlfw.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.Extras;
import com.qarea.mlfw.R;
import com.qarea.mlfw.activity.CheckKnowledgeSwipeActivity;
import com.qarea.mlfw.activity.CheckingKnowledgeActivity;
import com.qarea.mlfw.activity.SettingsActivity;
import com.qarea.mlfw.util.LocalDataProvider;

/**
 * Created with IntelliJ IDEA. User: nichtemna Date: 5/24/13 Time: 9:59 AM To
 * change this template use File | Settings | File Templates.
 */
public class CheckKnowledgeReceiver extends BroadcastReceiver {
	private static final String body = "You have long not checked your knowledge!";
	private static final String title = "Checked knowledge! ";
	private Context context;
	private Calendar cal;
	private Calendar calendar;
	private LocalDataProvider dataProvider;
	private int timeoutHours;
	private int startHours;
	private int endHours;
	private boolean notRemind;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context.getApplicationContext();
		dataProvider = LocalDataProvider.getInstance(context
				.getApplicationContext());
		// Log.d("tag", "toSend " + toSend());
		if (toSend()) {
			sendNotif();
		}
	}

	/*
	 * send notifications
	 */
	private void sendNotif() {
		cal = Calendar.getInstance(Locale.getDefault());

		final Class<? extends Activity> cls = getCheckKnowledgeActivity();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent checkingIntent = new Intent(context, cls);
		NotificationCompat.Builder notificationBuiled = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setAutoCancel(true)
				.setTicker(title)
				.setContentText(body)
				.setContentIntent(
						PendingIntent.getActivity(context, 0, checkingIntent,
								PendingIntent.FLAG_CANCEL_CURRENT))
				.setWhen(System.currentTimeMillis()).setContentTitle(title) // title
				.setDefaults(Notification.DEFAULT_ALL);
		Notification notification = notificationBuiled.getNotification();
		notificationManager.notify(1, notification);
	}

	private Class<? extends Activity> getCheckKnowledgeActivity() {
		final Class<? extends Activity> cls;
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		int checkMode = settings.getInt(Extras.CHECK_MODE, 1);
		if (checkMode == SettingsActivity.CHECK_KNOWLEDGE_TAP) {
			cls = CheckingKnowledgeActivity.class;
		} else {
			cls = CheckKnowledgeSwipeActivity.class;
		}
		return cls;
	}

	private boolean toSend() {
		getFromPreferences();
		int hour = getHour();
		if (!notRemind && timeoutHours != -1 && startHours != -1
				&& endHours != -1) {
			return (isWords() && startHours <= hour && endHours >= hour);
		} else {
			return false;
		}
	}

	/*
	 * get current hour
	 */
	private int getHour() {
		Calendar calendar_hour = Calendar.getInstance(Locale.getDefault());
		calendar_hour.setTime(new Date());
		return calendar_hour.get(Calendar.HOUR_OF_DAY);
	}

	private boolean isWords() {
		calendar = Calendar.getInstance(Locale.getDefault());
		calendar.setTime(new Date());
		clearCalendar();
		Long fromDate = calendar.getTimeInMillis();
		Long toDate = fromDate + LocalDataProvider.MILLSINDAY;
		return dataProvider.hasAnyWords(fromDate, toDate);
	}

	private void clearCalendar() {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.AM_PM, 0);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	public void getFromPreferences() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		timeoutHours = settings.getInt(BaseActivity.TIMEOUT, 1);
		startHours = settings.getInt(BaseActivity.HOURS_START, 8);
		endHours = settings.getInt(BaseActivity.HOURS_END, 20);
		notRemind = settings.getBoolean(BaseActivity.NOT_REMIND, false);
	}
}