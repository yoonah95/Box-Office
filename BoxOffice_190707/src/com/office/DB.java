package com.office;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.pool.OracleDataSource;

public class DB {

	OracleDataSource ods = null;
	Connection con = null;
	PreparedStatement stmt = null;
	String sql = null;

	ResultSet rs = null;

	public DB() {
		try {
			ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
			ods.setUser("wacher");
			ods.setPassword("qkrqk951");
			con = ods.getConnection();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public ArrayList movieList() {
		sql = "select name from movie";
		ArrayList<String> li = new ArrayList<String>();
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString(1);
				System.out.println(name);
				li.add(name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return li;

	}

	public Boolean movieShow(String name) {
		String result = null;
		sql = "select show from movie where name = ?";
		try {
			stmt = con.prepareStatement(sql);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getString(1);
				System.out.println(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (result.equals("»ó¿µÁß"))
			return true;
		else
			return false;
	}
}
