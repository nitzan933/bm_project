package utility.message_parsers;

import java.util.ArrayList;

import utility.enums.DataType;
import utility.enums.ErrorType;
import utility.enums.RequestType;

public class MessageParserError {
	
	public static String handleMessageExtractDataType_StringErrorMessage(Object message) {
		ArrayList<String> msg = (ArrayList<String>)message;
		if(!msg.get(1).equals("ERROR_MESSAGE")) {
			//TODO:ADD ERROR HANDLING
			return null; 
		}
		return msg.get(3);
	}
	
	public static ErrorType handleMessageExtractDataType__ErrorType(Object message) {
		ArrayList<String> msg = (ArrayList<String>)message;
		if(!msg.get(1).equals("ERROR_MESSAGE")) {
			//TODO:ADD ERROR HANDLING
			return null; 
		}
		return ErrorType.fromString(msg.get(2));
	}
	
	public static ArrayList<String> prepareMessageToClientWithDataType_Error(ErrorType errorType, String error) {
		if (error == null) {
			// TODO: Error handling
			System.out.println("createMessageToClientDataType_Error Failed");
			return null;
		}
		ArrayList<String> result = new ArrayList<String>();
		result.add(RequestType.SERVER_MESSAGE_TO_CLIENT_ERROR.toString());
		result.add(DataType.ERROR_MESSAGE.toString());
		result.add(errorType.toString());
		result.add(error);
		return result;
	}
}
