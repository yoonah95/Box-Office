package com.office;

public class PreorderInfo {
	private String title = null;
	private String screen = null;
	private String grade = null;
	private String seat = null;
	private String userID = null;
	private String paymentMethod = null;
	
	public PreorderInfo(TicketInfo ticketInfo, String userID) {
		title = ticketInfo.getTitle();
		screen = ticketInfo.getScreen();
		grade = ticketInfo.getGrade();
		seat = ticketInfo.getSeat();
		this.userID = userID;
		this.paymentMethod = ticketInfo.getPaymentMethod();
	}
	
	public boolean userCheck(String userID) {
		if(this.userID.equals(userID))
			return true;
		else
			return false;
	}
	
	public String getPreorderInfo() {
		String output = "영화 제목: " + title + "\n상영관: " + screen + "\n관람등급: " + grade + "\n좌석: " + seat;
		return output;
	}
}
