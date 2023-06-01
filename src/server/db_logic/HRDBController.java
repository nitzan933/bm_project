package server.db_logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utility.entity.BusinessClient;
import utility.enums.DataType;
import utility.enums.RequestType;

// TODO: Auto-generated Javadoc
/**
 * The Class HRDBController.
 */
public class HRDBController {
	
/** The Constant business_client. */
private final static String business_client = "business_client";
		
		/** The Constant Users. */
		private final static String Users = "users";
		
		/** The Constant HR_businessId. */
		private final static String HR_businessId = "businesses";
	
		/** The DB connection. */
		static Connection dbConnection;
		
		/** The DB name. */
		static String dbName;
	
	/**
	 * Instantiates a new HRDB controller.
	 *
	 * @param dbController the db controller
	 */
	public HRDBController(DBController dbController) {
		this.dbConnection = dbController.getDBConnection();
		this.dbName = dbController.getDBName();
	}
	
		
	/**
	 * Update users in DB.
	 *
	 * @param usersToUpdate the users to update
	 * @return true, if successful
	 */
	public static boolean updateUsersInDB(ArrayList<String> usersToUpdate){
	
		
		PreparedStatement ps;
		PreparedStatement ps1;
		PreparedStatement ps2;
		System.out.println(usersToUpdate.get(3));
		if(usersToUpdate.get(3).equals("true")) {
			try {
					String query = "UPDATE `" + dbName + "`." +  business_client +
								" SET isApproved = ?" +
								" WHERE userId = ?";
					ps = dbConnection.prepareStatement(query);
					ps.setInt(1, 1);
					ps.setInt(2, Integer.parseInt(usersToUpdate.get(2)));
					ps.executeUpdate();
					
					String query1 = "UPDATE `" + dbName + "`." + Users +
									" SET status = ?" +
									" WHERE userId = ?";
					
					ps2 = dbConnection.prepareStatement(query1);
					ps2.setString(1, "active");
					ps2.setInt(2, Integer.parseInt(usersToUpdate.get(2)));
					ps2.executeUpdate();
					
									
					
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
		}
		else {
			try {
				String query = "DELETE FROM `" + dbName + "`." +  business_client +
								" WHERE userId = ?";
				ps = dbConnection.prepareStatement(query);
				ps.setInt(1, Integer.parseInt(usersToUpdate.get(2)));
				ps.executeUpdate();
				
				String query1 = "UPDATE `" + dbName + "`." +  Users +
								" SET status = ?" +
								" WHERE userId = ?";
				ps1 = dbConnection.prepareStatement(query1);	
				ps1.setString(1, "unregistered");
				ps1.setInt(2, Integer.parseInt(usersToUpdate.get(2)));
				ps1.executeUpdate();
				
			}
			 catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * Gets the users that needs to be approved from DB.
	 *
	 * @param hrId the HR id
	 * @return the users to approve from DB
	 */
	public static ArrayList<String> getUsersToApproveFromDB(int hrId){
		ArrayList<String> userListToApprove = new ArrayList<>();
		
		userListToApprove.add(RequestType.SERVER_MESSAGE_TO_CLIENT_DATA_PROVIDED.toString());
		userListToApprove.add(DataType.HR_MANAGER.toString());
		
		BusinessClient tempUser;
		PreparedStatement ps;
		PreparedStatement ps2;
		PreparedStatement ps3;

		
		try {
			String query = "SELECT U.userId, U.firstName, U.phoneNumber, U.email "
					+ "FROM `" + dbName + "`."+  HR_businessId +" HRB ,`" + dbName + "`."+  business_client +" BC ,`" + dbName +"`." + Users + " U "
					+ "WHERE HRB.hr_Id = ? AND HRB.businessId = BC.businessId AND BC.userId = U.userId AND BC.isApproved = 0 AND U.status = ?";
			
//			String query = "SELECT business_Id"
//					+ " FROM `" + dbName +"`." + HR_businessId +
//					" WHERE hr_Id = ?";
//			
			ps = dbConnection.prepareStatement(query);
			ps.setInt(1, hrId);
			ps.setString(2, "wait");
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				userListToApprove.add(String.valueOf(rs.getInt(1)));
				userListToApprove.add(rs.getString(2));
				userListToApprove.add(rs.getString(3));
				userListToApprove.add(rs.getString(4));
			}
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return userListToApprove;
	}
	
	/**
	 * Approve business.
	 *
	 * @param hr_id the HR id
	 * @return true, if successful
	 */
	public static boolean approveBusiness(int hr_id){
		
		PreparedStatement ps;
		
		try {
				String query = "UPDATE `" + dbName + "`.businesses B" +
							" SET B.isApproved = ?" +
							" WHERE hr_Id = ?";
				ps = dbConnection.prepareStatement(query);
				ps.setInt(1, 1);
				ps.setInt(2, hr_id);
				ps.executeUpdate();
								
				
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		
		return true;
	}
	
	/**
	 * Check if the business is approved
	 *
	 * @param hr_id the HR id
	 * @return the status of the business
	 */
	public static int CheckApproveBusiness(int hr_id){
		
		PreparedStatement ps;
		ResultSet rs;
		int isApproved = 0;
		
		try {
				String query = "SELECT B.isApproved "+
						 	"FROM `" + dbName + "`." +  HR_businessId + " B" +
							" WHERE B.hr_Id = ?";
				ps = dbConnection.prepareStatement(query);
				ps.setInt(1, hr_id);
				rs = ps.executeQuery();
				rs.next();
				isApproved = rs.getInt(1);
				rs.close();								
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return isApproved;
	}
	
	/**
	 * Gets the approved users.
	 *
	 * @param hrId the HR id
	 * @return the approved users
	 */
	public static ArrayList<String> getApprovedUsers(int hrId){
		ArrayList<String> userListToApprove = new ArrayList<>();
		
		userListToApprove.add(RequestType.SERVER_MESSAGE_TO_CLIENT_DATA_PROVIDED.toString());
		userListToApprove.add(DataType.HR_MANAGER.toString());
		
		BusinessClient tempUser;
		PreparedStatement ps;
		PreparedStatement ps2;
		PreparedStatement ps3;

		
		try {
			String query = "SELECT U.userId, U.firstName, U.phoneNumber, U.email "
					+ "FROM `" + dbName + "`."+  HR_businessId +" HRB ,`" + dbName + "`."+  business_client +" BC ,`" + dbName +"`." + Users + " U "
					+ "WHERE HRB.hr_Id = ? AND HRB.businessId = BC.businessId AND BC.userId = U.userId AND BC.isApproved = 1";
			
//			String query = "SELECT business_Id"
//					+ " FROM `" + dbName +"`." + HR_businessId +
//					" WHERE hr_Id = ?";
//			
			ps = dbConnection.prepareStatement(query);
			ps.setInt(1, hrId);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				userListToApprove.add(String.valueOf(rs.getInt(1)));
				userListToApprove.add(rs.getString(2));
				userListToApprove.add(rs.getString(3));
				userListToApprove.add(rs.getString(4));
			}
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return userListToApprove;
	}

}
