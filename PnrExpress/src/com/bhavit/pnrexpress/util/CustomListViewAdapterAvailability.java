package com.bhavit.pnrexpress.util;

import java.util.List;
import java.util.Locale;

import com.bhavit.pnrexpress.R;
import com.bhavit.pnrexpress.model.Availability;
import com.bhavit.pnrexpress.model.Passenger;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListViewAdapterAvailability extends ArrayAdapter<Availability> {

	Context context;
	int rid;

	public CustomListViewAdapterAvailability(Context context, int resourceId,
			List<Availability> items) {
		super(context, resourceId, items);
		rid = resourceId;
		this.context = context;
	}

	/*private view holder class*/
	private class ViewHolder {

		TextView date;
		TextView availability;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Availability rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(rid, null);
			holder = new ViewHolder();
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.availability = (TextView) convertView.findViewById(R.id.availability);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/gothicRegular.TTF");

		holder.date.setText(rowItem.getDate());
		holder.availability.setText(rowItem.getAvailability());

		holder.date.setTypeface(tf);
		holder.availability.setTypeface(tf);


		return convertView;
	}
}