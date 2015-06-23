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
import com.bhavit.pnrexpress.model.Passenger;
 
public class CustomListViewAdapterPassengers extends ArrayAdapter<Passenger> {
 
    Context context;
    int rid;
 
    public CustomListViewAdapterPassengers(Context context, int resourceId,
            List<Passenger> items) {
        super(context, resourceId, items);
        rid = resourceId;
        this.context = context;
    }
 
    /*private view holder class*/
    private class ViewHolder {

        TextView serialNo;
        TextView bookingStatus;
        TextView currentStatus;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Passenger rowItem = getItem(position);
 
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(rid, null);
            holder = new ViewHolder();
            holder.serialNo = (TextView) convertView.findViewById(R.id.serialNo);
            holder.bookingStatus = (TextView) convertView.findViewById(R.id.bookingStatus);
            holder.currentStatus = (TextView) convertView.findViewById(R.id.currentStatus);
    		
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/gothicRegular.TTF");
        
        holder.serialNo.setText(rowItem.getName().toUpperCase(Locale.getDefault()));
        holder.bookingStatus.setText(rowItem.getStatusBefore().toUpperCase(Locale.getDefault()));
        holder.currentStatus.setText(rowItem.getStatusAfter().toUpperCase(Locale.getDefault()));
        
        holder.serialNo.setTypeface(tf);
        holder.bookingStatus.setTypeface(tf);
        holder.currentStatus.setTypeface(tf);
        
 
        return convertView;
    }
}