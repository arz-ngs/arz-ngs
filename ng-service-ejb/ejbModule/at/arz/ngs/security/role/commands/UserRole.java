package at.arz.ngs.security.role.commands;


public class UserRole {

	private String roleName;

	private boolean handover;

	public UserRole(String roleName, boolean handover) {
		this.roleName = roleName;
		this.handover = handover;
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
