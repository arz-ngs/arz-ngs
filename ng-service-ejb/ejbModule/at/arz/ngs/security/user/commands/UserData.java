package at.arz.ngs.security.user.commands;


public class UserData {

	private String userName;

	private String first_name;

	private String last_name;

	private String email;

	public UserData(String userName, String first_name, String last_name, String email) {
		this.userName = userName;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
