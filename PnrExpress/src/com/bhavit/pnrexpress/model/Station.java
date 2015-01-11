package com.bhavit.pnrexpress.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Station implements Parcelable
{
	private String pnrNumber;
	private String stationName;
	private String stationCode;
	private String stationNo; 	
	private String arrivalTime;
	private String departureTime;
	private String stopTime;
	private String day;
	private String distance;
	private String latitude;
	private String longitude;
	
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getStationNo() {
		return stationNo;
	}
	public void setStationNo(String stationNo) {
		this.stationNo = stationNo;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public String getStopTime() {
		return stopTime;
	}
	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Station(String pnrNumber, String stationName, String stationCode, String stationNo, String arrivalTime,
			String departureTime, String stopTime, String day, String distance,
			String latitude, String longitude) {
		super();
		this.pnrNumber = pnrNumber;
		this.stationName = stationName;
		this.stationCode = stationCode;
		this.stationNo = stationNo;
		this.arrivalTime = arrivalTime;
		this.departureTime = departureTime;
		this.stopTime = stopTime;
		this.day = day;
		this.distance = distance;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public Station() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(pnrNumber);
		dest.writeString(stationName);
		dest.writeString(stationCode);
		dest.writeString(stationNo);
		dest.writeString(latitude);
		dest.writeString(longitude);
		dest.writeString(arrivalTime);
		dest.writeString(departureTime);
		dest.writeString(stopTime);
		dest.writeString(day);
		dest.writeString(distance);

	}
	public String getPnrNumber() {
		return pnrNumber;
	}
	public void setPnrNumber(String pnrNumber) {
		this.pnrNumber = pnrNumber;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public static final Parcelable.Creator<Station> CREATOR = 
			new Parcelable.Creator<Station>() { 
		public Station createFromParcel(Parcel in) { 
			Station category = new Station();
			category.pnrNumber = in.readString();
			category.stationName = in.readString();
			category.stationCode = in.readString();
			category.stationNo = in.readString();	
			category.latitude = in.readString();
			category.longitude = in.readString();
			category.arrivalTime = in.readString();
			category.departureTime = in.readString();
			category.stopTime = in.readString();
			category.day = in.readString();
			category.distance = in.readString();

			return category;
		}

		@Override
		public Station[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}

	};
}
