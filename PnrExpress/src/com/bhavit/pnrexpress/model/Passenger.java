package com.bhavit.pnrexpress.model;

public class Passenger {
	
	String name;
	String statusBefore;
	String statusAfter;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatusBefore() {
		return statusBefore;
	}
	public void setStatusBefore(String statusBefore) {
		this.statusBefore = statusBefore;
	}
	public String getStatusAfter() {
		return statusAfter;
	}
	public void setStatusAfter(String statusAfter) {
		this.statusAfter = statusAfter;
	}
	public Passenger(String name, String statusBefore, String statusAfter) {
		super();
		this.name = name;
		this.statusBefore = statusBefore;
		this.statusAfter = statusAfter;
	}
	
	

}
