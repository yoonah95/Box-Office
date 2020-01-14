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
		name = "없음";
		gender = "없음";
		userID = "없음";
		userPass = "없음";
		birth = "없음";
		email = "없음";
		memberGrade = "화이트";
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
	
	public boolean isIDAlreadyExist(String userID) { //아이디 중복 체크
		if(this.userID.equals(userID))
			return true;
		else
			return false;
	}
	public boolean isUserExist(String userID, String userPass) { //로그인시 아이디 비번 모두 맞는지 확인
		if(this.userID.equals(userID) && this.userPass.equals(userPass))
			return true;
		else
			return false;
	}
	public String getUserInfo() { //비밀번호 제외한 유저정보 리턴
		String output = "아이디: " + userID + "\n이름: " + name + "\n성별: " + gender + "\n생년월일: " + birth + "\n이메일: " + email + "\n회원등급: " + memberGrade;
		return output;
	}
}
