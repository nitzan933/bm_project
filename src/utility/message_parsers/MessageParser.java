package utility.message_parsers;

import java.util.ArrayList;

import utility.enums.DataType;
import utility.enums.RequestType;

public class MessageParser {

	/**
	 * Data Message Layout: [ RequestType (enum) | DataType (enum) | Data strings ]
	 * RequestTypes defined in utility.RequestType
	 */

	// to minimize later changes we took the message parser to be a class of itself;
	public static RequestType parseMessage_RequestType(Object message) {
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
		case "CLIENT_REQUEST_TO_SERVER_REGISTER_CLIENT":
			return RequestType.CLIENT_REQUEST_TO_SERVER_REGISTER_CLIENT;
		case "CLIENT_REQUEST_TO_SERVER_SEARCH_RESTAURANT_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_SEARCH_RESTAURANT_REQUEST;
		case "CLIENT_REQUEST_TO_SERVER_INCOMING_FILE":
			return RequestType.CLIENT_REQUEST_TO_SERVER_INCOMING_FILE;
		case "CLIENT_REQUEST_TO_SERVER_REGISTER_SUPPLIER":
			return RequestType.CLIENT_REQUEST_TO_SERVER_REGISTER_SUPPLIER;
		case "CLIENT_REQUEST_TO_SERVER_CATEGORY_RESTAURANT_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_CATEGORY_RESTAURANT_REQUEST;
		case "CLIENT_REQUEST_TO_SERVER_MENU_REQUEST":
			return RequestType.CLIENT_REQUEST_TO_SERVER_MENU_REQUEST;
		case "CLIENT_REQUEST_TO_SERVER_BUSINESS_CLIENT_DATA":
			return RequestType.CLIENT_REQUEST_TO_SERVER_BUSINESS_CLIENT_DATA;
		case "CLIENT_REQUEST_TO_SERVER_GET_CLIENT_REFUNDS_DATA":
			return RequestType.CLIENT_REQUEST_TO_SERVER_GET_CLIENT_REFUNDS_DATA;
		case "SERVER_MESSAGE_TO_CLIENT_REFUND_AMOUNT":
			return RequestType.SERVER_MESSAGE_TO_CLIENT_REFUND_AMOUNT;
		case "CLIENT_REQUEST_TO_SERVER_GET_DATA_BUSINESSES_NAMES":
			return RequestType.CLIENT_REQUEST_TO_SERVER_GET_DATA_BUSINESSES_NAMES;
		case "CLIENT_REQUEST_TO_SERVER_APPROVE_BUSINESS":
			return RequestType.CLIENT_REQUEST_TO_SERVER_APPROVE_BUSINESS; 
		case "SERVER_MESSAGE_TO_CLIENT_APPROVE_BUSINESS_SUCCESS":
			return RequestType.SERVER_MESSAGE_TO_CLIENT_APPROVE_BUSINESS_SUCCESS;
		case "CLIENT_REQUEST_TO_SERVER_GET_DATA_CLIENT_INFO":
			return RequestType.CLIENT_REQUEST_TO_SERVER_GET_DATA_CLIENT_INFO;
		case "CLIENT_REQUEST_TO_SERVER_CHANGE_PERMISSION":
			return RequestType.CLIENT_REQUEST_TO_SERVER_CHANGE_PERMISSION; 
		case "CLIENT_REQUEST_TO_SERVER_OPEN_REPORT":
			return RequestType.CLIENT_REQUEST_TO_SERVER_OPEN_REPORT;
		case "SERVER_MESSAGE_TO_CLIENT_OPEN_REPORT_SUCCESS":
			return RequestType.SERVER_MESSAGE_TO_CLIENT_OPEN_REPORT_SUCCESS;
		case "CLIENT_REQUEST_TO_SERVER_APRROVE_BUSINESS":
            return RequestType.CLIENT_REQUEST_TO_SERVER_APRROVE_BUSINESS;
        case "CLIENT_REQUEST_TO_SERVER_CHECK_APRROVE_BUSINESS":
            return RequestType.CLIENT_REQUEST_TO_SERVER_CHECK_APRROVE_BUSINESS;
        case "CLIENT_REQUEST_TO_SERVER_GET_APPROVED_BUSINESS_CLIENTS":
            return RequestType.CLIENT_REQUEST_TO_SERVER_GET_APPROVED_BUSINESS_CLIENTS;
        case "CLIENT_REQUEST_TO_SERVER_GET_RESTAURANT_BY_SUPPLIER_REQUEST":
            return RequestType.CLIENT_REQUEST_TO_SERVER_GET_RESTAURANT_BY_SUPPLIER_REQUEST;
        case "CLIENT_REQUEST_TO_SERVER_ADD_DISH_TO_MENU_REQUEST":
            return RequestType.CLIENT_REQUEST_TO_SERVER_ADD_DISH_TO_MENU_REQUEST;
        case "SERVER_MESSAGE_TO_CLIENT_UPDATED_DISH_SUCCESS":
            return RequestType.SERVER_MESSAGE_TO_CLIENT_UPDATED_DISH_SUCCESS;
        case "CLIENT_REQUEST_TO_SERVER_UPDATE_DISH_IN_MENU_REQUEST":
            return RequestType.CLIENT_REQUEST_TO_SERVER_UPDATE_DISH_IN_MENU_REQUEST;
        case "CLIENT_REQUEST_TO_SERVER_DELETE_DISH_FROM_MENU_REQUEST":
            return RequestType.CLIENT_REQUEST_TO_SERVER_DELETE_DISH_FROM_MENU_REQUEST;
        case "CLIENT_REQUEST_TO_SERVER_GET_ORDERS_BY_RESTAURANT_ID_REQUEST":
            return RequestType.CLIENT_REQUEST_TO_SERVER_GET_ORDERS_BY_RESTAURANT_ID_REQUEST;
        case "CLIENT_REQUEST_TO_SERVER_SUPPLIER_UPDATE_ORDER":
        	return RequestType.CLIENT_REQUEST_TO_SERVER_SUPPLIER_UPDATE_ORDER;
        case "CLIENT_REQUEST_TO_SERVER_SUPPLIER_CANCEL_ORDER":
        	return RequestType.CLIENT_REQUEST_TO_SERVER_SUPPLIER_CANCEL_ORDER;
        case "CLIENT_REQUEST_TO_SERVER_SEND_REPORT_TO_CEO":
        	return RequestType.CLIENT_REQUEST_TO_SERVER_SEND_REPORT_TO_CEO;
        case "SERVER_MESSAGE_TO_CLIENT_SEND_TO_CEO_SUCCESS":
        	return RequestType.SERVER_MESSAGE_TO_CLIENT_SEND_TO_CEO_SUCCESS;
        case "CLIENT_REQUEST_TO_SERVER_CEO_QUARTERLY_REPORT":
        	return RequestType.CLIENT_REQUEST_TO_SERVER_CEO_QUARTERLY_REPORT;
        case "CLIENT_REQUEST_TO_SERVER_CEO_GET_BRANCHES_REPORTS":
        	return RequestType.CLIENT_REQUEST_TO_SERVER_CEO_GET_BRANCHES_REPORTS;
        case "SERVER_MESSAGE_TO_CLIENT_CEO_GET_BRANCHES_REPORTS":
        	return RequestType.SERVER_MESSAGE_TO_CLIENT_CEO_GET_BRANCHES_REPORTS;
        case "CLIENT_REQUEST_TO_SERVER_OPEN_REPORT_FROM_BRANCH":
        	return RequestType.CLIENT_REQUEST_TO_SERVER_OPEN_REPORT_FROM_BRANCH;
        case "CLIENT_REQUEST_TO_SERVER_OPEN_SUPPLIER_INCOME_REPORT":
        	return RequestType.CLIENT_REQUEST_TO_SERVER_OPEN_SUPPLIER_INCOME_REPORT;
        case "CLIENT_REQUEST_TO_SERVER_UPDATE_REFUND_AMOUNT_AFTER_REFUNDS_USED":
        	return RequestType.CLIENT_REQUEST_TO_SERVER_UPDATE_REFUND_AMOUNT_AFTER_REFUNDS_USED;
        case "CLIENT_REQUEST_TO_SERVER_BUSINESS_CLIENT_BUDGED_UPDATE":
        	return RequestType.CLIENT_REQUEST_TO_SERVER_BUSINESS_CLIENT_BUDGED_UPDATE;
		default:
			// TODO: ADD error handling for this case;
			return RequestType.CLIENT_REQUEST_TO_SERVER_UNKNOWN_REQUEST;
		}
	}

	public static DataType parseMessage_DataType(Object message) {
		// current iteration assumes message as an ArrayList<String>
		ArrayList<String> msg = (ArrayList<String>) message;
		switch (msg.get(1)) {
		case "ORDER":
			return DataType.ORDER;
		case "USER":
			return DataType.USER;
		case "CLIENT":
			return DataType.CLIENT;
		case "SUPPLIER":
			return DataType.SUPPLIER;
		case "SINGLE_TEXT_STRING":
			return DataType.SINGLE_TEXT_STRING;
		case "RESTAURANTS_LIST":
			return DataType.RESTAURANTS_LIST;
		case "HR_MANAGER":
			return DataType.HR_MANAGER;
		case "DISHES_LIST":
			return DataType.DISHES_LIST;
		case "ARRAYLIST_STRING":
			return DataType.ARRAYLIST_STRING;
		case "ERROR_MESSAGE":
			return DataType.ERROR_MESSAGE;
		case "BUSINESS_CLIENT_DATA":
			return DataType.BUSINESS_CLIENT_DATA;
		case "GET_DATA_OF_BUSINESS":
			return DataType.GET_DATA_OF_BUSINESS;
		case "APPROVE_BUSINESS":
			return DataType.APPROVE_BUSINESS;
		case "GET_DATA_OF_CLIENT":
			return DataType.GET_DATA_OF_CLIENT; 
		case "CHANGE_PERMISSION":
			return DataType.CHANGE_PERMISSION;
		case "REPORT":
			return DataType.REPORT;
		case "ORDERS_LIST":
			return DataType.ORDERS_LIST;
		case "RESTAURANT":
			return DataType.RESTAURANT;
		case "DISH":
			return DataType.DISH;
		case "CEO":
			return DataType.CEO;
		case "CLIENT_REFUNDS_DATA":
			return DataType.CLIENT_REFUNDS_DATA;
		default:
			System.out.println("parseMessage_DataType: unknown dataType - " + msg.get(1));
			return DataType.UNKNOWN;
		}
	}

	// ---------------------------------------------------Entity parsing functions


	/**
	 * Get Request Layout: [ RequestType | Data Type | DataToGet (id?) ]
	 * RequestTypes defined in utility.RequestType
	 */

}
