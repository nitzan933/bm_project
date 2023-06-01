package utility.entity;

public class Supplier {
	
	private String restaurantName;
	private String imagePath;
	private String categories;
	private String personalBranch;
	private String workerID;
	
	public Supplier(String restaurantName, String imagePath, String categories,String personalBranch,String workerID) {
		this.restaurantName = restaurantName;
		this.imagePath = imagePath;
		this.categories = categories;
		this.personalBranch=personalBranch;
		this.workerID = workerID;
	}
	
	public String getWorkerID() {
		return workerID;
	}

	public Supplier(String restaurantName,String categories,String personalBranch,String workerID) {
		this(restaurantName,"",categories,personalBranch,workerID);
	}
	
	public String getRestaurantName() {
		return restaurantName;
	}
	public String getPersonalBranch() {
		return personalBranch;
	}
	public String getImagePath() {
		return imagePath;
	}
	public String getCategories() {
		return this.categories;
	}
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public void setPersonalBranch(String branch) {
		personalBranch = branch;
	}
	

}
