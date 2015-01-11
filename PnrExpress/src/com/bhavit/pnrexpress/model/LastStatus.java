package com.bhavit.pnrexpress.model;

public class LastStatus {

	String pnrNumber;
	String lastStatus;
	public String getPnrNumber() {
		return pnrNumber;
	}
	public void setPnrNumber(String pnrNumber) {
		this.pnrNumber = pnrNumber;
	}
	public String getLastStatus() {
		return lastStatus;
	}
	public void setLastStatus(String lastStatus) {
		this.lastStatus = lastStatus;
	}
	public LastStatus(String pnrNumber, String lastStatus) {
		super();
		this.pnrNumber = pnrNumber;
		this.lastStatus = lastStatus;
	}
	
}
