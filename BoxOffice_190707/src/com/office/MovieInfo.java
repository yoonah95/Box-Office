package com.office;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.pool.OracleDataSource;

public class MovieInfo {
	private String title = null;
	private String genre = null;
	private String synopsis = null;
	private String grade = null;
	private String cast = null;
	private String runningTime = null;
	private String country = null;
	private String score = null;
	private String ranking = null;
	private String imgPath = null;

	OracleDataSource ods = null;
	Connection con = null;
	PreparedStatement stmt = null;
	String sql = null;

	ResultSet rs = null;

	public MovieInfo(String title) {
		this.title = title;

		try {
			ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
			ods.setUser("wacher");
			ods.setPassword("qkrqk951");
			con = ods.getConnection();

		} catch (Exception e) {
			System.out.println(e);
		}

		if (title.equals("영화 없음")) {
			genre = "없음";
			synopsis = "해당 영화 없음";
			grade = "해당 영화 없음";
			cast = "없음";
			runningTime = "없음";
			country = "없음";
			imgPath = "img/poster.png";
		} else {
			sql = "select * from movie where name = ?";
			try {
				stmt = con.prepareStatement(sql);
				stmt.setString(1, title);
				rs = stmt.executeQuery();
				while (rs.next()) {
					genre = rs.getString(4);
					synopsis = rs.getString(5);
					grade = rs.getString(6);
					cast = rs.getString(7);
					runningTime = rs.getString(8);
					country = rs.getString(10);
					score = rs.getString(11);
					ranking = rs.getString(12);
					imgPath = "img/poster01.jpg";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String getTitle() {
		return title;
	}

	public String getGenre() {
		return genre;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public String getGrade() {
		return grade;
	}

	public String getCast() {
		return cast;
	}

	public String getRunningTime() {
		return runningTime;
	}

	public String getCountry() {
		return country;
	}

	public String getScore() {
		return score;
	}

	public String getRanking() {
		return ranking;
	}

	public String getImgPath() {
		return imgPath;
	}
}
