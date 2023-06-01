package server.db_logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import server.exceptions.BMServerException;
import utility.entity.Client;
import utility.enums.ErrorType;
import utility.enums.UserType;

public class ClientDBController {
	
	private final String personalClientTableInDB = "personal_client";
	private final String businesslClientTableInDB = "business_client";
	private final String businessesTableInDB = "businesses";
	private final String userTableNameInDB = "users";
	private final String w4cTableNameInDB = "w4c";
	Connection dbConnection;
	String dbName;
	
	public ClientDBController(DBController dbController) {
		this.dbConnection = dbController.getDBConnection();
		this.dbName = dbController.getDBName();
		
	}
	/*This method set a new client in db, first check if the information about this user
	 * exist in the user table, if not return error message with throwing exception.
	 * Otherwise enter this user into client table depends which client he is.
	 * 
	 * @param client with all the data for registration **/
	
	public void setNewClient(Client client)  throws BMServerException{
		String userEmail = client.getEmail(), firstName = client.getFirstName(), lastName = client.getLastName();
		String phone = client.getPhone();
		int id = client.getUser_ID();
		UserType userType = client.getUserType();
		String status = userType.toString().equals("CLIENT_PERSONAL") ? "active" : "wait";
		String personalBranch = client.getPersonalBranch();
		ResultSet rs = null;
		PreparedStatement ps;
		String w4c;
		try {//check if user exist
			String query = "SELECT * FROM  `"+ dbName + "`." + userTableNameInDB + " WHERE  userTZ = ? AND firstName = ?"
					+ " AND lastName = ? AND  email = ? AND  phoneNumber = ? ";
			ps = dbConnection.prepareStatement(query);
			ps.setInt(1, id);
			ps.setString(2,firstName);
			ps.setString(3,lastName);
			ps.setString(4,userEmail);
			ps.setString(5,phone);
			rs = ps.executeQuery();
			if(!rs.next()) {
				throw new BMServerException(ErrorType.INVALID_CREDENTIALS_USER_NOT_FOUND, "USER DOESN'T EXIST");
			}
			//
			id = rs.getInt(1);
			//
			//if user exists and we are here: let's find what is the next available W4C in the system
			query = "SELECT * FROM `" + dbName + "`." + w4cTableNameInDB;
			ps = dbConnection.prepareStatement(query);
			rs = ps.executeQuery();
			if(!rs.last()) {
				w4c = "a100";
			} else {
				w4c =  rs.getString(1);
				int lastW4C = Integer.parseInt(w4c.replaceAll("[^0-9]", ""));
				w4c = "a" + String.valueOf(lastW4C + 1);
			}
			query = "UPDATE `" + dbName + "`." + userTableNameInDB + " SET userType = ?  , personalBranch = ?  , status = ? , w4c = ? WHERE userTZ = ?";
			ps = dbConnection.prepareStatement(query);
			ps.setString(1, userType.toString());
			ps.setString(2, personalBranch);
			ps.setString(3, status);
			ps.setString(4, w4c);
			ps.setInt(5, client.getUser_ID());
			ps.executeUpdate();
			if(userType == UserType.CLIENT_PERSONAL) {
				query = "INSERT INTO `" + dbName + "`." + personalClientTableInDB + "(personalCode,userId,balance,personalCreditNumber) VALUES(?,?,?,?)";
				ps = dbConnection.prepareStatement(query);
//				ps.setInt(1,client.getUser_ID()*2);
//				ps.setInt(2, client.getUser_ID());
				ps.setInt(1,id*2);
				ps.setInt(2, id);
				ps.setInt(3, 0);
				ps.setInt(4, client.getPersonalCreditNumber());
				ps.executeUpdate();
				rs.close();
				ps.close();
				
				
			}else if(userType == UserType.CLIENT_BUSINESS) {
				int personalCode = 0,balance = 0;
				ResultSet rs1;
				PreparedStatement  ps1;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				query = "SELECT * FROM  `"+ dbName + "`." + personalClientTableInDB + " WHERE userID = ?";
				ps1 = dbConnection.prepareStatement(query);
//				ps1.setInt(1, client.getUser_ID());
				ps1.setInt(1, id);
				rs1 = ps1.executeQuery();
				if(rs1.next()) {//if user exist, delete him 
					personalCode = rs1.getInt(1);
					balance = rs1.getInt(3);
//					query = "DELETE FROM `" + dbName + "`." + personalClientTableInDB + " WHERE userID = ?";
//					ps1 = dbConnection.prepareStatement(query);
//					ps1.setInt(1, client.getUser_ID());
//					ps1.execute();
				}
				//check if the business is approved
				query = "SELECT * FROM  `"+ dbName + "`." + businessesTableInDB + " WHERE businessID = ?";
				ps1 = dbConnection.prepareStatement(query);
				ps1.setInt(1, client.getBusinessId());
				rs1 = ps1.executeQuery();
				if(!rs1.next()) { //business not in the table 
					throw new BMServerException(ErrorType.BUSINESS_DOESNT_EXIST, "BUSINESS_DOESNT_EXIST");
				}
				if(rs1.getInt(3) != 2) {
					throw new BMServerException(ErrorType.BUSINESS_NOT_APPROVE, "BUSINESS_NOT_APPROVE");
				}
				query = "INSERT INTO `" + dbName + "`." + businesslClientTableInDB + "(businessCode,personalCode,balanceInApp,userID,budget,isApproved,businessId,personalCreditNumber) VALUES(?,?,?,?,?,?,?,?)";
				PreparedStatement ps2 = dbConnection.prepareStatement(query);
//				ps2.setInt(1,client.getUser_ID()*3);
				ps2.setInt(1,id*3);
				ps2.setInt(2,personalCode);
				ps2.setInt(3, balance);
//				ps2.setInt(4, client.getUser_ID());
				ps2.setInt(4, id);
				ps2.setInt(5, client.getBudget());
				ps2.setInt(6, 0);
				ps2.setInt(7, client.getBusinessId());
				ps2.setInt(8, client.getPersonalCreditNumber());
				ps2.executeUpdate();
				ps2.close();
			}
			//updating w4c table:
			if(userType == UserType.CLIENT_BUSINESS) {
				query = "INSERT INTO `" + dbName + "`." + w4cTableNameInDB + "(w4cCode, personalCode, businessCode) VALUES(?,?,?)";
			} else {
				query = "INSERT INTO `" + dbName + "`." + w4cTableNameInDB + "(w4cCode, personalCode) VALUES(?,?)";
			}
			ps = dbConnection.prepareStatement(query);
			ps.setString(1,w4c);
			ps.setInt(2, id*2);
			if(userType == UserType.CLIENT_BUSINESS) {
				ps.setInt(3, id*3);
			}
			ps.execute();
			ps.close();
			rs.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new BMServerException(ErrorType.CLIENT_ALREADY_EXIST, "CLIENT_ALREADY_EXIST");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getClientInfo(String branchName) throws BMServerException {
		ResultSet rs = null;
		PreparedStatement ps;
		ArrayList<String> clientList = new ArrayList<>();
		try
			{
			String query = "SELECT * FROM  `"+ dbName + "`." + userTableNameInDB + " WHERE personalBranch = ? AND (userType = ? OR userType = ?) AND (status = ? OR status = ?)";
			ps = dbConnection.prepareStatement(query);
			ps.setString(1, branchName);
			ps.setString(2, "CLIENT_PERSONAL");
			ps.setString(3, "CLIENT_BUSINESS");
			ps.setString(4,"frozen");
			ps.setString(5,"active");
			rs = ps.executeQuery();
			if(!rs.next()) {
				throw new BMServerException(ErrorType.NO_CLIENTS_IN_THIS_BRANCH, "NO_CLIENTS_IN_THIS_BRANCH");
			}
			clientList.add(rs.getString(2));
			clientList.add(rs.getString(3));
			clientList.add(branchName);
			clientList.add(rs.getString(8));
			clientList.add(rs.getString(1));
			while(rs.next()) {
			clientList.add(rs.getString(2));
			clientList.add(rs.getString(3));
			clientList.add(branchName);
			clientList.add(rs.getString(8));
			clientList.add(rs.getString(1));
			}
			return clientList;
			}catch(SQLException e) {
				e.printStackTrace();
				throw new BMServerException(ErrorType.NO_CLIENTS_IN_THIS_BRANCH, "NO_CLIENTS_IN_THIS_BRANCH");
			}
	}


	public void changePermissionRequest(String newStatus, String id) throws BMServerException {
		ResultSet rs = null;
		PreparedStatement ps;
		try {
			String query = "UPDATE `" + dbName + "`." + userTableNameInDB + " SET status = ? WHERE userId = ?";
			ps = dbConnection.prepareStatement(query);
			ps.setString(1, newStatus);
			ps.setInt(2, Integer.parseInt(id));
			ps.executeUpdate();
			if(newStatus.equals("unregistered")) {
				query = "SELECT userType FROM  `"+ dbName + "`." + userTableNameInDB + " WHERE userId = ?";
				ps = dbConnection.prepareStatement(query);
				ps.setInt(1, Integer.parseInt(id));
				rs = ps.executeQuery();
				rs.next();
				if(rs.getString(1).equals("CLIENT_PERSONAL")) {
					query = "DELETE FROM `"+ dbName + "`." + personalClientTableInDB + " WHERE userId = ?";
					ps = dbConnection.prepareStatement(query);
					ps.setInt(1, Integer.parseInt(id));
					ps.execute();
				}else {
					query = "DELETE FROM `"+ dbName + "`." + businesslClientTableInDB + " WHERE userId = ?";
					PreparedStatement ps2 = dbConnection.prepareStatement(query);
					ps2.setInt(1, Integer.parseInt(id));
					ps2.execute();
				}
			}
		} catch (SQLException e) {
			throw new BMServerException(ErrorType.UNKNOWN,"somthing went wrong");
		}
	
		
	}

}
