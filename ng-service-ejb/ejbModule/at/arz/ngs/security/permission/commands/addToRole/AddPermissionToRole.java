package at.arz.ngs.security.permission.commands.addToRole;

import at.arz.ngs.security.permission.commands.PermissionData;

public class AddPermissionToRole {

	private String roleName;

	private PermissionData permissionData;

	public AddPermissionToRole(String roleName, PermissionData permissionData) {
		this.roleName = roleName;
		this.permissionData = permissionData;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public PermissionData getPermissionData() {
		return permissionData;
	}

	public void setPermissionData(PermissionData permissionData) {
		this.permissionData = permissionData;
	}

}
