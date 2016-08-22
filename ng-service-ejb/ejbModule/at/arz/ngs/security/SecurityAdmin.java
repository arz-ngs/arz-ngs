package at.arz.ngs.security;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.api.RoleName;
import at.arz.ngs.api.UserName;
import at.arz.ngs.api.exception.EmptyField;
import at.arz.ngs.api.exception.NoPermission;
import at.arz.ngs.security.commands.Actor;
import at.arz.ngs.security.user.commands.addRole.AddRoleToUser;
import at.arz.ngs.security.user.commands.get.UserResponse;

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
		this.permissionRepository = permissionRepository;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}

	public UserResponse getUserOverview() {
		return null; // TODO -> ldap
	}

	/**
	 * Must be invoked to edit user, roles or permission.
	 */
	private void isActorAllowedToChangeSecurity(Actor actor) {
		// TODO check if actor is allowed to edit
		throw new NoPermission("The actor "+ actor.getUserName()
								+ " does not have the permission to edit security settings. To change one must have to role 'Admin'!");
	}

	/**
	 * Adds a role to a user. If that user has not been added to DB yet he will be added. Only users with assigned roles
	 * are stored in DB.
	 * 
	 * @param command
	 */
	private void addRoleToUser(Actor actor, AddRoleToUser command) {
		isActorAllowedToChangeSecurity(actor);

		if (command == null|| command.getUserName() == null
			|| command.getUserName().equals("")
			|| command.getRoleName() == null
			|| command.getRoleName().equals("")) {
			throw new EmptyField("Could not add role to user in order to empty fields.");
		}
		User user = getOrCreateUser(new UserName(command.getUserName()));

		Role role = roleRepository.getRole(new RoleName(command.getRoleName())); // if role not found -> rollback

	}

	private User getOrCreateUser(UserName user) {
		return null;
	}
}
