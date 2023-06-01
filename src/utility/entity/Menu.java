package utility.entity;

public class Menu {
	
	private String restaurant_ID;
	private Dish[] dishesList;
	
	
	public Menu(String restaurant_ID,Dish[] dishesList) {
		this.restaurant_ID = restaurant_ID;
		this.dishesList = dishesList;
	}


	public void setDishesList(Dish[] dishesList) {
		this.dishesList = dishesList;
	}


	public String getRestaurant_ID() {
		return restaurant_ID;
	}


	public Dish[] getDishesList() {
		return dishesList;
	}

}
