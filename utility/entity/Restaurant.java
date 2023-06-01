package utility.entity;

public class Restaurant {
	

	private String res_ID, resName, category, branch;

	public Restaurant(String res_ID, String resName, String category, String branch) {
		this.res_ID = res_ID;
		this.resName = resName;
		this.category = category;
		this.branch = branch;
	}


	public String getRes_ID() {
		return res_ID;
	}

	public String getResName() {
		return resName;
	}

	public String getCategory() {
		return category;
	}

	public String getBranch() {
		return branch;
	}
	
	

}
