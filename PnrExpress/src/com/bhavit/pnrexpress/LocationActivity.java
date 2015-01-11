package com.bhavit.pnrexpress;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.bhavit.pnrexpress.BaseActivity;
import com.bhavit.pnrexpress.model.Station;
import com.bhavit.pnrexpress.util.CustomListViewAdapterStationsList;

public class LocationActivity extends FragmentActivity implements LocationListener {
	// private MyLocationOverlay myLocationOverlay;

	// private OnLocationChangedListener listener;
	LocationManager locationManager;
	GoogleMap map;
	String provider;
	Marker meMarker;
	Button whereAmI;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		whereAmI = (Button)findViewById(R.id.button1);
		whereAmI.setTypeface(BaseActivity.tf);
		whereAmI.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Criteria criteria = new Criteria();
				provider = locationManager.getBestProvider(criteria, true);
				if(provider != null){
					locationManager.requestLocationUpdates(provider, 500, 1, LocationActivity.this);
					Location location = locationManager.getLastKnownLocation(provider);

					if (location != null) {
						System.out.println("Provider " + provider + " has been selected.");

						float lat = (float) (location.getLatitude());
						float lng = (float) (location.getLongitude());

						LatLng ll=new LatLng(lat, lng);
						
						if(meMarker != null){
							meMarker.remove();
						}

						meMarker = map.addMarker(new MarkerOptions().position(ll)
								.title("me").icon(BitmapDescriptorFactory
										.fromResource(R.drawable.blue_marker)));	


						map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 12), 2000, null);

					} else {
						Toast.makeText(getApplicationContext(), "Location not available",
								Toast.LENGTH_LONG).show();
						// latituteField.setText("Location not available");
						// longitudeField.setText("Location not available");
					}

				} else {
				Toast.makeText(getApplicationContext(), "Location Provider not available",
							Toast.LENGTH_LONG).show();
					
				}
			}
		});

		TextView heading = (TextView) findViewById(R.id.textView_heading);
		heading.setTypeface(BaseActivity.tf);

		final HashMap<String, LatLng> locMap = new HashMap<String, LatLng>();
		final HashMap<String, Marker> markerMap = new HashMap<String, Marker>();
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();

		final String pnrNo = this.getIntent().getStringExtra("pnr");
		final ArrayList<Station> l = BaseActivity.sqlHelper.getTrainRoute(pnrNo);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				double lat = 0;
				double lon = 0;
				
				for (final Station got : l) {

					if(got.getLatitude().equals("null") || got.getLongitude().equals("null")){

						Geocoder g = new Geocoder(getApplicationContext());
						try {
							Address add = g.getFromLocationName(got.getStationName(), 1).get(0);
							lon = 	add.getLongitude();
							lat = add.getLatitude();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{


						lat = Double.parseDouble(got.getLatitude());
						lon = Double.parseDouble(got.getLongitude());

					}

					final String name = got.getStationName();

					final LatLng loc = new LatLng(lat, lon);
					locMap.put(got.getStationNo(), loc); // for listview use

					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {

							Marker m = map.addMarker(new MarkerOptions().position(loc).title(
									"STATION: "+name+"("+got.getStationCode()+")").snippet("From "+got.getArrivalTime()+" to "+got.getDepartureTime()+" ("+got.getStopTime()+")"));
							markerMap.put(got.getStationNo(), m);

							// Zoom in, animating the camera.
							if(got.getStationNo().equals("1")){
								map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15), 2000, null);
								m.showInfoWindow();
							}
							
						}
					});
					

				}
				
			}
		}).start();

		

		ListView stationsList = (ListView)findViewById(R.id.stations_list);
		CustomListViewAdapterStationsList adapter = new CustomListViewAdapterStationsList(getApplicationContext(), R.layout.custom_list_item_stations_list, l);
		stationsList.setAdapter(adapter);
		stationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {

				LatLng clickedLoc = locMap.get(String.valueOf(arg2+1));

				map.animateCamera(CameraUpdateFactory.newLatLngZoom(clickedLoc, 15), 2000, new GoogleMap.CancelableCallback() {

					@Override
					public void onFinish() {
						markerMap.get(String.valueOf(arg2+1)).showInfoWindow();

					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub

					}
				});
			}
		});

	}

	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		//locationManager.requestLocationUpdates(provider, 400, 1, this);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		/*
		float lat = (float) (location.getLatitude());
		float lng = (float) (location.getLongitude());
		LatLng ll = new LatLng(lat, lng);
		if (meMarker != null) {
			meMarker.remove();
		}

		Marker meMarker = map.addMarker(new MarkerOptions()
				.position(ll)
				.title("me")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.blue_marker)));
		 */
		// latituteField.setText(String.valueOf(lat));
		// longitudeField.setText(String.valueOf(lng));
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

}
