package utility.message_parsers;

import java.util.ArrayList;

import utility.entity.Restaurant;
import utility.enums.DataType;
import utility.enums.RequestType;

public class MessegeParserRestaurants {
	
	public static ArrayList<Restaurant> handleMessageExtractDataType_Restaurants(Object message) {
		ArrayList<String> msg = (ArrayList<String>) message;
		if (!msg.get(1).equals("RESTAURANTS_LIST")) {
			// TODO:ADD ERROR HANDLING
			return null;
		}
		int i, j=2;
		ArrayList<Restaurant> result = new ArrayList<>();
		for(i=0; i<(msg.size()-2)/4; i++) {
			result.add(new Restaurant(msg.get(j),msg.get(j+1),msg.get(j+2),msg.get(j+3)));
			j+=4;
		}
		return result;
	}
	
	public static Object prepareMessageWithDataType_Restaurants(ArrayList<Restaurant> restaurantsList, RequestType requestType) {
		// this method is preparing user messages, data string for user are:
		// String orderNumber,String restaurantName,String orderTime,String
		// PhoneNumber,String TypeOfOrder,String orderAddress
		ArrayList<String> messageToPrepare = new ArrayList<String>();
		messageToPrepare.add(requestType.toString());
		messageToPrepare.add(DataType.RESTAURANTS_LIST.toString());
		
		for(Restaurant restaurant: restaurantsList) {
			messageToPrepare.add(String.valueOf(restaurant.getRes_ID()));
			messageToPrepare.add(restaurant.getResName());
			messageToPrepare.add(restaurant.getCategory());
			messageToPrepare.add(restaurant.getBranch());
		}

		return messageToPrepare;
	}

	public static Restaurant handleMessageExtractDataType_singleRestaurant(Object message) {
		ArrayList<String> msg = (ArrayList<String>) message;
		if (!msg.get(1).equals("RESTAURANT")) {
			// TODO:ADD ERROR HANDLING
			return null;
		}
		return new Restaurant(msg.get(2),msg.get(3),msg.get(4),msg.get(5));
	}

	public static Object prepareMessageWithDataType_singleRestaurant(Restaurant result, RequestType requestType) {
			
		ArrayList<String> messageToPrepare = new ArrayList<String>();
		messageToPrepare.add(requestType.toString());
		messageToPrepare.add(DataType.RESTAURANT.toString());
		
		messageToPrepare.add(String.valueOf(result.getRes_ID()));
		messageToPrepare.add(result.getResName());
		messageToPrepare.add(result.getCategory());
		messageToPrepare.add(result.getBranch());

		return messageToPrepare;
	}

}
