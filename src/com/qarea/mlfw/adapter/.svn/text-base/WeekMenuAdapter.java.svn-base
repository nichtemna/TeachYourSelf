package com.qarea.mlfw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qarea.mlfw.DBHelper.DBSchedule;
import com.qarea.mlfw.R;
import com.qarea.mlfw.activity.WeekViewActivity;
import com.qarea.mlfw.util.WeekProgress;

public class WeekMenuAdapter extends ArrayAdapter<WeekProgress> {

	private LayoutInflater inflater;
	private WeekProgress[] weekList;
	private Context context;

	public WeekMenuAdapter(Context context, WeekProgress[] weekList) {
		super(context, R.layout.raw_week_menu, weekList);
		this.weekList = weekList;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout rawView = (LinearLayout) inflater.inflate(
				R.layout.raw_week_menu, parent, false);
		TextView rawText = (TextView) rawView
				.findViewById(R.id.raw_week_menu_text);
		rawText.setText(weekList[position].getWeekName());
		rawView.setOnClickListener(new WeekClick(weekList[position]
				.getWeekFirstDay()));
		return rawView;
	}

	private class WeekClick implements OnClickListener {

		private long date;

		public WeekClick(long date) {
			this.date = date;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, WeekViewActivity.class);
			intent.putExtra(DBSchedule.DATE, date);
			context.startActivity(intent);
		}

	}

}
