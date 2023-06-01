package utility.entity;

import java.util.HashMap;

public class ClientRefundsData {
	private int userID;
	private HashMap<Integer, Integer> resRefundPairs;
	
	public ClientRefundsData(int userID) {
		this.resRefundPairs = new HashMap<>();
		this.userID = userID;
	}
	
	public void addResRefundPair(int resID, int refundValue) {
		resRefundPairs.put(resID, refundValue);
	}
	
	public int getRefundByResID(int resID) {
		if(resRefundPairs.get(resID) != null) {
			return resRefundPairs.get(resID);
		}
		 return 0;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public HashMap<Integer, Integer> getResRefundHashmap(){
		return resRefundPairs;
	}
	
	public int totalRefundsValue() {
		int result = 0;
		for(int k : resRefundPairs.keySet()) {
			result += resRefundPairs.get(k);
		}
		return result;
	}

	@Override
	public String toString() {
		return "ClientRefundsData [userID=" + userID + ", resRefundPairs=" + resRefundPairs.toString() + "]";
	}
	
}
