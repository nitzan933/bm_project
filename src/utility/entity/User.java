package utility.entity;

import utility.enums.UserType;

public class User {

	private int user_ID;
	private String firstName;
	private String lastName;
	private String personalBranch;
	private String email;
	private String phone;
	private String status;
	private String w4c;
	private String password;
	private boolean isSignedIn;	
	private UserType userType;
	
	private int personalCode;
	private int buisnessCode;
	
	
	public User(int user_ID, String firstName, String lastName, String personalBranch, String email, String phone, UserType userType,
			String status, String w4c, boolean isSignedIn, String password, int personalCode, int buisnessCode) {
		this.user_ID = user_ID; 
		this.firstName = firstName;
		this.lastName = lastName;
		this.personalBranch = personalBranch;
		this.email = email;
		this.phone = phone;
		this.status = status;
		this.w4c = w4c;
		this.password = password;
		this.isSignedIn = isSignedIn;
		this.userType = userType;
		this.personalCode = personalCode;
		this.buisnessCode = buisnessCode;
	}
	
	public User(int user_ID, String firstName, String lastName, String personalBranch, String email, String phone, UserType userType,
			String status, String w4c, boolean isSignedIn, String password) {

		this(user_ID, firstName, lastName, personalBranch, email, phone, userType, status, w4c, isSignedIn, password, -1, -1);
	}
	
	public User(String email, String password){
		this(-1, "", "", "", email, "", UserType.USER, "", "", false, password, -1, -1);

	}
	
	
	public int getUser_ID() {
		return user_ID;
	}


	public String getFirstName() {
		return firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public String getPersonalBranch() {
		return personalBranch;
	}


	public String getEmail() {
		return email;
	}


	public String getPhone() {
		return phone;
	}


	public String getStatus() {
		return status;
	}


	public String getW4c() {
		return w4c;
	}


	public String getPassword() {
		return password;
	}


	public boolean isSignedIn() {
		return isSignedIn;
	}

	public UserType getUserType() {
		return userType;
	}
	

	public int getPersonalCode() {
		return personalCode;
	}



	public int getBuisnessCode() {
		return buisnessCode;
	}

	@Override
	public String toString() {
		return "User [user_ID=" + user_ID + ", firstName=" + firstName + ", lastName=" + lastName + ", personalBranch="
				+ personalBranch + ", email=" + email + ", phone=" + phone + ", status=" + status + ", w4c=" + w4c
				+ ", password=" + password + ", isSignedIn=" + isSignedIn + ", userType=" + userType + ", personalCode="
				+ personalCode + ", buisnessCode=" + buisnessCode + "]";
	}


	public void setPersonalCode(int personalCode) {
		this.personalCode = personalCode;
	}

	public void setBuisnessCode(int buisnessCode) {
		this.buisnessCode = buisnessCode;
	}


}
