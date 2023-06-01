package utility.message_parsers;

import java.util.ArrayList;

import utility.entity.BusinessClientData;
import utility.enums.DataType;
import utility.enums.RequestType;

public class MessageParserBusinessClientData {
	public static Object prepareMessageWithDataType_BusinessClientData(BusinessClientData bcd, RequestType requestType) {
		ArrayList<String> messageToPrepare = new ArrayList<String>();
		messageToPrepare.add(requestType.toString());
		messageToPrepare.add(DataType.BUSINESS_CLIENT_DATA.toString());
		messageToPrepare.add(String.valueOf(bcd.getUserID()));
		messageToPrepare.add(String.valueOf(bcd.getPersonalCode()));
		messageToPrepare.add(String.valueOf(bcd.getBusinessCode()));
		messageToPrepare.add(String.valueOf(bcd.getBalanceInApp()));
		messageToPrepare.add(String.valueOf(bcd.getBudget()));
		messageToPrepare.add(String.valueOf(bcd.isApproved()));
		messageToPrepare.add(String.valueOf(bcd.getBusinessID()));
		messageToPrepare.add(String.valueOf(bcd.getPersonalCreditNum()));
		return messageToPrepare;
	}
	
	public static BusinessClientData handleMessageExtractDataType_BusinessClientData(Object message) {
		ArrayList<String> msg = (ArrayList<String>) message;
		if (!msg.get(1).equals("BUSINESS_CLIENT_DATA")) {
			// TODO:ADD ERROR HANDLING
			return null;
		}
		BusinessClientData clientData = new BusinessClientData(Integer.parseInt(msg.get(2)),Integer.parseInt(msg.get(3)), Integer.parseInt(msg.get(4)), 
				Integer.parseInt(msg.get(5)), Integer.parseInt(msg.get(6)), Boolean.valueOf(msg.get(7)), Integer.parseInt(msg.get(8)),Integer.parseInt(msg.get(9)));
		
		return clientData;
	}
}
