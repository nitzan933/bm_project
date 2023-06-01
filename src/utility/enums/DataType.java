package utility.enums;

public enum DataType {
	SINGLE_TEXT_STRING("SINGLE_TEXT_STRING"),	//used for messages that contain a single string of text: Connection/Debug
	ORDER("ORDER"),								//used for Order Messages
	USER("USER"),								//used for User Messages
	CLIENT("CLIENT"),							//used for branch manager messages
	SUPPLIER("SUPPLIER"),
	HR_MANAGER("HR_MANAGER"),
	APPROVE_BUSINESS("APPROVE_BUSINESS"),
	DECLINE_BUSINESS("DECLINE_BUSINESS"),
	GET_DATA_OF_BUSINESS("GET_DATA_OF_BUSINESS"),
	GET_DATA_OF_CLIENT("GET_DATA_OF_CLIENT"),
	CHANGE_PERMISSION("CHANGE_PERMISSION"),
	RESTAURANTS_LIST("RESTAURANTS_LIST"),
	BUSINESS_CLIENT_DATA("BUSINESS_CLIENT_DATA"),
	ERROR_MESSAGE("ERROR_MESSAGE"),				//used for Error Messages
	ARRAYLIST_STRING("ARRAYLIST_STRING"),
	DISHES_LIST("DISHES_LIST"),
	CLIENT_REFUNDS_DATA("CLIENT_REFUNDS_DATA"),
	REPORT("REPORT"),
	ORDERS_LIST("ORDERS_LIST"),
	RESTAURANT("RESTAURANT"),
	CEO("ECO"),
	DISH("DISH"),
	UNKNOWN("UNKNOWN");							//used internally, should not be sent as a type
	
	private String dataType;
	
	private DataType(String dataType) {
		this.dataType = dataType;
	}
	
	@Override
	public String toString() {
		return dataType;
	}
}
