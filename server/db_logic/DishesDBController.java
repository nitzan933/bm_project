package server.db_logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import server.exceptions.BMServerException;
import utility.entity.Dish;
import utility.enums.ErrorType;

public class DishesDBController {

	private final String dishesTableNameInDB = "dishes";
	
	Connection dbConnection;
	String dbName;
	
	public DishesDBController(DBController dbController) {
		this.dbConnection = dbController.getDBConnection();
		this.dbName = dbController.getDBName();
	}
	
	public ArrayList<Dish> GetDishesListFromResIDData(String resID) throws BMServerException {
		ArrayList<Dish> result = new ArrayList<>();
		PreparedStatement ps;
		try {
			String query = "SELECT * FROM  `"+ dbName + "`." + dishesTableNameInDB +
					" WHERE resID = '" + resID + "'";
			ps = dbConnection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()) {//if restaurant doesn't exist 
				throw new BMServerException(ErrorType.DISHES_NOT_FOUND, "no dishes found");
			}
			
			//DB holds: a list of:[dish_ID|description|size|cooking_level|res_ID|price|name
			//we don't want to return some fields back to client side after authentication, so we leave them blank
			while(true) {
				result.add(new Dish(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8)));
				if(!rs.next()) break;
			}
			
			rs.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Dish  AddDishToMenu(Dish dish) throws BMServerException{
		ResultSet rs;
		PreparedStatement ps = null;
		try {
				String query = "SELECT * FROM `"+ dbName + "`." + dishesTableNameInDB
						+ " WHERE dishId = (SELECT MAX(dishId) From `"+ dbName + "`." + dishesTableNameInDB +")";
				ps = dbConnection.prepareStatement(query);
				rs = ps.executeQuery();
				rs.next();
				int count = rs.getInt(1);
				rs.close();
				int id = count+1;
				String query2 = "INSERT INTO `" + dbName + "`." + dishesTableNameInDB + "(dishId,resId,type,name,description,"
						+	"size,CookingLevel,price)" + " VALUES(?,?,?,?,?,?,?,?)";
				PreparedStatement ps2 = dbConnection.prepareStatement(query2);
				ps2.setInt(1,id);
				ps2.setString(2, dish.getRes_ID());
				ps2.setString(3, dish.getType());
				ps2.setString(4, dish.getName());
				ps2.setString(5, dish.getDescription());
				ps2.setString(6, dish.getSize());
				ps2.setString(7, dish.getCooking_level());
				ps2.setString(8, dish.getPrice());
				ps2.executeUpdate();
				ps2.close();
				
			return new Dish(String.valueOf(id), dish.getRes_ID(), dish.getType(), dish.getName(), dish.getDescription(), 
					dish.getSize(), dish.getCooking_level(), dish.getPrice());
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void UpdateDishInMenu(Dish dish) throws BMServerException{
		try {
			String query2 = "UPDATE `" + dbName + "`." + dishesTableNameInDB
					+ " SET type = ?, name = ?, description = ?, size = ?, cookingLevel = ?,"
					+ " price = ? WHERE dishId = ?";
				PreparedStatement ps2 = dbConnection.prepareStatement(query2);
				ps2.setString(1,dish.getType());
				ps2.setString(2, dish.getName());
				ps2.setString(3, dish.getDescription());
				ps2.setString(4, dish.getSize());
				ps2.setString(5, dish.getCooking_level());
				ps2.setString(6, dish.getPrice());
				ps2.setInt(7, Integer.parseInt(dish.getDish_ID()));
				
				ps2.executeUpdate();
				ps2.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void DeleteDishFromMenu(Dish dish) throws BMServerException{
		try {
			String query2 = "DELETE FROM `" + dbName + "`." + dishesTableNameInDB
					+ " WHERE dishId = ?";
				PreparedStatement ps2 = dbConnection.prepareStatement(query2);
				ps2.setInt(1, Integer.parseInt(dish.getDish_ID()));
				ps2.executeUpdate();
				ps2.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	

}
