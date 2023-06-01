package utility.enums;

public enum UserType {
	
	USER("USER"),
	CLIENT_PERSONAL("CLIENT_PERSONAL"),
	CLIENT_BUSINESS("CLIENT_BUSINESS"),
	HR_MANAGER("HR_MANAGER"),
	BM_BRANCH_MANAGER("BM_BRANCH_MANAGER"), 
	BM_CEO("BM_CEO"), 
	RESTAURANT_OWNER("RESTAURANT_OWNER"), 
	RESTAURANT_TRUSTED_WORKER("RESTAURANT_TRUSTED_WORKER");

	private String userType;

	private UserType(String userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return userType;
	}
	
	public static UserType fromString(String userTypeString) {
		switch(userTypeString) {
		case "CLIENT_PERSONAL":
			return UserType.CLIENT_PERSONAL;
		case "CLIENT_BUSINESS":
			return UserType.CLIENT_BUSINESS;
		case "HR_MANAGER":
			return UserType.HR_MANAGER;
		case "BM_BRANCH_MANAGER":
			return UserType.BM_BRANCH_MANAGER;
		case "BM_CEO":
			return UserType.BM_CEO;
		case "RESTAURANT_OWNER":
			return UserType.RESTAURANT_OWNER;
		default:
			return UserType.USER;
		}
	}
}
