package com.bhavit.pnrexpress.adapters;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhavit.pnrexpress.R;
import com.bhavit.pnrexpress.model.Train;

public class CustomListViewAdapterTrainsList extends ArrayAdapter<Train> {

	Context context;
	int rid;
	public CustomListViewAdapterTrainsList(Context context, int resourceId,
			List<Train> items) {
		super(context, resourceId, items);
		rid = resourceId;
		this.context = context;
	}

	/*private view holder class*/
	private class ViewHolder {

		TextView trainNameNum;
		TextView arrivalTime;
		TextView departureTime;
		//TextView travelTime;
		ImageView icon;
		TextView monday;
		TextView tuesday;
		TextView wednesday;
		TextView thursday;
		TextView friday;
		TextView saturday;
		TextView sunday;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Train rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(rid, null);
			holder = new ViewHolder();
			holder.trainNameNum = (TextView) convertView.findViewById(R.id.train);
			holder.arrivalTime = (TextView) convertView.findViewById(R.id.arrival_time);
			holder.departureTime = (TextView) convertView.findViewById(R.id.departure_time);
			//holder.travelTime = (TextView) convertView.findViewById(R.id.travel_time);
			holder.icon = (ImageView) convertView.findViewById(R.id.train_icon);
			holder.monday = (TextView) convertView.findViewById(R.id.monday);
			holder.tuesday = (TextView) convertView.findViewById(R.id.tuesday);
			holder.wednesday = (TextView) convertView.findViewById(R.id.wednesday);
			holder.thursday = (TextView) convertView.findViewById(R.id.thursday);
			holder.friday = (TextView) convertView.findViewById(R.id.friday);
			holder.saturday = (TextView) convertView.findViewById(R.id.saturday);
			holder.sunday = (TextView) convertView.findViewById(R.id.sunday);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/gothicRegular.TTF");

		holder.trainNameNum.setText(rowItem.getTrainName().toUpperCase(Locale.getDefault())+"("+rowItem.getTrainNo()+")");
		holder.arrivalTime.setText("ARRIVAL: "+rowItem.getArrivalTime());
		holder.departureTime.setText("DEPARTURE: "+rowItem.getDepartureTime());
		//holder.travelTime.setText("TRAVEL-TIME: "+rowItem.getTravelTime());
		holder.icon.setImageResource(R.drawable.list_train_1);

		holder.trainNameNum.setTypeface(tf);
		holder.arrivalTime.setTypeface(tf);
		holder.departureTime.setTypeface(tf);
		//holder.travelTime.setTypeface(tf);

		holder.monday.setBackgroundColor(rowItem.getRunsOnColor()[0]);

		holder.tuesday.setBackgroundColor(rowItem.getRunsOnColor()[1]);

		holder.wednesday.setBackgroundColor(rowItem.getRunsOnColor()[2]);

		holder.thursday.setBackgroundColor(rowItem.getRunsOnColor()[3]);

		holder.friday.setBackgroundColor(rowItem.getRunsOnColor()[4]);

		holder.saturday.setBackgroundColor(rowItem.getRunsOnColor()[5]);

		holder.sunday.setBackgroundColor(rowItem.getRunsOnColor()[6]);


		return convertView;
	}
}

