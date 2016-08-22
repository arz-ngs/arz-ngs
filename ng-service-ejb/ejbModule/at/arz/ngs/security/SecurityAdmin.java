package at.arz.ngs.security;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.api.Action;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.RoleName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.UserName;
import at.arz.ngs.api.exception.EmptyField;
import at.arz.ngs.api.exception.NoPermission;
import at.arz.ngs.api.exception.PermissionNotFound;
import at.arz.ngs.api.exception.RoleAlreadyExist;
import at.arz.ngs.api.exception.RoleNotFound;
import at.arz.ngs.api.exception.UserNotFound;
import at.arz.ngs.api.exception.User_RoleNotFound;
import at.arz.ngs.security.commands.Actor;
import at.arz.ngs.security.permission.commands.PermissionData;
import at.arz.ngs.security.permission.commands.addToRole.AddPermissionToRole;
import at.arz.ngs.security.role.commands.UserRole;
import at.arz.ngs.security.role.commands.create.CreateRole;
import at.arz.ngs.security.role.commands.get.AllRolesResponse;
import at.arz.ngs.security.role.commands.get.RoleResponse;
import at.arz.ngs.security.role.commands.remove.RemoveRole;
import at.arz.ngs.security.user.commands.addRole.AddRoleToUser;
import at.arz.ngs.security.user.commands.get.UserResponse;
import at.arz.ngs.security.user.commands.removeRole.RemoveRoleFromUser;

@Stateless
public class SecurityAdmin {

	@Inject
	private PermissionRepository permissionRepository;

	@Inject
	private RoleRepository roleRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private User_RoleRepository repository;

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
	public void addRoleToUser(Actor actor, AddRoleToUser command) {
		isActorAllowedToChangeSecurity(actor);

		if (command == null|| command.getUserName() == null
			|| command.getUserName().equals("")
			|| command.getRoleName() == null
			|| command.getRoleName().equals("")) {
			throw new EmptyField("Could not add role to user in order to empty fields.");
		}
		User user = getOrCreateUser(new UserName(command.getUserName()));

		Role role = roleRepository.getRole(new RoleName(command.getRoleName())); // if role not found -> rollback

		repository.addUser_Role(user, role, command.isHandover());
	}

	public void removeRoleFromUser(Actor actor, RemoveRoleFromUser command) {
		isActorAllowedToChangeSecurity(actor);

		if (command == null|| command.getUserName() == null
			|| command.getUserName().equals("")
			|| command.getRoleName() == null
			|| command.getRoleName().equals("")) {
			throw new EmptyField("Could not remove role from user in order to empty fields.");
		}

		User user = userRepository.getUser(new UserName(command.getUserName()));
		Role role = roleRepository.getRole(new RoleName(command.getRoleName()));

		removeRoleFromUser(role, user);
	}

	private void removeRoleFromUser(Role role, User user) {
		User_Role user_Role = repository.getUser_Role(user, role);
		repository.removeUser_Role(user_Role); // TODO if no role is referenced to a user, delete this user
	}

	public void createRole(Actor actor, CreateRole command) {
		isActorAllowedToChangeSecurity(actor);

		if (command == null || command.getRoleName() == null || command.getRoleName().equals("")) {
			throw new EmptyField("Could not create role in order to empty fields.");
		}

		try {
			roleRepository.getRole(new RoleName(command.getRoleName()));
			throw new RoleAlreadyExist("The role " + command.getRoleName() + " is already existing");
		} catch (RoleNotFound e) {
			// if not exists
		}
		roleRepository.addRole(new RoleName(command.getRoleName()));
	}

	public void removeRole(Actor actor, RemoveRole command) {
		isActorAllowedToChangeSecurity(actor);

		if (command == null || command.getRoleName() == null || command.getRoleName().equals("")) {
			throw new EmptyField("Could not remove role in order to empty fields.");
		}

		Role role = roleRepository.getRole(new RoleName(command.getRoleName()));

		for (User user : userRepository.getAllUsers()) {
			try {
				removeRoleFromUser(role, user);
			} catch (User_RoleNotFound e) {
				// this user did not have this role -> continue
			}
		}

		roleRepository.removeRole(role);
	}

	public AllRolesResponse getAllRoles() {
		AllRolesResponse res = new AllRolesResponse();

		List<Role> allRoles = roleRepository.getAllRoles();
		for (Role r : allRoles) {
			res.addRole(r.getRoleName().getName());
		}

		return res;
	}

	public RoleResponse getRolesForUser(String userName) {
		if (userName == null || userName.equals("")) {
			throw new EmptyField("A username must be set.");
		}

		RoleResponse res = new RoleResponse(userName);

		User user = userRepository.getUser(new UserName(userName));
		for (User_Role ur : user.getUser_roles()) {
			res.addUserRole(new UserRole(ur.getRole().getRoleName().getName(), ur.isHandover()));
		}

		return res;
	}

	public void addPermissionToRole(Actor actor, AddPermissionToRole command) {
		isActorAllowedToChangeSecurity(actor);

		if (command == null || command.getRoleName() == null || command.getRoleName().equals("")) {
			throw new EmptyField("Could not add permission to role in order to empty fields.");
		}

		PermissionData permissionData = command.getPermissionData();

		if (permissionData == null|| permissionData.getAction() == null
			|| permissionData.getAction().equals("")
			|| permissionData.getEnvironmentName() == null
			|| permissionData.getEnvironmentName().equals("")
			|| permissionData.getServiceName() == null
			|| permissionData.getServiceName().equals("")) {
			throw new EmptyField("Could not add permission to role in order to empty fields.");
		}

		Action action = convert(permissionData.getAction());

		Permission permission = getOrCreatePermission(	new EnvironmentName(permissionData.getEnvironmentName()),
														new ServiceName(permissionData.getServiceName()),
														action);

		Role role = roleRepository.getRole(new RoleName(command.getRoleName()));
		role.addPermission(permission);
	}

	private Action convert(String action) {
		switch (action) {
			case "all":
				return Action.all;
			case "start":
				return Action.start;
			case "stop":
				return Action.stop;
			case "restart":
				return Action.restart;
			case "status":
				return Action.status;
			default:
				throw new IllegalArgumentException("Actionparam must be an valid action.");
		}
	}

	/**
	 * 
	 * @param user Must not be null.
	 * @return
	 */
	private User getOrCreateUser(UserName userName) {
		try {
			return userRepository.getUser(userName);
		} catch (UserNotFound e) {
			userRepository.addUser(userName);
		}
		return userRepository.getUser(userName);
	}

	private Permission getOrCreatePermission(EnvironmentName environmentName, ServiceName serviceName, Action action) {
		try {
			return permissionRepository.getPermission(environmentName, serviceName, action);
		} catch (PermissionNotFound e) {
			permissionRepository.addPermission(environmentName, serviceName, action);
		}
		return permissionRepository.getPermission(environmentName, serviceName, action);
	}
}
