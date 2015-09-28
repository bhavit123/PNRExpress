package com.bhavit.pnrexpress.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bhavit.pnrexpress.R;
import com.bhavit.pnrexpress.model.SearchHistory;

public class CustomListAdapterSearchHistory extends ArrayAdapter<SearchHistory>{

	Context context;
	int rid;
	
	public CustomListAdapterSearchHistory(Context context, int resourceId,
			List<SearchHistory> items) {
		
		super(context, resourceId, items);
		rid = resourceId;
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	/*private view holder class*/
	private class ViewHolder {

		TextView fromStationName;
		TextView fromStationCode;
		TextView toStationName;
		TextView toStationCode;
		TextView to;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		SearchHistory rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(rid, null);
			holder = new ViewHolder();
			holder.fromStationName = (TextView) convertView.findViewById(R.id.textView4);
			holder.fromStationCode = (TextView) convertView.findViewById(R.id.textView5);
			holder.toStationName = (TextView) convertView.findViewById(R.id.textView6);
			holder.toStationCode = (TextView) convertView.findViewById(R.id.textView7);
			holder.to = (TextView) convertView.findViewById(R.id.textView2);
			
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/gothicRegular.TTF");

		holder.fromStationName.setText(rowItem.getStationFrom().split("-")[0]);
		holder.fromStationCode.setText(rowItem.getStationFrom().split("-")[1].trim());
		holder.toStationName.setText(rowItem.getStationTo().split("-")[0]);
		holder.toStationCode.setText(rowItem.getStationTo().split("-")[1].trim());

		holder.fromStationName.setTypeface(tf);
		holder.fromStationCode.setTypeface(tf);
		holder.toStationName.setTypeface(tf);
		holder.toStationCode.setTypeface(tf);
		holder.to.setTypeface(tf);
		
		return convertView;
	}

}
