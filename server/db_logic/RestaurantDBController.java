package server.db_logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import server.exceptions.BMServerException;
import utility.entity.Restaurant;
import utility.entity.User;
import utility.enums.ErrorType;


public class RestaurantDBController {
	private final String restaurantTableNameInDB = "restaurant";
	
	Connection dbConnection;
	String dbName;
	
	public RestaurantDBController(DBController dbController) {
		this.dbConnection = dbController.getDBConnection();
		this.dbName = dbController.getDBName();
	}
	
	public ArrayList<Restaurant> GetRestaurantsListFromSearchData(ArrayList<String> searchText) throws BMServerException{
		ArrayList<Restaurant> result = new ArrayList<>();
		PreparedStatement ps;
		try {
			ArrayList<String> searchBranch = (ArrayList<String>)searchText;
			String query = "SELECT * FROM  `"+ dbName + "`." + restaurantTableNameInDB +
					" WHERE resName LIKE '%" + searchBranch.get(0) + "%' AND branch = '" + searchBranch.get(1) + "'";
			ps = dbConnection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()) {//if restaurant doesn't exist 
				throw new BMServerException(ErrorType.INVALID_CREDENTIALS_RESTAURANT_NOT_FOUND, "no restaurants found");
			}
			
			//DB holds: a list of:[resID|resName|category|branch]
			//we don't want to return some fields back to client side after authentication, so we leave them blank
			while(true) {
				result.add(new Restaurant(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
				if(!rs.next()) break;
			}
			
			rs.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Restaurant> GetRestaurantsListFromCategoriesData(ArrayList<String> category) throws BMServerException {
		ArrayList<Restaurant> result = new ArrayList<>();
		PreparedStatement ps;
		try {
			ArrayList<String> categoryBranch = (ArrayList<String>)category;
			String query = "SELECT * FROM  `"+ dbName + "`." + restaurantTableNameInDB +
					" WHERE category = '" + categoryBranch.get(0) + "' AND branch = '" + categoryBranch.get(1) + "'";
			ps = dbConnection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()) {//if restaurant doesn't exist 
				throw new BMServerException(ErrorType.INVALID_CREDENTIALS_RESTAURANT_NOT_FOUND, "no restaurants found");
			}
			
			//DB holds: a list of:[resID|resName|category|branch]
			//we don't want to return some fields back to client side after authentication, so we leave them blank
			while(true) {
				result.add(new Restaurant(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
				if(!rs.next()) break;
			}
			
			rs.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Restaurant GetRestaurantFromUserSupplierData(User supplier) throws BMServerException {
		String res_workersTableNameInDB = "res_workers";
		Restaurant result;
		PreparedStatement ps;
		try {
			int supplierID = supplier.getUser_ID();
			String query_getResID = "SELECT * FROM  `"+ dbName + "`." + res_workersTableNameInDB +
					" WHERE userId = '" + supplierID + "'";
			ps = dbConnection.prepareStatement(query_getResID);
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()) {//if restaurant doesn't exist 
				throw new BMServerException(ErrorType.INVALID_CREDENTIALS_RESTAURANT_NOT_FOUND, "no restaurants found");
			}
			String resID = rs.getString(2);
			String query_getRes = "SELECT * FROM  `"+ dbName + "`." + restaurantTableNameInDB +
					" WHERE resId = '" + resID + "'";
			ps = dbConnection.prepareStatement(query_getRes);
			ResultSet rs2 = ps.executeQuery();
			
			if(!rs2.next()) {//if restaurant doesn't exist 
				throw new BMServerException(ErrorType.INVALID_CREDENTIALS_RESTAURANT_NOT_FOUND, "no restaurants found");
			}
			//DB holds:[resID|resName|category|branch]
			//we don't want to return some fields back to client side after authentication, so we leave them blank
			result = new Restaurant(rs2.getString(1),rs2.getString(2),rs2.getString(3),rs2.getString(4));		
			
			rs.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	
	}

	

}
