package at.arz.ngs.security.role.commands.get;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AllRoleResponse {

	private List<String> roles;

	public AllRoleResponse() {
		roles = new LinkedList<>();
	}

	public List<String> getRoles() {
		return Collections.unmodifiableList(roles);
	}

	public void addRole(String role) {
		roles.add(role);
	}
}
