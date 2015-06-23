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
import android.widget.TextView;

import com.bhavit.pnrexpress.R;
import com.bhavit.pnrexpress.model.Station;

public class CustomListViewAdapterStationsList extends ArrayAdapter<Station> {

	Context context;
	int rid;

	public CustomListViewAdapterStationsList(Context context, int resourceId,
			List<Station> items) {
		super(context, resourceId, items);
		rid = resourceId;
		this.context = context;
	}

	/*private view holder class*/
	private class ViewHolder {

		TextView station;
		TextView stationNo;
		TextView arrivalTime;
		TextView departureTime;
		TextView haltTime;
		TextView distanceCovered;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Station rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(rid, null);
			holder = new ViewHolder();
			holder.station = (TextView) convertView.findViewById(R.id.station);
			holder.stationNo = (TextView) convertView.findViewById(R.id.station_no);
			holder.arrivalTime = (TextView) convertView.findViewById(R.id.arrival_time);
			holder.departureTime = (TextView) convertView.findViewById(R.id.departure_time);
			holder.haltTime = (TextView) convertView.findViewById(R.id.halt_time);
			holder.distanceCovered = (TextView) convertView.findViewById(R.id.distance_covered);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/gothicRegular.TTF");

		holder.station.setText(rowItem.getStationName().toUpperCase(Locale.getDefault())+"("+rowItem.getStationCode().toUpperCase(Locale.getDefault())+")");
		holder.stationNo.setText(rowItem.getStationNo());
		holder.arrivalTime.setText("ARRIVAL: "+rowItem.getArrivalTime());
		holder.departureTime.setText("DEPARTURE: "+rowItem.getDepartureTime());
		holder.haltTime.setText("HALT-TIME: "+rowItem.getStopTime());
		holder.distanceCovered.setText("DISTANCE-COVERED: "+rowItem.getDistance());

		holder.station.setTypeface(tf);
		holder.arrivalTime.setTypeface(tf);
		holder.departureTime.setTypeface(tf);
		holder.haltTime.setTypeface(tf);
		holder.distanceCovered.setTypeface(tf);

		return convertView;
	}
}
