package com.bhavit.pnrexpress.model;

public class Train {

	String trainName;
	String trainNo;
	String fromStation;
	String toStation;
	String departureTime;
	String ArrivalTime;
	String travelTime;
	String runsOn;
	String classes;
	String searchQuery;
	

	public Train(String trainName, String trainNo, String fromStation,
			String toStation, String departureTime, String arrivalTime,
			String travelTime, String runsOn, String classes, String searchQuery) {
		super();
		this.trainName = trainName;
		this.trainNo = trainNo;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.departureTime = departureTime;
		ArrivalTime = arrivalTime;
		this.travelTime = travelTime;
		this.runsOn = runsOn;
		this.searchQuery = searchQuery;
		this.classes = classes;
	}
	public String getTrainName() {
		return trainName;
	}
	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public String getFromStation() {
		return fromStation;
	}
	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}
	public String getToStation() {
		return toStation;
	}
	public void setToStation(String toStation) {
		this.toStation = toStation;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public String getArrivalTime() {
		return ArrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		ArrivalTime = arrivalTime;
	}
	public String getTravelTime() {
		return travelTime;
	}
	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}
	public String getRunsOn() {
		return runsOn;
	}
	public void setRunsOn(String runsOn) {
		this.runsOn = runsOn;
	}
	public String getSearchQuery() {
		return searchQuery;
	}
	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	

}
