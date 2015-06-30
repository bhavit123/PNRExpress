package com.bhavit.pnrexpress.model;

public class Availability {
	
	String date;
	String availability;
	public Availability(String date, String availability) {
		super();
		this.date = date;
		this.availability = availability;
	}
	public Availability(){
		
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	
	

}
