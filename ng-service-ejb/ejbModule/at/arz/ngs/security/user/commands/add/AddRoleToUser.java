package at.arz.ngs.security.user.commands.add;


public class AddRoleToUser {

	private String userName;

	private String roleName;

	private boolean handover;

	public AddRoleToUser(String userName, String roleName, boolean handover) {
		this.userName = userName;
		this.roleName = roleName;
		this.handover = handover;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean isHandover() {
		return handover;
	}

	public void setHandover(boolean handover) {
		this.handover = handover;
	}

}
