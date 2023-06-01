
package utility.entity;

import java.util.ArrayList;

import utility.enums.OrderType;

public class Order {

	private int orderingUserID;
	private int orderID;
	
	private String userFirstName;
	private String userLastName;
	private String fullName;
	private String userPhone;
	private String status;
	private String userEmail;
	
	private String deliveryAddress;
	
	private int restaurantID;
	private String typeOfOrder;
	
	private int totalPrice; 	//dish price + discount
	private String timeOfOrder;
	private String timeOfArrival;

	private ArrayList<Dish> dishesInOrder;
	
	//Added by Semion:
	private boolean isPrivateOrder = true;
	private int orderPrice; 	//price actually paid without delivery
	private int deliveryPrice; 	//price for order delivery paid
	
	public Order(User user,  int restaurantID) {
		this(user.getFirstName(), user.getLastName(), user.getPhone(), "", restaurantID, "");
	}
	
	public Order(User user, String deliveryAddress,  int restaurantID, String typeOfOrder) {
		this(user.getFirstName(), user.getLastName(), user.getPhone(), deliveryAddress, restaurantID, typeOfOrder);
	}
	
	public Order( String userFirstName, String userLastName, String userPhone, String deliveryAddress, int restaurantID, String typeOfOrder) {
		dishesInOrder = new ArrayList<Dish>();
		this.deliveryAddress = deliveryAddress;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userPhone = userPhone;
		this.restaurantID = restaurantID;
		this.typeOfOrder = typeOfOrder;
	}
	

	
	public Order( int orderID, String typeOfOrder, String deliveryAddress, String status, int totalPrice,
			String timeOfOrder, String fullName, String userPhone) {
		
		dishesInOrder = new ArrayList<Dish>();
		this.orderID = orderID;
		this.typeOfOrder = typeOfOrder;
		this.deliveryAddress = deliveryAddress;
		this.status = status;
		this.totalPrice = totalPrice;
		this.timeOfOrder = timeOfOrder;		
		this.fullName = fullName;
		this.userPhone = userPhone;
		
	}
	
	//Added by Semion
	public Order(String userFirstName, String userLastName, String userPhone, String deliveryAddress, int restaurantID, OrderType typeOfOrder) {
		this(userFirstName, userLastName, userPhone, deliveryAddress, restaurantID, typeOfOrder.toString());
	}

	public Order( String userFirstName, String userLastName, String userPhone, String deliveryAddress, int restaurantID, OrderType typeOfOrder, int orderPrice, int deliveryPrice) {
		this(userFirstName, userLastName, userPhone, deliveryAddress, restaurantID, typeOfOrder.toString());
		this.deliveryPrice = deliveryPrice;
		this.orderPrice = orderPrice;
		
	}
	//End

	public int getOrderingUserID() {
		return orderingUserID;
	}

	public void setOrderingUserID(int orderingUserID) {
		this.orderingUserID = orderingUserID;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public int getRestaurantID() {
		return restaurantID;
	}

	public void setRestaurantID(int restaurantID) {
		this.restaurantID = restaurantID;
	}

	public String getTypeOfOrder() {
		return typeOfOrder;
	}

	public void setTypeOfOrder(String typeOfOrder) {
		this.typeOfOrder = typeOfOrder;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getTimeOfOrder() {
		return timeOfOrder;
	}

	public void setTimeOfOrder(String timeOfOrder) {
		this.timeOfOrder = timeOfOrder;
	}

	public String getTimeOfArrival() {
		return timeOfArrival;
	}

	public void setTimeOfArrival(String timeOfArrival) {
		this.timeOfArrival = timeOfArrival;
	}

	public ArrayList<Dish> getDishesInOrder() {
		return dishesInOrder;
	}

	public void setDishesInOrder(ArrayList<Dish> dishesInOrder) {
		this.dishesInOrder = dishesInOrder;
	}
	

	@Override
	public String toString() {
		return "Order [orderingUserID=" + orderingUserID + ", orderID=" + orderID + ", userFirstName=" + userFirstName
				+ ", userLastName=" + userLastName + ", fullName=" + fullName + ", userPhone=" + userPhone + ", status="
				+ status + ", userEmail=" + userEmail + ", deliveryAddress=" + deliveryAddress + ", restaurantID="
				+ restaurantID + ", typeOfOrder=" + typeOfOrder + ", totalPrice=" + totalPrice + ", timeOfOrder="
				+ timeOfOrder + ", timeOfArrival=" + timeOfArrival
				+ ", isPrivateOrder=" + isPrivateOrder + ", orderPrice=" + orderPrice + ", deliveryPrice="
				+ deliveryPrice + ", dishesInOrder=" + dishesInOrder.toString()+ "]";
	}


	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
  
  public boolean isPrivateOrder() {
		return isPrivateOrder;
	}

	public void setPrivateOrder(boolean isPrivateOrder) {
		this.isPrivateOrder = isPrivateOrder;
	}
	
	//Added by Semion:
	public void addDish(Dish dish) {
		dishesInOrder.add(dish);
	}
	
	public void removeDish(Dish dish) {
		dishesInOrder.remove(dish);
	}
	
	public Dish getDishByDishID(String dishID) {
		for (Dish d : dishesInOrder) {
			if(d.getDish_ID().equals(dishID))
				return d;
		}
		return null;
	}
	
	public boolean checkIfDishInOrderByDish(Dish dish) {
		return checkIfDishInOrderByDishID(dish.getDish_ID());
	}
	
	public boolean checkIfDishInOrderByDishID(String dishID) {
		for (Dish d : dishesInOrder) {
			if(d.getDish_ID().equals(dishID))
				return true;
		}
		return false;
	}
	
	public int getAmountOfDishes() {
		return dishesInOrder.size();
	}

	public int getOrderPrice() {
		return orderPrice;
	}

	public int getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setOrderPrice(int orderPrice) {
		this.orderPrice = orderPrice;
	}

	public void setDeliveryPrice(int deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

}

