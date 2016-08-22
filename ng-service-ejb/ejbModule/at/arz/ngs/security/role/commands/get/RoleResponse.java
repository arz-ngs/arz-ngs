package at.arz.ngs.security.role.commands.get;

import java.util.LinkedList;
import java.util.List;

public class RoleResponse {

	private List<UserRole> userRoles;

	public RoleResponse() {
		userRoles = new LinkedList<>();
	}

	public void addUserRole(UserRole userRole) {
		userRoles.add(userRole);
	}
}
