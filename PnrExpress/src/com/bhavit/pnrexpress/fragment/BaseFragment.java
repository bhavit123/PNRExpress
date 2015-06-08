package com.bhavit.pnrexpress.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bhavit.pnrexpress.R;

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
