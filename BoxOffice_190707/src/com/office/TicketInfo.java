package com.office;

public class TicketInfo {
	private String title = null;
	private String screen = null;
	private String time = null;
	private String grade = null;
	private String age = null;
	private String seat = null;
	private String paymentMethod = null;
	
	public TicketInfo() {
		title = "없음";
		screen = "없음";
		time = "없음";
		grade = "없음";
		age = "없음";
		seat = "없음";
	}
	
	public void setTitle(String title) {this.title = title;}
	public void setScreen(String screen) {this.screen = screen;}
	public void setTime(String time) {this.time = time;}
	public void setGrade(String grade) {this.grade = grade;}
	public void setAge(String age) {this.age = age;}
	public void setSeat(String seat) {this.seat = seat;}
	public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod;}

	public String getTitle() {return title;}
	public String getScreen() {return screen;}
	public String getTime() {return time;}
	public String getGrade() {return grade;}
	public String getAge() {return age;}
	public String getSeat() {return seat;}
	public String getPaymentMethod() {	return paymentMethod;}
	
	public String getTicketInfo() {
		String output = "영화 제목: " + title + "\n상영관: " + screen + "\n시간: " + time + "\n관람등급: " + grade + "\n나이: " + age + "\n좌석: " + seat;
		return output;
	}
}
