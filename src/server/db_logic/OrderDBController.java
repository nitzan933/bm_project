package server.db_logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import server.exceptions.BMServerException;
import utility.entity.ClientRefundsData;
import utility.entity.Dish;
import utility.entity.Order;
import utility.enums.OrderType;

public class OrderDBController {
  private final String orderTableNameInDB = "orders";
	private final String refundsTableNameInDB = "refunds";
	private final String dishInOrderTableNameInDB = "dish_in_order";
	
	private final String ordersTableNameInDB = "orders";
	
	Connection dbConnection;
	String dbName;
	
	public OrderDBController(DBController dbController) {
		this.dbConnection = dbController.getDBConnection();
		this.dbName = dbController.getDBName();
	}
	
	public Boolean moveOrder(String orderId, String status) {
		PreparedStatement ps;
		PreparedStatement ps1;
		PreparedStatement ps3;
		PreparedStatement ps4;
		PreparedStatement ps5;
		PreparedStatement ps6;
		try {
			String query = "UPDATE `" + dbName + "`." +  ordersTableNameInDB +
							" SET status = ?" +
							" WHERE orderId = ?";
			ps = dbConnection.prepareStatement(query);
			ps.setString(1, status);
			ps.setInt(2, Integer.parseInt(orderId));
			ps.executeUpdate();			
			if(status.equals("in the kitchen")) {
                String query1 = "UPDATE `" + dbName + "`." +  ordersTableNameInDB +
                            " SET timeOfApproval = CURRENT_TIMESTAMP" +
                            " WHERE orderId = ?";
                ps1 = dbConnection.prepareStatement(query1);
                ps1.setInt(1, Integer.parseInt(orderId));
                ps1.executeUpdate();
            }
			if(status.equals("done")) {
                String query2 = "UPDATE `" + dbName + "`." +  ordersTableNameInDB +
                            " SET timeOfArrival = CURRENT_TIMESTAMP" +
                            " WHERE orderId = ?";
                PreparedStatement ps2 = dbConnection.prepareStatement(query2);
                ps2.setInt(1, Integer.parseInt(orderId));
                ps2.executeUpdate();
                String query3 = "SELECT recieverID, resId,totalPrice FROM  `"+ dbName + "`." + ordersTableNameInDB 
                        + " WHERE orderId = ? AND ((((typeOfOrder=?) OR (typeOfOrder=?)) AND (time_to_sec(TIMEDIFF(timeOfArrival,timeOfApproval))/60>60)) "
                        + " OR (typeOfOrder=? AND (time_to_sec(TIMEDIFF(timeOfArrival,timeOfApproval))/60>20)))";
                    
                    ps3 = dbConnection.prepareStatement(query3);
                    ps3.setInt(1, Integer.parseInt(orderId));
                    ps3.setString(2, OrderType.DELIVERY_REGULAR.toString());
                    ps3.setString(3,  OrderType.DELIVERY_ROBOT.toString());
                    ps3.setString(4,  OrderType.DELIVERY_EARLY.toString());
                    
                    ResultSet rs = ps3.executeQuery();
                    
                    if(rs.next()) {
                        String query4 = "SELECT refundAmount" +
                                " FROM `" + dbName + "`.refunds" +
                                " WHERE userId = ? AND resId=?";
                        
                        ps4 = dbConnection.prepareStatement(query4);
                        ps4.setInt(1, rs.getInt(1));
                        ps4.setInt(2, rs.getInt(2));
                        
                        ResultSet rs1 = ps4.executeQuery();
                        
                        if(rs1.next()) {
                            String query5 = "UPDATE `" + dbName + "`.refunds" +
                                " SET refundAmount =?" +
                                " WHERE orderId = ? AND resId=?";
                            
                            ps5 = dbConnection.prepareStatement(query5);
                            ps5.setFloat(1, (float) (rs.getInt(3) * 0.5));
                            ps5.setInt(2, rs.getInt(1));
                            ps5.setInt(3, rs.getInt(2));
                            
                            ps5.executeUpdate();
                        }
                        else {
                            System.out.println("here");
                            String query6 = "INSERT INTO `bm-db`.refunds (userId,resId,refundAmount) VALUES(?,?,?)";
                            ps6 = dbConnection.prepareStatement(query6);
                            ps6.setFloat(3, (float) (rs.getInt(3) * 0.5));
                            ps6.setInt(1, rs.getInt(1));
                            ps6.setInt(2, rs.getInt(2));
                            
                            ps6.executeUpdate();
                        }
                    }
            }
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Boolean cancelOrder(String orderId) {
		PreparedStatement ps;
		PreparedStatement ps1;
		PreparedStatement ps2;
		PreparedStatement ps3;
		PreparedStatement ps4;
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		
		try {
			String query5 = "SELECT recieverID" +
							" FROM `" + dbName + "`." + ordersTableNameInDB +
							" WHERE orderId = ?";
			ps4 = dbConnection.prepareStatement(query5);
			ps4.setInt(1, Integer.parseInt(orderId));
			rs1 = ps4.executeQuery();
			if(rs1.next()) {
				
				int recieverId = rs1.getInt(1);
				String query2 = "SELECT totalPrice" +
								" FROM `" + dbName + "`." + ordersTableNameInDB +
								" WHERE orderId = ?";
				ps1 = dbConnection.prepareStatement(query2);
				ps1.setInt(1, Integer.parseInt(orderId));
				rs2 = ps1.executeQuery();
				rs2.next();
				int totalPrice = rs2.getInt(1);
				
				String query3 = "SELECT budget" +
								" FROM `" + dbName + "`.business_client BC, `" + dbName + "`." + ordersTableNameInDB + " O"+
								" WHERE O.orderId = ? AND BC.userId = O.recieverID";
				ps2 = dbConnection.prepareStatement(query3);
				ps2.setInt(1, Integer.parseInt(orderId));
				rs3 = ps2.executeQuery();
				rs3.next();
				int budget = rs3.getInt(1);
				
				String query4 = "UPDATE `" + dbName + "`.business_client" +
								" SET budget = ?" +
								" WHERE userId = ?";
				ps3 = dbConnection.prepareStatement(query4);
				int sum = budget + totalPrice;
				ps3.setInt(1, sum);
				ps3.setInt(2, recieverId);
				ps3.executeUpdate();
			}
			
			String query = "DELETE FROM `" + dbName + "`." +  ordersTableNameInDB +
							" WHERE orderId = ?";
			ps = dbConnection.prepareStatement(query);
			ps.setInt(1, Integer.parseInt(orderId));
			ps.executeUpdate();
			
			String query1= "DELETE FROM `" + dbName + "`.dish_in_order" +
					" WHERE orderId = ?";
			ps1 = dbConnection.prepareStatement(query1);
			ps1.setInt(1, Integer.parseInt(orderId));
			ps1.executeUpdate();
		
			
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
		

	public ArrayList<Order> getOrdersByResId(String resId, String status) throws BMServerException{
		ArrayList<Order> result = new ArrayList<>();
		PreparedStatement ps;
		boolean flag = true;

		try {
			String query = "SELECT * FROM  `"+ dbName + "`." + ordersTableNameInDB +
					" WHERE resID = '" + resId + "' AND status = '" + status +"'"; //get list of all orders in this specific restaurant that are in specific status
			ps = dbConnection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()) {//if there are no orders
//				throw new BMServerException(ErrorType.ORDERS_NOT_FOUND, "no orders found");
				flag = false;
			}
			
		
			while(flag == true) { //for each order
				//saves all dishes in ordes
				String query2 = "SELECT * FROM  `"+ dbName + "`." + "dish_in_order" +
						" WHERE orderId = '" + rs.getInt(1) + "'"; //get list of all dishes in this order
				PreparedStatement ps2 = dbConnection.prepareStatement(query2);
				ResultSet rs2 = ps2.executeQuery();
				
				//saves email of client
				
				String query6 = "SELECT email FROM  `"+ dbName + "`." + "users" +
						" WHERE userId = '" + rs.getInt(11) + "'"; //get list of all dishes in this order
				PreparedStatement ps6 = dbConnection.prepareStatement(query6);
				ResultSet rs6 = ps6.executeQuery();
				
				Order order = new Order(rs.getInt(1), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6),
						rs.getString(7), rs.getString(9), rs.getString(10));
				
				if(rs6.next())
					order.setUserEmail(rs6.getString(1));
				
				rs6.close();
				
				if(!rs2.next()) {//if there are no dishes in this order 
					rs2.close();
					break;
				}
				
				
				while(true) {//for each dish in this order
					String query3 = "SELECT * FROM  `"+ dbName + "`." + "dishes" +
						" WHERE dishId = '" + rs2.getInt(3) + "'"; //get details for this dish
					PreparedStatement ps3 = dbConnection.prepareStatement(query3);
					ResultSet rs3 = ps3.executeQuery();
					if(rs3.next()) { 
						Dish dish = new Dish(rs3.getString(1), rs3.getString(2), rs3.getString(3), rs3.getString(4),
								rs3.getString(5), rs2.getString(5), rs2.getString(6), rs3.getString(8));
						dish.setExceptions(rs2.getString(7));
						order.getDishesInOrder().add(dish);
					} 
					rs3.close();
					if(!rs2.next()) {
						rs2.close();
						break;
					}
					
				}
				
				result.add(order);
				if(!rs.next()) {
					rs.close();
					break;
				}
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	//Added by Semion:
	public void writeOrderDataToDB(Order order) {
		System.out.println("writeOrderDataToDB: " + order.toString());
		try {
			System.out.println("writeOrderDataToDB started");
			String query = "INSERT INTO `" + dbName + "`." + orderTableNameInDB
					+ " (resId,typeOfOrder,address,status,totalPrice,timeOfOrder,nameOfReceiver, phone, orderPrice, orderDeliveryPrice, isPersonal, recieverID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = dbConnection.prepareStatement(query);
			ps.setInt(1, order.getRestaurantID());
			ps.setString(2, order.getTypeOfOrder().toString());
			ps.setString(3, order.getDeliveryAddress());
			ps.setString(4, "wait");
			ps.setInt(5, order.getTotalPrice());
			ps.setString(6,order.getTimeOfOrder());
			String name = order.getUserFirstName() + " " + order.getUserLastName();
			ps.setString(7, name);
			ps.setString(8, order.getUserPhone());
			ps.setInt(9, order.getOrderPrice());
			ps.setInt(10, order.getDeliveryPrice());
			ps.setBoolean(11, order.isPrivateOrder());
			ps.setInt(12, order.getOrderingUserID());
			ps.executeUpdate();
			
			query = "SELECT MAX(orderId) FROM `"+dbName+"`."+orderTableNameInDB+" WHERE resId = ? AND timeOfOrder = ?";
			ps = dbConnection.prepareStatement(query);
			ps.setInt(1, order.getRestaurantID());
			ps.setString(2,order.getTimeOfOrder());
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) {
				System.out.println("writeOrderDataToDB: could not find the order we just added!");
				return;
			}
			int orderID = rs.getInt(1);
			//Important: dish_in_order 'key' should be auto_incremental
			query = "INSERT INTO `" + dbName + "`." + dishInOrderTableNameInDB + " (orderId,dishId,userId,size,cookingLevel,exceptions) VALUES (?,?,?,?,?,?)";
			for(Dish d : order.getDishesInOrder()) {
				ps = dbConnection.prepareStatement(query);
				ps.setInt(1, orderID);
				ps.setString(2, d.getDish_ID());
				ps.setInt(3, order.getOrderingUserID());
				ps.setString(4, d.getSize());
				ps.setString(5, d.getCooking_level());
				ps.setString(6, d.getDescription());
				ps.execute();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e1) {
			System.out.println(e1.getMessage());
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public ClientRefundsData getCRFDataForRestaurantsGiven(ArrayList<String> resIDs) {
		String userID = resIDs.get(0);
		ClientRefundsData result = new ClientRefundsData(Integer.valueOf(resIDs.get(0)));
		PreparedStatement ps;
		try {
			for(int i = 1; i < resIDs.size(); i++) {
				String query = "SELECT refundAmount FROM `"+dbName+"`."+refundsTableNameInDB+" WHERE userID = ? AND resID = ?";
				ps = dbConnection.prepareStatement(query);
				ps.setString(1, userID);
				ps.setString(2, resIDs.get(i));
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) {
					continue;
				}
				result.addResRefundPair(Integer.valueOf(resIDs.get(i)), rs.getInt(1));
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}catch(Exception e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		return result;
	}


	public void updateCRFDataForRestaurantsGiven(ClientRefundsData crf) {
		int userID = crf.getUserID();
		String query;
		PreparedStatement ps;
		try {
			for(int k : crf.getResRefundHashmap().keySet()) {
				if(crf.getRefundByResID(k) > 0) {
					query = "UPDATE `" + dbName + "`." +refundsTableNameInDB + " SET refundAmount = ? WHERE userID = ? AND resID = ?";
					ps = dbConnection.prepareStatement(query);
					ps.setInt(1, crf.getRefundByResID(k));
					ps.setInt(2, userID);
					ps.setInt(3, k);
					ps.execute();
				} else if(crf.getRefundByResID(k) == 0) {
					query = "DELETE FROM `" + dbName + "`." +refundsTableNameInDB + " WHERE userID = ? AND resID = ?";
					ps = dbConnection.prepareStatement(query);
					ps.setInt(1, userID);
					ps.setInt(2, k);
					ps.execute();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch(Exception e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
	}
	
}
	

