package at.arz.ngs.security.login;

import at.arz.ngs.security.user.commands.UserData;

/**
 * Not forward this response to REST, only to UI. REST have to login every time. UI only one time in the beginning.
 * 
 */
public class LoginResponse {

	/**
	 * if null or, the login was not successful.
	 */
	private UserData user;

	public LoginResponse(UserData user) {
		this.user = user;
	}

	public UserData getUser() {
		return user;
	}

	public void setUser(UserData user) {
		this.user = user;
	}

}
