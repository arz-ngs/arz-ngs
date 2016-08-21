package at.arz.ngs.security;

import java.util.List;

import at.arz.ngs.api.RoleName;

public interface RoleRepository {
	Role getRole(RoleName roleName);
	
	List<Role> getAllRoles();
	
	void addRole(RoleName roleName);
	
	void removeRole(Role role);
}
