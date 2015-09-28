package com.bhavit.pnrexpress.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhavit.pnrexpress.R;
import com.bhavit.pnrexpress.model.LiveTrainRunningStatus;

public class CustomListAdapterLiveRunningStatus extends
		ArrayAdapter<LiveTrainRunningStatus> {

	Context context;
	int rid;

	public CustomListAdapterLiveRunningStatus(Context context, int resourceId,
			List<LiveTrainRunningStatus> items) {
		super(context, resourceId, items);
		rid = resourceId;
		this.context = context;
	}

	/* private view holder class */
	private class ViewHolder {

		ImageView imageTrain;
		ImageView imageRail;
		TextView station;
		TextView platform;
		TextView sArrival;
		TextView sDeparture;
		TextView aArrival;
		TextView aDeparture;
		TextView trainStatus;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		LiveTrainRunningStatus rowItem = getItem(position);
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(rid, null);
			holder = new ViewHolder();
			holder.imageTrain = (ImageView) convertView
					.findViewById(R.id.train_icon);
			holder.imageRail = (ImageView) convertView
					.findViewById(R.id.rail_icon);
			holder.station = (TextView) convertView.findViewById(R.id.station);
			holder.platform = (TextView) convertView
					.findViewById(R.id.tv_platformno);
			holder.sArrival = (TextView) convertView
					.findViewById(R.id.tv_scheduled_arrival_time);
			holder.sDeparture = (TextView) convertView
					.findViewById(R.id.tv_scheduled_departure_time);
			holder.aArrival = (TextView) convertView
					.findViewById(R.id.tv_actual_arrival_time);
			holder.aDeparture = (TextView) convertView
					.findViewById(R.id.tv_actual_departure_time);
			holder.trainStatus = (TextView) convertView
					.findViewById(R.id.tv_trainstatus);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.station.setText(rowItem.getStation().toUpperCase());
		holder.platform.setText("PLATFORM NO.: " + rowItem.getPlatform());
		holder.sArrival.setText("SCHEDULED ARRIVAL: " + rowItem.getsArrival());
		holder.sDeparture.setText("SCHEDULED DEPARTURE: "
				+ rowItem.getsDeparture());
		holder.aArrival.setText("ACTUAL ARRIVAL: " + rowItem.getaArrival());
		holder.aDeparture.setText("ACTUAL DEPARTURE: "
				+ rowItem.getaDeparture());
		holder.trainStatus.setText("TRAIN STATUS: " + rowItem.getTrainStatus());

		holder.imageRail.setBackgroundResource(rowItem.getBackground());

		holder.imageTrain.setVisibility(rowItem.getTrainIcon());

		return convertView;
	}
}