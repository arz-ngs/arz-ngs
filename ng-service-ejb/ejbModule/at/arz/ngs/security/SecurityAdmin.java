package at.arz.ngs.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.api.Action;
import at.arz.ngs.api.Email;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.FirstName;
import at.arz.ngs.api.LastName;
import at.arz.ngs.api.RoleName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.UserName;
import at.arz.ngs.api.exception.EmptyField;
import at.arz.ngs.api.exception.LoginFailed;
import at.arz.ngs.api.exception.NoPermission;
import at.arz.ngs.api.exception.PermissionNotFound;
import at.arz.ngs.api.exception.RoleAlreadyExist;
import at.arz.ngs.api.exception.RoleAlreadyHasPermission;
import at.arz.ngs.api.exception.RoleNotFound;
import at.arz.ngs.api.exception.UserAlreadyHasRole;
import at.arz.ngs.api.exception.UserNotFound;
import at.arz.ngs.api.exception.User_RoleNotFound;
import at.arz.ngs.journal.JournalEntry;
import at.arz.ngs.journal.JournalRepository;
import at.arz.ngs.security.commands.Actor;
import at.arz.ngs.security.commands.login.Login;
import at.arz.ngs.security.commands.login.LoginResponse;
import at.arz.ngs.security.permission.commands.PermissionData;
import at.arz.ngs.security.permission.commands.addToRole.AddPermissionToRole;
import at.arz.ngs.security.permission.commands.get.PermissionResponse;
import at.arz.ngs.security.permission.commands.removeFromRole.RemovePermissionFromRole;
import at.arz.ngs.security.role.commands.UserRole;
import at.arz.ngs.security.role.commands.create.CreateRole;
import at.arz.ngs.security.role.commands.get.RoleResponse;
import at.arz.ngs.security.role.commands.get.RolesResponse;
import at.arz.ngs.security.role.commands.remove.RemoveRole;
import at.arz.ngs.security.user.commands.UserData;
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
	private User_RoleRepository userRoleRepository;
	
	@Inject
	private JournalRepository journalRepository;

	public static final String ADMIN = "Administrator";

	private boolean isJunitTest = false;
	private boolean isASUsingLDAP;
	private boolean isUsingLDAPPropSet;

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
	public SecurityAdmin(PermissionRepository permissionRepository, RoleRepository roleRepository,
			UserRepository userRepository, User_RoleRepository userRoleRepository, JournalRepository journalRepository) {
		this.permissionRepository = permissionRepository;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.journalRepository = journalRepository;
		isJunitTest = true;
	}

	public UserResponse getUserOverview() {
		UserResponse res = new UserResponse();
		for (User u : userRepository.getAllUsers()) {
			res.addUser(new UserData(u.getUserName().getName(), u.getFirstName().getName(), u.getLastName().getName(),
					u.getEmail().getEmail()));
		}

		return res;
	}

	public LoginResponse login(Login login) {
		if (login == null || login.getUserName() == null || login.getUserName().equals("")
				|| login.getPassword() == null) {
			throw new EmptyField("login field must be set");
		}

		if (login.getUserName().equals("admin") && login.getPassword().equals("admin")) { // TODO IMPORTANT: remove in
			// productional stage
			//Replaces InitalInserts
//			userRepository.addUser(new UserName("admin"), new FirstName("Vorname"), new LastName("Nachname"), new Email("test@mail.com"));
//			roleRepository.addRole(new RoleName(SecurityAdmin.ADMIN));
//			User u = userRepository.getUser(new UserName("admin"));
//			Role r = roleRepository.getRole(new RoleName(SecurityAdmin.ADMIN));
//			userRoleRepository.addUser_Role(u, r, true);
			return new LoginResponse(new UserData("admin", "Max", "Mustermann", "max.mustermann@email.at"));
		}

		UserData userData = adjustUser(login.getUserName());

		return new LoginResponse(userData);
	}

	/**
	 * Checks if the actor has the rights to perfom an action. If not an
	 * NoPermission Exception is thrown.
	 */
	public void proofPerformAction(EnvironmentName env, ServiceName service, Action action, Actor actor) {
		if (isAuthorizedToPerformAction(env, service, action, actor)) {
			return;
		}
		throw new NoPermission("The actor " + actor + " does not have permission to perform an action in environment "
				+ env.getName() + " on service " + service.getName() + "!");
	}
	
	public boolean isAuthorizedToPerformAction(EnvironmentName env, ServiceName service, Action action, Actor actor) {
		try {
			proofActorAdminAccess(actor);
			return true;
		}
		catch (NoPermission e) {
			User user = userRepository.getUser(new UserName(actor.getUserName()));
			for (User_Role ur : user.getUser_roles()) {
				Role role = ur.getRole();
				//				System.err.println("PROOFPERMISSION: ROLLE " + role.getRoleName().getName());
				for (Permission permission : role.getPermissions()) {
					EnvironmentName envName = permission.getEnvironmentName();
					ServiceName serviceName = permission.getServiceName();
					Action act = permission.getAction();
					if ((envName.equals(env) || envName.getName().equals("*"))
							&& (serviceName.equals(service) || serviceName.getName().equals("*"))
							&& (act.equals(action) || act.name().equals("all"))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Must be invoked to edit user, roles or permission to proof the actors
	 * access. Also for editing/new/remove ServiceInstance this method has to be
	 * used. Only Administrators have access to perfom changes. If no permission
	 * an NoPermission Exception is thrown.
	 */
	public void proofActorAdminAccess(Actor actor) {
		if (!isAdmin(actor)) {
			throw new NoPermission("The actor " + actor.getUserName()
					+ " does not have the permission to do this action. To change one must have to role '" + ADMIN
					+ "'!");
		}
	}

	public boolean isAdmin(Actor actor) {
		if (actor == null || actor.getUserName() == null || actor.getUserName().equals("")) {
			throw new EmptyField("actor was null or empty");
		}
		User user = userRepository.getUser(new UserName(actor.getUserName()));
		for (User_Role ur : user.getUser_roles()) {
			if (ur.getRole().getRoleName().getName().equals(ADMIN)) {
				return true; // ok, access is granted
			}
		}
		return false;
	}

	private void proofActorHasSameRoleAndHandoverRights(Actor actor, Role role) {
		User userActor = userRepository.getUser(new UserName(actor.getUserName()));
		for (User_Role actorUR : userActor.getUser_roles()) {
			if (actorUR.getRole().equals(role) && actorUR.isHandover()) {
				return; // ok user can perform further
			}
		}

		throw new NoPermission("The actor " + actor.getUserName()
				+ " does not have the permission to edit the targeted role. To do so one must have the same role as the person with the targeted role and the handvoer right!");
	}

	/**
	 * Adds a role to a user. If that user has not been added to DB yet he will
	 * be added. Only users with assigned roles are stored in DB.
	 * 
	 * @param command
	 */
	public void addRoleToUser(Actor actor, AddRoleToUser command) {
		boolean checkIfUserhasSameRole = false; // admin do not need the same role

		if (!isJunitTest) {
			try {
				proofActorAdminAccess(actor);
			}
			catch (NoPermission e) { // user is not an Admin -> check if he can handover his own role to other people
				checkIfUserhasSameRole = true;
			}
		}
		else {
			isJunitTest = false; // let junit test only one try to set the admin role
		}

		if (command == null || command.getUserName() == null || command.getUserName().equals("")
				|| command.getRoleName() == null || command.getRoleName().equals("")) {
			throw new EmptyField("Could not add role to user in order to empty fields.");
		}
		User userToAddTo = userRepository.getUser(new UserName(command.getUserName()));

		Role roleToAdd = roleRepository.getRole(new RoleName(command.getRoleName())); // if role not found -> rollback

		if (checkIfUserhasSameRole) {
			proofActorHasSameRoleAndHandoverRights(actor, roleToAdd);

		}
		try {
			userRoleRepository.getUser_Role(userToAddTo, roleToAdd);
			throw new UserAlreadyHasRole(userToAddTo.getUserName().getName(), roleToAdd.getRoleName().getName());
		}
		catch (User_RoleNotFound e) {
			userRoleRepository.addUser_Role(userToAddTo, roleToAdd, command.isHandover());
			journalRepository.addJournalEntry(new JournalEntry(new UserName(actor.getUserName()), "Role", roleToAdd.getOid(), "assign to " + userToAddTo.getUserName().getName()));
		}
	}

	public void removeRoleFromUser(Actor actor, RemoveRoleFromUser command) {
		boolean checkIfUserhasSameRole = false; // admin do not need the same role
		try {
			proofActorAdminAccess(actor);
		}
		catch (NoPermission e) { // user is not an Admin -> check if he can handover his own role to other people
			checkIfUserhasSameRole = true;
		}

		if (command == null || command.getUserName() == null || command.getUserName().equals("")
				|| command.getRoleName() == null || command.getRoleName().equals("")) {
			throw new EmptyField("Could not remove role from user in order to empty fields.");
		}

		User user = userRepository.getUser(new UserName(command.getUserName()));
		Role role = roleRepository.getRole(new RoleName(command.getRoleName()));

		if (checkIfUserhasSameRole) {
			proofActorHasSameRoleAndHandoverRights(actor, role);
		}

		removeRoleFromUser(role, user, actor);

	}

	private void removeRoleFromUser(Role role, User user, Actor actor) {
		User_Role user_Role = userRoleRepository.getUser_Role(user, role);
		userRoleRepository.removeUser_Role(user_Role);
		journalRepository.addJournalEntry(new JournalEntry(new UserName(actor.getUserName()), "Role", role.getOid(), "remove from " + user.getUserName().getName()));

		// remove user if no role is set. If removal did not succeed, because of references go further
		try {

			user.removeUser_Role(user_Role);
			role.removeUser_Role(user_Role);
		}
		catch (RuntimeException e) {
			// also ok, then go further
		}
	}

	public void createRole(Actor actor, CreateRole command) {
		proofActorAdminAccess(actor);

		if (command == null || command.getRoleName() == null || command.getRoleName().equals("")) {
			throw new EmptyField("Could not create role in order to empty fields.");
		}

		try {
			roleRepository.getRole(new RoleName(command.getRoleName()));
			throw new RoleAlreadyExist("The role " + command.getRoleName() + " is already existing");
		}
		catch (RoleNotFound e) {
			// if not exists
		}
		RoleName roleName = new RoleName(command.getRoleName());
		roleRepository.addRole(roleName);
		Role role = roleRepository.getRole(roleName);
		journalRepository.addJournalEntry(new JournalEntry(new UserName(actor.getUserName()), "Role", role.getOid(), "create"));
	}

	public void removeRole(Actor actor, RemoveRole command) {
		proofActorAdminAccess(actor);

		if (command == null || command.getRoleName() == null || command.getRoleName().equals("")) {
			throw new EmptyField("Could not remove role in order to empty fields.");
		}

		Role role = roleRepository.getRole(new RoleName(command.getRoleName()));

		if (role.getRoleName().getName().equals(ADMIN)) {
			throw new IllegalArgumentException("The administrator cannot be removed!");
		}

		for (User user : userRepository.getAllUsers()) {
			try {
				removeRoleFromUser(role, user, actor);
			}
			catch (User_RoleNotFound e) {
				// this user did not have this role -> continue
			}
		}
		List<Permission> permToDelete = role.getPermissions();
		roleRepository.removeRole(role);
		for (Permission p : permToDelete) {
			try {
				if (p.getRoles().size() <= 1) {
					permissionRepository.removePermission(p);
				}
			}
			catch (RuntimeException e) {
			}
		}
		journalRepository.addJournalEntry(new JournalEntry(new UserName(actor.getUserName()), "Role", role.getOid(), "remove"));
	}

	public RolesResponse getAllRoles() {
		RolesResponse res = new RolesResponse();

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
		proofActorAdminAccess(actor);

		if (command == null || command.getRoleName() == null || command.getRoleName().equals("")) {
			throw new EmptyField("Could not add permission to role in order to empty fields.");
		}

		PermissionData permissionData = command.getPermissionData();

		if (permissionData == null || permissionData.getAction() == null || permissionData.getAction().equals("")
				|| permissionData.getEnvironmentName() == null || permissionData.getEnvironmentName().equals("")
				|| permissionData.getServiceName() == null || permissionData.getServiceName().equals("")) {
			throw new EmptyField("Could not add permission to role in order to empty fields.");
		}

		Action action = convert(permissionData.getAction());

		Permission permission = getOrCreatePermission(new EnvironmentName(permissionData.getEnvironmentName()),
				new ServiceName(permissionData.getServiceName()), action);

		Role role = roleRepository.getRole(new RoleName(command.getRoleName()));
		if (!role.getPermissions().contains(permission) && !permission.getRoles().contains(role)) {
			role.addPermission(permission);
			permission.addRole(role);
			journalRepository.addJournalEntry(new JournalEntry(new UserName(actor.getUserName()), "Role", role.getOid(), "assign " + permission.getServiceName() + "/" + permission.getEnvironmentName() + "/" + permission.getAction().name()));

		}
		else {
			throw new RoleAlreadyHasPermission(role.getRoleName().getName(), permission.getEnvironmentName().getName()
					+ "/" + permission.getServiceName().getName() + "/" + permission.getAction().name());
		}

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

	public PermissionResponse getPermissions(String roleName) {
		if (roleName == null || roleName.equals("")) {
			throw new EmptyField("A rolename must be set.");
		}

		PermissionResponse res = new PermissionResponse(roleName);

		Role role = roleRepository.getRole(new RoleName(roleName));

		for (Permission p : role.getPermissions()) {
			String envName = convertStarToAll(p.getEnvironmentName().getName());
			String servName = convertStarToAll(p.getServiceName().getName());
			String action = p.getAction().name();
			if (action.equals("all")) {
				action = "alle";
			}
			res.addPermission(new PermissionData(envName, servName, action));
		}
		return res;
	}

	private String convertStarToAll(String value) {
		if (value.equals("*")) {
			return "alle";
		}
		return value;
	}

	public void removePermissionFromRole(Actor actor, RemovePermissionFromRole command) {
		proofActorAdminAccess(actor);

		if (command == null || command.getRoleName() == null || command.getRoleName().equals("")) {
			throw new EmptyField("Could not remove permission from role in order to empty fields.");
		}

		PermissionData permissionData = command.getPermissionData();

		if (permissionData == null || permissionData.getAction() == null || permissionData.getAction().equals("")
				|| permissionData.getEnvironmentName() == null || permissionData.getEnvironmentName().equals("")
				|| permissionData.getServiceName() == null || permissionData.getServiceName().equals("")) {
			throw new EmptyField("Could not remove permission from role in order to empty fields.");
		}

		Role role = roleRepository.getRole(new RoleName(command.getRoleName()));

		String envName = convertAllToStar(permissionData.getEnvironmentName());
		String servName = convertAllToStar(permissionData.getServiceName());
		String action = permissionData.getAction();
		if (action.equals("alle")) {
			action = "all";
		}

		Permission permission = permissionRepository.getPermission(new EnvironmentName(envName),
				new ServiceName(servName), convert(action));
		permission.removeRole(role);
		role.removePermission(permission);
		journalRepository.addJournalEntry(new JournalEntry(new UserName(actor.getUserName()), "Role", role.getOid(), "remove " + permission.getServiceName() + "/" + permission.getEnvironmentName() + "/" + permission.getAction().name()));


		// try to remove permission if not used anymore, if exception this permission is used
		try {
			permissionRepository.removePermission(permission);
		}
		catch (RuntimeException e) {
			// ok if not working due to referenced objects
		}
	}

	private String convertAllToStar(String value) {
		if (value.equals("alle")) {
			value = "*";
		}
		return value;
	}

	public UserData getUserDataFromUser(String userName) {
		User user = userRepository.getUser(new UserName(userName));
		return new UserData(userName, user.getFirstName().getName(), user.getLastName().getName(),
				user.getEmail().getEmail());
	}

	private Permission getOrCreatePermission(EnvironmentName environmentName, ServiceName serviceName, Action action) {
		try {
			return permissionRepository.getPermission(environmentName, serviceName, action);
		}
		catch (PermissionNotFound e) {
			permissionRepository.addPermission(environmentName, serviceName, action);
		}
		return permissionRepository.getPermission(environmentName, serviceName, action);
	}

	private UserData adjustUser(String userName) {
		if (userName == null || userName.equals("")) {
			throw new EmptyField("user identification is empty!");
		}

		UserData userData = null;

		if (isApplicationUsingsLDAPauth()) {
			LDAPConnector con = new LDAPConnector();

			userData = con.getUserData(userName);
		}
		else {
			//local use
			if (userName.equals("alex")) {
				userData = new UserData("alex", "Alexander", "Schiegl", "alexander.schiegl@gmx.at");
			}
			else if (userName.equals("daniel")) {
				userData = new UserData("daniel", "Daniel", "Testor", "daniel.testor@hotmail.com");
			}
			else {
				userData = new UserData(userName, "Test", "User", "test.user@email.com");
			}
		}

		if (userData == null) {
			throw new LoginFailed("Falsche Accountdaten. Bitte versuchen Sie es erneut.");
		}

		UserName name = new UserName(userData.getUserName());
		FirstName firstName = new FirstName(userData.getFirst_name());
		LastName lastName = new LastName(userData.getLast_name());
		Email email = new Email(userData.getEmail());

		try {

			User user = userRepository.getUser(name);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
		}
		catch (UserNotFound e) {
			userRepository.addUser(name, firstName, lastName, email);
		}

		return userData;
	}

	public RolesResponse getHandoverRolesFromActor(Actor actor) {
		if (isAdmin(actor)) {
			return getAllRoles();
		}
		else {
			User user = userRepository.getUser(new UserName(actor.getUserName()));
			RolesResponse response = new RolesResponse();
			for (User_Role ur : userRoleRepository.getUser_RoleByUser(user)) {
				if (ur.isHandover()) {
					response.addRole(ur.getRole().getRoleName().getName());
				}
			}
			return response;
		}
	}

	public boolean isApplicationUsingsLDAPauth() {
		if (isUsingLDAPPropSet) {
			return isASUsingLDAP;
		}

		String config_dir = System.getProperty("jboss.server.config.dir").replace(";", "");
		try {
			FileInputStream ldap_IS = new FileInputStream(config_dir + "/security_authentication.properties");
			Properties p = new Properties();
			p.load(ldap_IS);
			String property = p.getProperty("is_AS_using_ldap");
			if (property.equals("true")) {
				isASUsingLDAP = true;
				isUsingLDAPPropSet = true;
				return true;
			}
		}
		catch (IOException e) {
		}
		isASUsingLDAP = false;
		isUsingLDAPPropSet = true;
		return false;
	}
}
