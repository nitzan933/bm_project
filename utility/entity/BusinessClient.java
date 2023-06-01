package utility.entity;


public class BusinessClient {
	private final String workerID;
	private final String workerName;
	private final String workerPhone;
	private final String workerEmail;
	
	private boolean isApproved = false;

	public BusinessClient(String userId, String firstName, String phoneNumber, String email) {
		this.workerID = userId;
		this.workerName = firstName;
		this.workerPhone = phoneNumber;
		this.workerEmail = email;
	}
	
	public void setIsApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
	
	public boolean getIsApproved() {
		return isApproved;
	}

	public String getWorkerID() {
		return workerID;
	}

	public String getWorkerName() {
		return workerName;
	}

	public String getWorkerPhone() {
		return workerPhone;
	}

	public String getWorkerEmail() {
		return workerEmail;
	}

	@Override
	public String toString() {
		return "BusinessClient [workerID=" + workerID + ", workerName=" + workerName + ", workerPhone=" + workerPhone
				+ ", workerEmail=" + workerEmail + "]";
	}


}
