package utility.entity;

public class ClientChangePermission {
	
	private final String firstName;
	private final String lastName;
	private  String status;
	private final String branch;
	private final String id;
	

	public ClientChangePermission(String firstName,String lastName, String branch, String status,String id) {
		this.branch=branch;
		this.firstName = firstName;
		this.status = status;
		this.lastName=lastName;
		this.id=id;
	}
	
	public ClientChangePermission(String branch, String status) {
		this("","",branch,"","");
	}
	
	public String getId() {
		return id;
	}

	public String getBranch() {
		return branch;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String newS) {
		status = newS;
	}


}
