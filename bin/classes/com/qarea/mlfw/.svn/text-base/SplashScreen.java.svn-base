package com.qarea.mlfw;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.qarea.mlfw.DBHelper.DBDictionary;
import com.qarea.mlfw.activity.MainMenuActivity;
import com.qarea.mlfw.service.NotificationService;
import com.qarea.mlfw.util.SelectedDictionary;

public class SplashScreen extends BaseActivity {

	private ImageView iv2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		if (dataProvider.getAllDictionaryCount() == 0) {
			dataProvider.insertAssets();
		}
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		int dictionaryID = settings.getInt(SAVE_WORD, -1);
		if (dictionaryID != -1) {
			if (dataProvider.dictionaryIdEnabled(dictionaryID)) {
				SelectedDictionary.setDictionaryID(dictionaryID);
				Cursor selectedDictionaryList = dataProvider
						.getSelectedDictionary();
				while (selectedDictionaryList.moveToNext())
					SelectedDictionary.addDictionary(selectedDictionaryList
							.getInt(selectedDictionaryList
									.getColumnIndex(DBDictionary._ID)));
				selectedDictionaryList.close();

			} else {
				SelectedDictionary.setDictionaryID(-1);
			}
		}
		if (!isMyServiceRunning()) {
			Log.d("tag", "service not running");
			startService(new Intent(this, NotificationService.class));
		}else{
			Log.d("tag", "service is running");
		}

		setAnimation();
	}

	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (NotificationService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private void setAnimation() {
		iv2 = (ImageView) findViewById(R.id.imageView2);
		iv2.setBackgroundResource(R.drawable.progress_indeterminate_horizontal);
		AnimationDrawable frameAnimation = (AnimationDrawable) iv2
				.getBackground();
		iv2.post(new Runnable() {
			@Override
			public void run() {
				AnimationDrawable frameAnimation = (AnimationDrawable) iv2
						.getBackground();
				frameAnimation.start();
			}
		});
		// if animation is finished - start mainMenuActicity
		checkIfAnimationDone(frameAnimation);
	}

	private void checkIfAnimationDone(AnimationDrawable anim) {
		final AnimationDrawable a = anim;
		int timeBetweenChecks = 300;
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
					checkIfAnimationDone(a);
				} else {
					SplashScreen.this.finish();
					Intent mainIntent = new Intent(SplashScreen.this,
							MainMenuActivity.class);
					mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					SplashScreen.this.startActivity(mainIntent);
				}
			}
		}, timeBetweenChecks);
	};
}
