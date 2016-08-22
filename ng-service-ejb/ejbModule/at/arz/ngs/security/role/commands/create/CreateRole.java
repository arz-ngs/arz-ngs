package at.arz.ngs.security.role.commands.create;


public class CreateRole {

	private String roleName;

	public CreateRole(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
