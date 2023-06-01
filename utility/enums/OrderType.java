package utility.enums;

public enum OrderType {
	PICKUP("PICKUP"),
	DELIVERY_EARLY("DELIVERY_EARLY"),
	DELIVERY_REGULAR("DELIVERY_REGULAR"),
	DELIVERY_ROBOT("DELIVERY_ROBOT"),
	UNKNOWN("UNKNOWN");
	
	private String orderType;
	
	private OrderType(String orderType) {
		this.orderType = orderType;
	}
	
	@Override
	public String toString() {
		return orderType;
	}
	
	public static OrderType fromString(String orderTypeString) {
		switch(orderTypeString) {
		case "PICKUP":
			return OrderType.PICKUP;
		case "DELIVERY_EARLY":
			return OrderType.DELIVERY_EARLY;
		case "DELIVERY_REGULAR":
			return OrderType.DELIVERY_REGULAR;
		case "DELIVERY_ROBOT":
			return OrderType.DELIVERY_ROBOT;
		default:
			return OrderType.UNKNOWN;
		}
	}
}
