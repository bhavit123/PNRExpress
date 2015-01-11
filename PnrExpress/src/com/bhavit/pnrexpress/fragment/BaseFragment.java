package com.bhavit.pnrexpress.fragment;

import com.bhavit.pnrexpress.R;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BaseFragment extends Fragment {
	
	

	public void setDrawerAndSettingsButtons(View rootView){
		
		final DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
		Button drawerToggle = (Button) rootView.findViewById(R.id.drawerButton);
		drawerToggle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				drawer.openDrawer(Gravity.LEFT);
				
			}
		});
		
	}
}
