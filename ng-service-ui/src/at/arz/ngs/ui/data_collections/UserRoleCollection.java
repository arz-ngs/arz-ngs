package at.arz.ngs.ui.data_collections;

import at.arz.ngs.security.role.commands.UserRole;

public class UserRoleCollection {
	private boolean removeFieldRendered;

	private UserRole userRole;

	public UserRoleCollection(boolean removeFieldRendered, UserRole userRole) {
		super();
		this.removeFieldRendered = removeFieldRendered;
		this.userRole = userRole;
	}

	public boolean isRemoveFieldRendered() {
		return removeFieldRendered;
	}

	public void setRemoveFieldRendered(boolean removeFieldRendered) {
		this.removeFieldRendered = removeFieldRendered;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
}
