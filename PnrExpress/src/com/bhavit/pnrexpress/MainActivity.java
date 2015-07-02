package com.bhavit.pnrexpress;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bhavit.pnrexpress.fragment.CommunityFragment;
import com.bhavit.pnrexpress.fragment.PagesFragment;
import com.bhavit.pnrexpress.fragment.PnrFragment;
import com.bhavit.pnrexpress.fragment.SeatAvailabilityFragment;
import com.bhavit.pnrexpress.fragment.SeatlayoutFragment;
import com.bhavit.pnrexpress.fragment.WhatsHotFragment;
import com.bhavit.pnrexpress.model.NavDrawerItem;
import com.bhavit.pnrexpress.util.AppHelper;
import com.bhavit.pnrexpress.util.NavDrawerListAdapter;

public class MainActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	int selectedPosition = 0;
	ArrayList<String> list;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		list = new ArrayList<String>();

		JSONArray trains;
		try {
			trains = new JSONArray(AppHelper.loadJSONFromAsset(this, "trains.json"));

			for(int i = 0; i<trains.length(); i++){
				JSONObject train = trains.getJSONObject(i);
				String trainName = train.getString("trainName");
				String trainCode = train.getString("trainNo");
				list.add(trainName + "- " + trainCode);

			}

		} catch (JSONException e) {

			e.printStackTrace();
		}

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Pnr Status
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// Seat Availability
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Train Route
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		//Seat layout
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		// Communities, Will add a counter here
		/*
		 * navDrawerItems.add(new NavDrawerItem(navMenuTitles[3],
		 * navMenuIcons.getResourceId(3, -1), true, "22")); // Pages
		 * navDrawerItems.add(new NavDrawerItem(navMenuTitles[4],
		 * navMenuIcons.getResourceId(4, -1))); // What's hot, We will add a
		 * counter here navDrawerItems.add(new NavDrawerItem(navMenuTitles[5],
		 * navMenuIcons.getResourceId(5, -1), true, "50+"));
		 */

		// Recycle the typed array
		navMenuIcons.recycle();
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(MainActivity.this, navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.string.app_name, // nav drawer open - description for
				// accessibility
				R.string.app_name // nav drawer close - description for
				// accessibility
				) {
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				// invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				// invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new PnrFragment();
			break;
		case 1:
			fragment = new SeatAvailabilityFragment();
			break;
		case 2:
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
					android.R.layout.simple_dropdown_item_1line, list);
			AppHelper.openAutoCompleteInputDialog(MainActivity.this, "Select Train", "Enter Train name or number",adapter, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (AppHelper.inputDialogAutoCompletetv.getText().toString().contains("-")){
						if (!AppHelper.inputDialogAutoCompletetv.getText().toString().equals("")) {
							
							Intent i = new Intent(MainActivity.this, LocationActivity.class);
							i.putExtra("tnum", AppHelper.inputDialogAutoCompletetv.getText().toString()
									.split("-")[1].trim());
							startActivity(i);
							
						} else {
							
							AppHelper.toast(MainActivity.this, "Select a train first.");
						}
					} else {
						AppHelper.toast(MainActivity.this, "Select train from options.");
					}
		
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);// to toggle softkeyboard open/close
				}
			});
			mDrawerLayout.closeDrawer(mDrawerList);
			break;
		case 3:
			fragment = new SeatlayoutFragment();
			break;
		case 4:
			fragment = new PagesFragment();
			break;
		case 5:
			fragment = new WhatsHotFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			selectedPosition = position;
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		// getActionBar().setTitle(mTitle);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {

		if (selectedPosition != 0) {
			displayView(0);
		} else {
			super.onBackPressed();
		}
	}
}
