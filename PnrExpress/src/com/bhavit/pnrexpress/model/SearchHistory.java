package com.bhavit.pnrexpress.model;

public class SearchHistory {
	
	String stationFrom;
	String stationTo;
	
	public SearchHistory() {
		super();

	}
	public SearchHistory(String stationFrom, String stationTo) {
		super();
		this.stationFrom = stationFrom;
		this.stationTo = stationTo;
	}
	public String getStationFrom() {
		return stationFrom;
	}
	public void setStationFrom(String stationFrom) {
		this.stationFrom = stationFrom;
	}
	public String getStationTo() {
		return stationTo;
	}
	public void setStationTo(String stationTo) {
		this.stationTo = stationTo;
	}
	

}
