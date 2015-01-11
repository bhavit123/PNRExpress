package com.bhavit.pnrexpress.model;

public class PnrDetail {
	
	String pnrNumber;
	String trainName;
	String trainNumber;
	String fromStationName;
	String fromStationCode;
	String toStationName;
	String toStationCode;
	String boardingStationName;
	String boardingStationCode;
	String reservationUptoStationName;
	String reservationUptoStationCode;
	String dateOfJourney;
	String reservationClass;
	
	
	public PnrDetail(String pnrNumber, String trainName, String trainNumber,
			String fromStationName, String fromStationCode,
			String toStationName, String toStationCode,
			String boardingStationName, String boardingStationCode,
			String reservationUptoStationName,
			String reservationUptoStationCode, String dateOfJourney,
			String reservationClass) {
		super();
		this.pnrNumber = pnrNumber;
		this.trainName = trainName;
		this.trainNumber = trainNumber;
		this.fromStationName = fromStationName;
		this.fromStationCode = fromStationCode;
		this.toStationName = toStationName;
		this.toStationCode = toStationCode;
		this.boardingStationName = boardingStationName;
		this.boardingStationCode = boardingStationCode;
		this.reservationUptoStationName = reservationUptoStationName;
		this.reservationUptoStationCode = reservationUptoStationCode;
		this.dateOfJourney = dateOfJourney;
		this.reservationClass = reservationClass;
	}
	public PnrDetail() {
		// TODO Auto-generated constructor stub
	}
	public String getPnrNumber() {
		return pnrNumber;
	}
	public void setPnrNumber(String pnrNumber) {
		this.pnrNumber = pnrNumber;
	}
	public String getTrainName() {
		return trainName;
	}
	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}
	public String getTrainNumber() {
		return trainNumber;
	}
	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}
	public String getFromStationName() {
		return fromStationName;
	}
	public void setFromStationName(String fromStationName) {
		this.fromStationName = fromStationName;
	}
	public String getFromStationCode() {
		return fromStationCode;
	}
	public void setFromStationCode(String fromStationCode) {
		this.fromStationCode = fromStationCode;
	}
	public String getToStationName() {
		return toStationName;
	}
	public void setToStationName(String toStationName) {
		this.toStationName = toStationName;
	}
	public String getToStationCode() {
		return toStationCode;
	}
	public void setToStationCode(String toStationCode) {
		this.toStationCode = toStationCode;
	}
	public String getBoardingStationName() {
		return boardingStationName;
	}
	public void setBoardingStationName(String boardingStationName) {
		this.boardingStationName = boardingStationName;
	}
	public String getBoardingStationCode() {
		return boardingStationCode;
	}
	public void setBoardingStationCode(String boardingStationCode) {
		this.boardingStationCode = boardingStationCode;
	}
	public String getReservationUptoStationName() {
		return reservationUptoStationName;
	}
	public void setReservationUptoStationName(String reservationUptoStationName) {
		this.reservationUptoStationName = reservationUptoStationName;
	}
	public String getReservationUptoStationCode() {
		return reservationUptoStationCode;
	}
	public void setReservationUptoStationCode(String reservationUptoStationCode) {
		this.reservationUptoStationCode = reservationUptoStationCode;
	}
	public String getDateOfJourney() {
		return dateOfJourney;
	}
	public void setDateOfJourney(String dateOfJourney) {
		this.dateOfJourney = dateOfJourney;
	}
	public String getReservationClass() {
		return reservationClass;
	}
	public void setReservationClass(String reservationClass) {
		this.reservationClass = reservationClass;
	}
	
	

}
