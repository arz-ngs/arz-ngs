package at.arz.ngs.security.permission.commands.get;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import at.arz.ngs.security.permission.commands.PermissionData;

public class PermissionResponse {

	private String roleName;

	private List<PermissionData> permissions;

	public PermissionResponse(String roleName) {
		this.roleName = roleName;
		this.permissions = new LinkedList<>();
	}

	public String getRoleName() {
		return roleName;
	}

	public List<PermissionData> getPermissions() {
		return Collections.unmodifiableList(permissions);
	}

	public void addPermission(PermissionData permission) {
		permissions.add(permission);
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
