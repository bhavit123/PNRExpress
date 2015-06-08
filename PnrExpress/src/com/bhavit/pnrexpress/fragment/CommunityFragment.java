package com.bhavit.pnrexpress.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhavit.pnrexpress.R;

public class CommunityFragment extends BaseFragment {
	
	public CommunityFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
         
        setDrawerAndSettingsButtons(rootView);
        
        return rootView;
    }
}
