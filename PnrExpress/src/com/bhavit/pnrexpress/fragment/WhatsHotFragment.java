package com.bhavit.pnrexpress.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhavit.pnrexpress.R;

public class WhatsHotFragment extends BaseFragment {
	
	public WhatsHotFragment(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_whats_hot, container, false);
         
        setDrawerAndSettingsButtons(rootView);
        
        return rootView;
    }
}
