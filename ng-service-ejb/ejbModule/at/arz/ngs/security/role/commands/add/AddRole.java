package at.arz.ngs.security.role.commands.add;


public class AddRole {

	private String roleName;

	public AddRole(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
