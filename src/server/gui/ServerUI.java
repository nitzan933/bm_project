package server.gui;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.stage.Stage;
import server.logic.BMServerLogic;

public class ServerUI extends Application {
	
	private static BMServerLogic bmServer;
	
	final public static int DEFAULT_PORT = 5555;
	final public static String DB_NAME = "bm-db"; //name of the schema in MYSQL
	final public static String DB_USER = "root";
	final public static String DB_PASS = "789852";

	public static void main(String[] args) {
		 launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//prepare and call javaFX window
        ServerMainWindowController serverController = new ServerMainWindowController();
        serverController.setServerUI(this);
        serverController.start(primaryStage);
        
	}
	
	//this function needs ServerMainWindowController class to update gui from server logic
	public static boolean startServer(int port, String schemaName, String DBuser, 
			String DBpassword, ServerMainWindowController ctrl) throws Exception {
		if(port <= 0 || port >= 65535 || schemaName == null || DBuser == null || DBpassword == null) {
			//TODO: add error handling
			return false;
		}
		//SERVER INIT:
		bmServer = new BMServerLogic(port, schemaName, DBuser, DBpassword);
		try {
			bmServer.listen(); // Start listening for connections
			bmServer.setGuiController(ctrl); //set gui controller to let logic update gui
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
			throw new Exception("Error Starting server");
		}
		return true;
	}
	
	public static boolean stopServer() throws Exception{
		try {
			if(bmServer == null)
				return false;
			bmServer.close();
		} catch (IOException e) {
			System.out.println("ERROR - Could not stop the server!");
			throw new Exception("Error Stopping server");
		}
		bmServer = null;
		return true;
	}
	
	//----------------LISTEN FUNCTIONS

	public static boolean startServerListening() throws Exception{
		try {
			if(bmServer == null)
				return false;
			bmServer.listen();
		} catch (IOException e) {
			System.out.println("ERROR - Could not listen for clients!");
			throw new Exception("Error Listening to clients");
		}
		return true;
	}
	
	public static boolean stopServerListening() {
		if (bmServer == null)
			return false;
		bmServer.stopListening();
		return true;
	}
	
	//Method added especially for Avi!(Hi Avi)
	//why whould we need to import users!?
	public static void importUsers() {
		if(bmServer == null) {
			return;
		}
        Connection dbConnection = bmServer.getDBController().getDBConnection();
        PreparedStatement ps;
        PreparedStatement ps1;
        try {
            String query = "SELECT * FROM bm-db.manage_users";
            ps = dbConnection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String query1 = "SELECT email,password FROM bm-db.users WHERE email = ?";
                ps1 = dbConnection.prepareStatement(query1);
                ps1.setString(1, rs.getString(4));
                ResultSet rs1 = ps1.executeQuery();
                if(!rs1.next()) { //if the user doesn't exist
                     String query2 = "INSERT INTO bm-db.users (firstName,lastName,email,phoneNumber,userType,status,password,personalBranch,userTZ) VALUES(?,?,?,?,?,?,?,?,?)";
                    ps1 = dbConnection.prepareStatement(query2);
                    ps1.setString(1, rs.getString(1));
                    ps1.setString(2, rs.getString(2));
                    ps1.setString(3, rs.getString(3));
                    ps1.setString(4, rs.getString(4));
                    ps1.setString(5, rs.getString(5));
                    ps1.setString(6, "unregistered");
                    ps1.setString(7, rs.getString(7));
                    ps1.setString(8, rs.getString(6));
                    ps1.setString(9, rs.getString(9));
                    ps1.executeUpdate();
                }
                rs1.close();
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
