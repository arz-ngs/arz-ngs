package at.arz.ngs.security.role.commands.get;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import at.arz.ngs.security.role.commands.UserRole;

/**
 * Response-object for getting all roles for a single user.
 * 
 * @author alex
 *
 */
public class RoleResponse {

	private String userName;

	private List<UserRole> userRoles;

	public RoleResponse(String userName) {
		this.userName = userName;
		userRoles = new LinkedList<>();
	}

	public void addUserRole(UserRole userRole) {
		userRoles.add(userRole);
	}

	public List<UserRole> getUserRoles() {
		return Collections.unmodifiableList(userRoles);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
