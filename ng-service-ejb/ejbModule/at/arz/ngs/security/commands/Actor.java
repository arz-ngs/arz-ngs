package at.arz.ngs.security.commands;

/**
 * An actor is the person who perfoms an action or an update/add/remove of an ServiceInstance.
 *
 */
public class Actor {

	private String userName;

	public Actor(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
