package main;

import java.sql.*;

public class DatabaseLoader {
	private String url;
	private String user;
	private String passWord;
	private Connection db;
	
	/**
	 * 
	 * @param url the URL of data base.
	 * @param user the user name
	 * @param passWord the pass word to access
	 */
	public DatabaseLoader(String url, String user, String passWord) {
		this.url = url;
		this.user = user;
		this.passWord = passWord;
	}
	
	/**
	 * 
	 * @return a Connection type, use it to access database
	 */
	public Connection connectToDatabase() {
		try {
			db = DriverManager.getConnection(url, user, passWord);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Failed!!!");
			e.printStackTrace();
			return null;
		}
		System.out.println("Connection Success!!!");
		return db;
	}
	
	/**
	 * 
	 * @return return true if connection is closed
	 */
	public Boolean closeConnection() {
		try {
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Close failed!!!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
