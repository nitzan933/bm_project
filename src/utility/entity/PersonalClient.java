package utility.entity;

import utility.enums.UserType;

public class PersonalClient extends User {

	int personalCode;
	int balance;
	
	PersonalClient(int user_ID, String firstName, String lastName, String personalBranch, String email, String phone,
			String status, String w4c, String password, boolean isSignedIn, int personalCode, int balance) {
		super(user_ID, firstName, lastName, personalBranch, email, phone, UserType.CLIENT_PERSONAL,
				status, w4c, isSignedIn, password);
		this.personalCode = personalCode;
		this.balance = balance;
	}
	
	PersonalClient(int user_ID) {
		this(user_ID, "","","","","","", "", "", false, 0, 0);
	}
	
	public int getPersonalCode() {
		return personalCode;
	}

	public int getBalance() {
		return balance;
	}
	
	public boolean addToBalance(int addValue) {
		if(addValue < 0 ) {
			return false;
		}
		balance += addValue;
		return true;
	}
	
	public boolean substractFromBalance(int subValue) {
		if(subValue < 0 || subValue > balance) {
			return false;
		}
		balance -= subValue;
		return true;
	}


}
