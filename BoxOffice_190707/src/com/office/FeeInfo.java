package com.office;

public class FeeInfo {
	private int fundamental = 0;
	private int age = 0;
	private int screen = 0;
	private int memberGrade = 0;
	private String seat = null;
	private String time = null;
	private String week = null;
	
	public FeeInfo() {
		fundamental = 4000;
		seat = "����";
	}
	
	public void setAge(String input) { // ���̿� ���� ����
		switch(input) {
		case "����":
			age = 2000; break;
		case "û�ҳ�":
			age = 1000; break;
		case "���":
			age = 0; break;
		default:
			age = 0;
		}
	}
	
	public void setScreen(String input) { // �󿵰��� ���� ����
		switch(input) {
		case "2D":
			screen = 0; break;
		case "3D":
			screen = 2000; break;
		case "4D":
			screen = 3000; break;
		case "IMAX":
			screen = 3000; break;
		default:
			screen = 0;
		}
	}
	
	public int total() {
		int total = fundamental + age + screen;
		return total;
	}
}
