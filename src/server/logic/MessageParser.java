package server.logic;

import java.util.ArrayList;

import utility.enums.DataType;
import utility.enums.RequestType;

public class MessageParser {

	/**
	 * Data Message Layout: [ RequestType (enum) | DataType (enum) | Data strings ]
	 * RequestTypes defined in utility.RequestType
	 */

	// to minimize later changes we took the message parser to be a class of itself;
	public static RequestType parseMessageFromClient_RequestType(Object message) {
		// current iteration assumes message as an ArrayList<String>
		ArrayList<String> msg = (ArrayList<String>) message;
		switch (msg.get(0)) {
		case "CLIENT_REQUEST_TO_SERVER_GET_DATA":
			return RequestType.CLIENT_REQUEST_TO_SERVER_GET_DATA;
		case "CLIENT_REQUEST_TO_SERVER_WRITE_UPDATE_TO_DB":
			return RequestType.CLIENT_REQUEST_TO_SERVER_WRITE_UPDATE_TO_DB;
		case "CLIENT_REQUEST_TO_SERVER_WRITE_NEW_TO_DB":
			return RequestType.CLIENT_REQUEST_TO_SERVER_WRITE_NEW_TO_DB;
		case "CLIENT_REQUEST_TO_SERVER_CONNECTION_STATUS":
			return RequestType.CLIENT_REQUEST_TO_SERVER_CONNECTION_STATUS;
		case "CLIENT_REQUEST_TO_SERVER_DEBUG_MESSAGE":
			return RequestType.CLIENT_REQUEST_TO_SERVER_DEBUG_MESSAGE;
		case "CLIENT_REQUEST_TO_SERVER_LOGIN_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_LOGIN_REQUEST;
		case "CLIENT_REQUEST_TO_SERVER_LOGOUT_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_LOGOUT_REQUEST;
		case "CLIENT_REQUEST_TO_SERVER_W4C_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_W4C_REQUEST;
		case "CLIENT_REQUEST_TO_SERVER_SEARCH_RESTAURANT_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_SEARCH_RESTAURANT_REQUEST;
		case "CLIENT_REQUEST_TO_SERVER_CATEGORY_RESTAURANT_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_CATEGORY_RESTAURANT_REQUEST;
		case "CLIENT_REQUEST_TO_SERVER_MENU_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_MENU_REQUEST;
		case "CLIENT_REQUEST_TO_SERVER_GET_ORDERS_BY_RESTAURANT_ID_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_GET_ORDERS_BY_RESTAURANT_ID_REQUEST;
		case "CLIENT_REQUEST_TO_SERVER_GET_RESTAURANT_BY_SUPPLIER_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_GET_RESTAURANT_BY_SUPPLIER_REQUEST;
		case "CLIENT_REQUEST_TO_SERVER_SUPPLIER_CANCEL_ORDER":
			return RequestType.CLIENT_REQUEST_TO_SERVER_SUPPLIER_CANCEL_ORDER;
		case "CLIENT_REQUEST_TO_SERVER_SUPPLIER_UPDATE_ORDER":
			return RequestType.CLIENT_REQUEST_TO_SERVER_SUPPLIER_UPDATE_ORDER;
		case "CLIENT_REQUEST_TO_SERVER_ADD_DISH_TO_MENU_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_ADD_DISH_TO_MENU_REQUEST;
		case "SERVER_MESSAGE_TO_CLIENT_UPDATED_DISH_SUCCESS":
			return RequestType.SERVER_MESSAGE_TO_CLIENT_UPDATED_DISH_SUCCESS;
		case "CLIENT_REQUEST_TO_SERVER_UPDATE_DISH_IN_MENU_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_UPDATE_DISH_IN_MENU_REQUEST;
		case "CLIENT_REQUEST_TO_SERVER_DELETE_DISH_FROM_MENU_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_DELETE_DISH_FROM_MENU_REQUEST;
		default:
			// TODO: ADD error handling for this case;
			return RequestType.CLIENT_REQUEST_TO_SERVER_UNKNOWN_REQUEST;
		}
	}

	public static DataType parseMessageFromClient_DataType(Object message) {
		// current iteration assumes message as an ArrayList<String>
		ArrayList<String> msg = (ArrayList<String>) message;
		switch (msg.get(1)) {
		case "ORDER":
			return DataType.ORDER;
		case "USER":
			return DataType.USER;
		case "SINGLE_TEXT_STRING":
			return DataType.SINGLE_TEXT_STRING;
		case "RESTAURANTS_LIST":
			return DataType.RESTAURANTS_LIST;
		case "ARRAYLIST_STRING":
			return DataType.ARRAYLIST_STRING;
		default:
			// TODO: ADD error handling for this case;
			return DataType.UNKNOWN;
		}
	}

	// ---------------------------------------------------Entity parsing functions

//	public static Order parseMessageDataType_Order(Object message) {
//		ArrayList<String> msg = (ArrayList<String>) message;
//		if (!msg.get(1).equals("ORDER")) {
//			// TODO: ADD error handling for this case;
//			System.out.println("Error parseMessageDataType_Order not order");
//			return null;
//		}
//		Order order = new Order(msg.get(2), msg.get(3), msg.get(4), msg.get(5), msg.get(6), msg.get(7));
//		return order;
//	}
//
//	public static User parseMessageDataType_User(Object message) {
//		ArrayList<String> msg = (ArrayList<String>) message;
//		if (!msg.get(1).equals("USER")) {
//			// TODO: ADD error handling for this case;
//			System.out.println("Error parseMessageDataType_User not User");
//			return null;
//		}
//		User user = new User(Integer.valueOf(msg.get(2)), msg.get(3), msg.get(4), msg.get(5), msg.get(6), msg.get(7),
//				UserType.fromString(msg.get(8)), msg.get(9), msg.get(10), Boolean.parseBoolean(msg.get(11)),
//				msg.get(12));
//		return user;
//	}

	/**
	 * Get Request Layout: [ RequestType | Data Type | DataToGet (id?) ]
	 * RequestTypes defined in utility.RequestType
	 */

	public static String parseMessageDataType_Order_GetRequestOrderID(Object message) {
		ArrayList<String> msg = (ArrayList<String>) message;
		if (!msg.get(1).equals("ORDER")) {
			// TODO: ADD error handling for this case;
			System.out.println("Error parseMessageDataType_Order_GetRequestOrderID not order");
			return null;
		}
		String id = msg.get(2);
		return id;
	}

	public static String parseMessageDataType_SingleTextString(Object message) {
		ArrayList<String> msg = (ArrayList<String>) message;
		if (!msg.get(1).equals("SINGLE_TEXT_STRING")) {
			// TODO: ADD error handling for this case;
			System.out.println("Error parseMessageDataType_SingleTextString not string");
			return null;
		}
		String string = msg.get(2);
		return string;
	}

	public static ArrayList<String> parseMessageDataType_ArrayListString(Object message) {
		ArrayList<String> msg = (ArrayList<String>)message;
		System.out.println(msg.get(1));
		if (!msg.get(1).equals("ARRAYLIST_STRING")) {
			// TODO: ADD error handling for this case;
			System.out.println("Error parseMessageDataType_ArrayListString not arraylist");
			return null;
		}
		ArrayList<String> arraylist = new ArrayList<>();
		arraylist.add(msg.get(2));
		arraylist.add(msg.get(3));
		return arraylist;
	}

	// -------------------------------------------------Entity wrapping functions

//	public static ArrayList<String> createMessageToClientDataType_User(User user, RequestType requestType) {
//		if (user == null) {
//			System.out.println("createMessageToClientDataType_User Failed");
//			return null;
//		}
//		ArrayList<String> result = new ArrayList<String>();
//		result.add(requestType.toString());
//		result.add(DataType.USER.toString());
//		result.add(String.valueOf(user.getUser_ID()));
//		result.add(user.getFirstName());
//		result.add(user.getLastName());
//		result.add(user.getPersonalBranch());
//		result.add(user.getEmail());
//		result.add(user.getPhone());
//		result.add(user.getUserType().toString());
//		result.add(user.getStatus());
//		result.add(user.getW4c());
//		result.add(String.valueOf(user.isSignedIn()));
//		result.add(user.getPassword());
//		return result;
//	}

//	public static ArrayList<String> createMessageToClientDataType_Order(Order order) {
//		if (order == null) {
//			// TODO: Error handling
//			System.out.println("createMessageToClientDataType_Order Failed");
//			return null;
//		}
//		ArrayList<String> result = new ArrayList<String>();
//		result.add(RequestType.SERVER_MESSAGE_TO_CLIENT_DATA_PROVIDED.toString());
//		result.add(DataType.ORDER.toString());
//		result.add(order.getOrderNumber());
//		result.add(order.getRestaurantName());
//		result.add(order.getOrderTime());
//		result.add(order.getPhoneNumber());
//		result.add(order.getTypeOfOrder());
//		result.add(order.getOrderAddress());
//		return result;
//	}

//	public static ArrayList<String> createMessageToClientDataType_Error(ErrorType errorType, String error) {
//		if (error == null) {
//			// TODO: Error handling
//			System.out.println("createMessageToClientDataType_Error Failed");
//			return null;
//		}
//		ArrayList<String> result = new ArrayList<String>();
//		result.add(RequestType.SERVER_MESSAGE_TO_CLIENT_ERROR.toString());
//		result.add(DataType.ERROR_MESSAGE.toString());
//		result.add(errorType.toString());
//		result.add(error);
//		return result;
//	}

}
