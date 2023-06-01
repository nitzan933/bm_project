package utility.entity;

import utility.enums.UserType;

public class Client extends User{
	
	private int balanceInApp;
	private int budget;
	private int isApproved;
	private int businessId;
	private int employerCode;
	private int personalCreditNumber;
	private String email;
	private UserType userType;
	
	
	public Client(int user_ID, String firstName, String lastName, String personalBranch, String email, String phone,
			UserType userType, String status, String w4c, boolean isSignedIn, String password, int personalCode, int businessCode,
			int balanceInApp, int budget, int isApproved,int businessId, int personalCreditNumber) {
		super(user_ID, firstName, lastName, personalBranch, email, phone, userType, status, w4c, isSignedIn, password,
				personalCode, businessCode);
		this.balanceInApp = balanceInApp;
		this.budget = budget;
		this.isApproved = isApproved;
		this.businessId = businessId;
		this.employerCode = businessId;
		this.personalCreditNumber = personalCreditNumber;
		this.email=email;
		this.userType=userType;
	}
	
	public Client(int ID,String firstName, String lastName,String personalBranch,String email, String phone,int personalCreditNumber, 
				  int employerCode, int mothlyBalance, UserType type) {
		this(ID,firstName,lastName, personalBranch, email, phone,type,"","",false,"",0,0,0,mothlyBalance,0,employerCode,personalCreditNumber);
	}


	public int getBalanceInApp() {
		return balanceInApp;
	}

	public void setBalanceInApp(int balanceInApp) {
		this.balanceInApp = balanceInApp;
	}

	public int getBudget() {
		return budget;
	}


	public int getIsApproved() {
		return isApproved;
	}


	public int getBusinessId() {
		return businessId;
	}


	public int getPersonalCreditNumber() {
		return personalCreditNumber;
	}
//
//
//	public String getFirstName() {
//		return super.getFirstName();
//	}
//
//
//	public String getLastName() {
//		return super.getLastName();
//	}
//
//
//	public String getEmail() {
//		return email;
//	}
//
//
//	public String getPhone() {
//		return super.getPhone();
//	}
//
//
//	public int getBuisnessCode() {
//		return super.getBuisnessCode();
//	}
//	
	public int getEmployerCode() {
		return employerCode;
	}
	
	public UserType getUserType() {
		return userType;
	}

}
