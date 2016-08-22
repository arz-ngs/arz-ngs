package at.arz.ngs.security.role.commands.remove;


public class RemoveRole {

	private String roleName;

	public RemoveRole(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
