package com.office;

public class UserInfo {
	private String name = null;
	private String gender = null;
	private String userID = null;
	private String userPass = null;
	private String birth = null;
	private String email = null;
	private String memberGrade = null;
	
	public UserInfo() {
		name = "����";
		gender = "����";
		userID = "����";
		userPass = "����";
		birth = "����";
		email = "����";
		memberGrade = "ȭ��Ʈ";
	}
	
	public void setName(String name) {this.name = name;}
	public void setGender(String gender) {this.gender = gender;}
	public void setUserID(String userID) {this.userID = userID;}
	public void setUserPass(String userPass) {this.userPass = userPass;}
	public void setBirth(String birth) {this.birth = birth;}
	public void setEmail(String email) {this.email = email;}
	
	public String getUserName() {return name;}
	public String getUserID() {return userID;}
	public String getMemberGrade() {return memberGrade;}
	
	public boolean isIDAlreadyExist(String userID) { //���̵� �ߺ� üũ
		if(this.userID.equals(userID))
			return true;
		else
			return false;
	}
	public boolean isUserExist(String userID, String userPass) { //�α��ν� ���̵� ��� ��� �´��� Ȯ��
		if(this.userID.equals(userID) && this.userPass.equals(userPass))
			return true;
		else
			return false;
	}
	public String getUserInfo() { //��й�ȣ ������ �������� ����
		String output = "���̵�: " + userID + "\n�̸�: " + name + "\n����: " + gender + "\n�������: " + birth + "\n�̸���: " + email + "\nȸ�����: " + memberGrade;
		return output;
	}
}
