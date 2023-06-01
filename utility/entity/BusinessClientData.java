package utility.entity;

public class BusinessClientData {
	private int userID;
	private int personalCode;
	private int businessCode;
	private int balanceInApp;
	private int budget;
	private boolean approved;
	private int businessID;
	private int personalCreditNum;
	
	public BusinessClientData( int businessCode, int personalCode, int balanceInApp, int userID, int budget,
			boolean approved, int businessID, int personalCreditNum) {
		this.userID = userID;
		this.personalCode = personalCode;
		this.businessCode = businessCode;
		this.balanceInApp = balanceInApp;
		this.budget = budget;
		this.approved = approved;
		this.businessID = businessID;
		this.personalCreditNum = personalCreditNum;
	}

	BusinessClientData(int businessCode){
		this(0, 0, businessCode,0, 0, false, 0, 0);
	}

	public int getUserID() {
		return userID;
	}

	public int getPersonalCode() {
		return personalCode;
	}

	public int getBusinessCode() {
		return businessCode;
	}

	public int getBalanceInApp() {
		return balanceInApp;
	}

	public int getBudget() {
		return budget;
	}

	public boolean isApproved() {
		return approved;
	}

	public int getBusinessID() {
		return businessID;
	}

	public int getPersonalCreditNum() {
		return personalCreditNum;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public void setPersonalCode(int personalCode) {
		this.personalCode = personalCode;
	}

	public void setBusinessCode(int businessCode) {
		this.businessCode = businessCode;
	}

	public void setBalanceInApp(int balanceInApp) {
		this.balanceInApp = balanceInApp;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public void setBusinessID(int businessID) {
		this.businessID = businessID;
	}

	public void setPersonalCreditNum(int personalCreditNum) {
		this.personalCreditNum = personalCreditNum;
	}

	@Override
	public String toString() {
		return "BusinessClientData [userID=" + userID + ", personalCode=" + personalCode + ", businessCode="
				+ businessCode + ", balanceInApp=" + balanceInApp + ", budget=" + budget + ", approved=" + approved
				+ ", businessID=" + businessID + ", personalCreditNum=" + personalCreditNum + "]";
	}
}
