package at.arz.ngs.security.login;

/**
 * Not forward this response to REST, only to UI. REST have to login every time. UI only one time in the beginning.
 * 
 */
public class LoginResponse {

	/**
	 * if null or empty String, the login was not successful. This is the username out of active directory.
	 */
	private String userName;

	/**
	 * additional: e.g.: Max Mustermann
	 */
	private String employeeName;

	public LoginResponse(String userName, String employeeName) {
		super();
		this.userName = userName;
		this.employeeName = employeeName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

}
