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
		title = "����";
		screen = "����";
		time = "����";
		grade = "����";
		age = "����";
		seat = "����";
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
		String output = "��ȭ ����: " + title + "\n�󿵰�: " + screen + "\n�ð�: " + time + "\n�������: " + grade + "\n����: " + age + "\n�¼�: " + seat;
		return output;
	}
}
