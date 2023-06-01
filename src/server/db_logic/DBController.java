package server.db_logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBController {
	
	private static DBController controller = null;
	
	private static Connection conn;
	private static String dbName, user, password;
	private static boolean connected = false;
	
	public static DBController getDBControllerInstanceFor(String givenDbName, String givenUser, String givenPassword) {
		if(givenDbName.equals(dbName) && givenUser.equals(user) && givenPassword.equals(password)) {
			if(controller == null) {
				controller = new DBController(givenDbName, givenUser, givenPassword);
			}
			return controller;
		}
		if(connected) {
			disconnectFromDB();
		}
		controller = new DBController(givenDbName, givenUser, givenPassword);
		return controller;
	}

	private DBController(String dbName, String user, String password) {
		DBController.dbName = dbName;
		DBController.password = password;
		DBController.user = user;
	}
	
	public void connectToDBServer() {
		if (connected)
			return;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
			return;
		}
		try {

			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/" + dbName + "?serverTimezone=IST", user,password);
			System.out.println("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return;
		}
		connected = true;
	}
	
	public Connection getDBConnection() {
		return conn;
	}
	
	public String getDBName() {
		return dbName;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public static void disconnectFromDB() {
		try {
			if(connected)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connected = false;
	}
	
	
	
}
