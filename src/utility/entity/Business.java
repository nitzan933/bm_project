package utility.entity;

public class Business {
	
	private int businessId;
	private String businessName;
	private int isApproved;
	private int hr_id;
	private String branch;
	public Business(int businessId, String businessName, int isApproved, int hr_id, String branch) {
		super();
		this.businessId = businessId;
		this.businessName = businessName;
		this.isApproved = isApproved;
		this.hr_id = hr_id;
		this.branch = branch;
	}
	
	public Business(String businessName,int isApproved, String branch) {
		this(0,businessName,isApproved,0,branch);
	}
	
	public int getBusinessId() {
		return businessId;
	}
	public String getBusinessName() {
		return businessName;
	}
	public int getIsApproved() {
		return isApproved;
	}
	public int getHr_id() {
		return hr_id;
	}
	public String getBranch() {
		return branch;
	}

	

}
