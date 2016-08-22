package at.arz.ngs.security;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class SecurityAdmin {

	@Inject
	PermissionRepository permissionRepository;

	@Inject
	RoleRepository roleRepository;

	@Inject
	UserRepository userRepository;

	protected SecurityAdmin() {
		// ejb constructor
	}

	/**
	 * Only for JUnit-Tests
	 * 
	 * @param permissionRepository
	 * @param roleRepository
	 * @param userRepository
	 */
	public SecurityAdmin(	PermissionRepository permissionRepository,
							RoleRepository roleRepository,
							UserRepository userRepository) {
		super();
		this.permissionRepository = permissionRepository;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}


}
