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
import com.bhavit.pnrexpress.model.PnrDetail;

public class CustomListViewAdapterCheckedPnrs extends ArrayAdapter<PnrDetail> {
	 
    Context context;
    int rid;
 
    public CustomListViewAdapterCheckedPnrs(Context context, int resourceId,
            List<PnrDetail> items) {
        super(context, resourceId, items);
        rid = resourceId;
        this.context = context;
    }
 
    /*private view holder class*/
    private class ViewHolder {

        TextView pnrNum;
        TextView fromStation;
        TextView toStation;
        TextView doj;
        ImageView icon;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        PnrDetail rowItem = getItem(position);
 
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(rid, null);
            holder = new ViewHolder();
            holder.pnrNum = (TextView) convertView.findViewById(R.id.pnr_num);
            holder.fromStation = (TextView) convertView.findViewById(R.id.from_st);
            holder.toStation = (TextView) convertView.findViewById(R.id.to_st);
            holder.doj = (TextView) convertView.findViewById(R.id.date_of_journey);
            holder.icon = (ImageView) convertView.findViewById(R.id.train_icon);
    		
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/gothicRegular.TTF");
        
        holder.pnrNum.setText("PNR: "+rowItem.getPnrNumber().toUpperCase(Locale.getDefault()));
        holder.fromStation.setText("FROM: "+rowItem.getBoardingStationName()+"("+rowItem.getBoardingStationCode().toUpperCase(Locale.getDefault())+")");
        holder.toStation.setText("TO: "+rowItem.getReservationUptoStationName()+"("+rowItem.getReservationUptoStationCode().toUpperCase(Locale.getDefault())+")");
        holder.doj.setText("DOJ: "+rowItem.getDateOfJourney().toUpperCase(Locale.getDefault()));
        holder.icon.setImageResource(R.drawable.list_train_1);
        
        holder.pnrNum.setTypeface(tf);
        holder.fromStation.setTypeface(tf);
        holder.toStation.setTypeface(tf);
        holder.doj.setTypeface(tf);
 
        return convertView;
    }
}
