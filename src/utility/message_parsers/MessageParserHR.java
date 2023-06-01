package utility.message_parsers;

import java.util.ArrayList;

import utility.entity.BusinessClient;
import utility.enums.DataType;
import utility.enums.RequestType;

public class MessageParserHR {
	
	public static ArrayList<BusinessClient>  handleMessageExtractDataType_HRGetData(Object message) {
		ArrayList<BusinessClient> parsedData = new ArrayList<BusinessClient>();
		ArrayList<String> DataFromServer = (ArrayList<String>)message;
		int count = 0;
		if (!DataFromServer.get(1).equals("HR_MANAGER")) {
			return null;
		}
		for(int i = 2 ; i+3 < DataFromServer.size() ; i+=4) {
			BusinessClient businessClient = new BusinessClient(DataFromServer.get(i), DataFromServer.get(i+1),DataFromServer.get(i+2), DataFromServer.get(i+3));
			parsedData.add(businessClient);
		}
		return parsedData;
	}
	
	public static Object prepareMessageToServer_HRDataRequest(int userID, DataType dataType,RequestType requestType) {	
		ArrayList<String> messageToPrepare = new ArrayList<String>();
		
		messageToPrepare.add(requestType.toString());
		messageToPrepare.add(DataType.HR_MANAGER.toString());
		messageToPrepare.add(String.valueOf(userID));
		
		return messageToPrepare;

	}
	
	public static Object prepareMessageToServer_HRUpdateDB(BusinessClient businessClient,  DataType dataType,RequestType requestType) {	
		ArrayList<String> messageToPrepare = new ArrayList<String>();
		
		messageToPrepare.add(requestType.toString());
		messageToPrepare.add(DataType.HR_MANAGER.toString());
		messageToPrepare.add(businessClient.getWorkerID());
		messageToPrepare.add(String.valueOf(businessClient.getIsApproved()));
				
		return messageToPrepare;

	}
	
	public static ArrayList<String> handleMessageFromClient_HRGetData(Object msg){
		return (ArrayList<String>)msg;
	}
	
	public static Object prepareMessageToServer_HRApproveBusiness(int hr_id,  DataType dataType,RequestType requestType) {	
		ArrayList<String> messageToPrepare = new ArrayList<String>();
		
		messageToPrepare.add(requestType.toString());
		messageToPrepare.add(DataType.HR_MANAGER.toString());
		messageToPrepare.add(String.valueOf(hr_id));

				
		return messageToPrepare;

	}
	
	
	
	public static int handleMessageFromClient_ApproveBusinessClient(ArrayList<String> messageFromClient) {	
		return Integer.parseInt(messageFromClient.get(2));

	}
	
	public static ArrayList<String> prepareMessageToServer_HRCheckApproveBusiness(int isApproved,  DataType dataType,RequestType requestType) {	
		ArrayList<String> messageToPrepare = new ArrayList<String>();
		
		messageToPrepare.add(requestType.toString());
		messageToPrepare.add(DataType.HR_MANAGER.toString());
		messageToPrepare.add(String.valueOf(isApproved));

				
		return messageToPrepare;

	}
}
//	public static Object prepareMessageWithDataType_User(BusinessClient BusinessClient, RequestType requestType) {
//		
//		ArrayList<String> messageToPrepare = new ArrayList<String>();
//
//		return messageToPrepare;
//	}

