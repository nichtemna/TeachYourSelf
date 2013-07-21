package com.qarea.mlfw.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceManager extends BroadcastReceiver {

	Context mContext;
	private final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// All registered broadcasts are received by this
		mContext = context;
		String action = intent.getAction();
		if (action.equalsIgnoreCase(BOOT_ACTION)) {
			// check for boot complete event & start your service
			startService();
		}

	}

	private void startService() {
		// here, you will start your service
		mContext.startService(new Intent(mContext, NotificationService.class));
	}
}