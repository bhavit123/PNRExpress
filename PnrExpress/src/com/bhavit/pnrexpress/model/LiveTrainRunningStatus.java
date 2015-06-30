package com.bhavit.pnrexpress.model;

import android.graphics.Color;
import android.view.View;

public class LiveTrainRunningStatus {
	
	String station;
	String platform;
	String sArrival;
	String sDeparture;
	String aArrival;
	String aDeparture;
	String trainStatus;
	int trainIcon = View.INVISIBLE;
	int background = Color.TRANSPARENT;
	public LiveTrainRunningStatus(String station, String platform,
			String sArrival, String sDeparture, String aArrival,
			String aDeparture, String trainStatus) {
		super();
		this.station = station;
		this.platform = platform;
		this.sArrival = sArrival;
		this.sDeparture = sDeparture;
		this.aArrival = aArrival;
		this.aDeparture = aDeparture;
		this.trainStatus = trainStatus;
	}
	
	public int getTrainIcon() {
		return trainIcon;
	}

	public void setTrainIcon(int trainIcon) {
		this.trainIcon = trainIcon;
	}

	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getsArrival() {
		return sArrival;
	}
	public void setsArrival(String sArrival) {
		this.sArrival = sArrival;
	}
	public String getsDeparture() {
		return sDeparture;
	}
	public void setsDeparture(String sDeparture) {
		this.sDeparture = sDeparture;
	}
	public String getaArrival() {
		return aArrival;
	}
	public void setaArrival(String aArrival) {
		this.aArrival = aArrival;
	}
	public String getaDeparture() {
		return aDeparture;
	}
	public void setaDeparture(String aDeparture) {
		this.aDeparture = aDeparture;
	}
	public String getTrainStatus() {
		return trainStatus;
	}
	public void setTrainStatus(String trainStatus) {
		this.trainStatus = trainStatus;
	}
	
}
