package at.arz.ngs.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.api.Action;
import at.arz.ngs.api.Email;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.FirstName;
import at.arz.ngs.api.LastName;
import at.arz.ngs.api.RoleName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.UserName;
import at.arz.ngs.api.exception.NoPermission;
import at.arz.ngs.journal.JournalAdmin;
import at.arz.ngs.journal.jpa.JPAJournalRepository;
import at.arz.ngs.security.commands.Actor;
import at.arz.ngs.security.permission.commands.PermissionData;
import at.arz.ngs.security.permission.commands.addToRole.AddPermissionToRole;
import at.arz.ngs.security.permission.jpa.JPAPermissionRepository;
import at.arz.ngs.security.role.jpa.JPARoleRepository;
import at.arz.ngs.security.user.commands.addRole.AddRoleToUser;
import at.arz.ngs.security.user.jpa.JPAUserRepository;
import at.arz.ngs.security.userrole.jpa.JPAUser_RoleRepository;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.create.CreateNewServiceInstance;

public class SecurityAdminIT extends AbstractJpaIT {

	private SecurityAdmin securityAdmin;
	private PermissionRepository permissionRepository;
	private RoleRepository roleRepository;
	private UserRepository userRepository;
	private User_RoleRepository userRoleRepository;
	private JournalAdmin journalAdmin;

	@Before
	public void setUpBeforeClass() {
		permissionRepository = new JPAPermissionRepository(getEntityManager());
		roleRepository = new JPARoleRepository(getEntityManager());
		userRepository = new JPAUserRepository(getEntityManager());
		userRoleRepository = new JPAUser_RoleRepository(getEntityManager());
		journalAdmin = new JournalAdmin(SessionContextMother.authenticatedAs("admin"),
				new JPAJournalRepository(getEntityManager()));

		securityAdmin = new SecurityAdmin(permissionRepository, roleRepository, userRepository, userRoleRepository,
				journalAdmin, SessionContextMother.authenticatedAs("admin"));

		ScriptData scriptData = new ScriptData();
		scriptData.setPathStart("start");
		CreateNewServiceInstance command = new CreateNewServiceInstance();
		command.setEnvironmentName("env1");
		command.setHostName("host1");
		command.setInstanceName("instance1");
		command.setServiceName("serv1");
		command.setScript(scriptData);

		userRepository.addUser(new UserName("daniel"), new FirstName(""), new LastName(""), new Email(""));
		userRepository.addUser(new UserName("admin"), new FirstName(""), new LastName(""), new Email(""));
		userRepository.addUser(new UserName("alex"), new FirstName(""), new LastName(""), new Email(""));
		userRepository.addUser(new UserName("user1"), new FirstName(""), new LastName(""), new Email(""));
		userRepository.addUser(new UserName("user2"), new FirstName(""), new LastName(""), new Email(""));
		// roleRepository.addRole(new RoleName("Administrator"));
		roleRepository.addRole(new RoleName("entwickler"));

		// userRoleRepository.addUser_Role(userRepository.getUser(new UserName("daniel")),
		// roleRepository.getRole(new RoleName("entwickler")),
		// true);

		// securityAdmin.addPermissionToRole(actor, command);

		// to
		// admin-rights
		roleRepository.addRole(new RoleName(SecurityAdmin.ADMIN));
		AddRoleToUser addRoleToUserCommand = new AddRoleToUser("admin", SecurityAdmin.ADMIN, true);
		securityAdmin.setContext(SessionContextMother.authenticatedAs("admin"));
		securityAdmin.addRoleToUser(addRoleToUserCommand);

		securityAdmin.setContext(SessionContextMother.authenticatedAs("admin"));
	}

	@Test
	public void addRoleToUser() {
		Actor actor = new Actor(userRepository.getUser(new UserName("daniel")).getUserName().toString());
		Actor actor2 = new Actor(userRepository.getUser(new UserName("alex")).getUserName().toString());
		Actor actor3 = new Actor(userRepository.getUser(new UserName("user1")).getUserName().toString());
		Actor actor4 = new Actor(userRepository.getUser(new UserName("user2")).getUserName().toString());
		AddRoleToUser addRoleToUserCommand = new AddRoleToUser("daniel", "entwickler", false);
		AddRoleToUser addRoleToUserCommand2 = new AddRoleToUser("user2", "entwickler", true);

		securityAdmin.setContext(SessionContextMother.authenticatedAs("admin"));
		securityAdmin.addRoleToUser(addRoleToUserCommand2);
		assertEquals(2, roleRepository.getAllRoles().size());
		try {
			securityAdmin.setContext(SessionContextMother.authenticatedAs(actor.getUserName()));
			securityAdmin.addRoleToUser(addRoleToUserCommand);
			fail();
		}
		catch (NoPermission e) {
			// wanted
		}
		try {
			securityAdmin.setContext(SessionContextMother.authenticatedAs(actor2.getUserName()));
			securityAdmin.addRoleToUser(addRoleToUserCommand);
			fail();
		}
		catch (NoPermission e) {
			// wanted
		}
		try {
			securityAdmin.setContext(SessionContextMother.authenticatedAs(actor3.getUserName()));
			securityAdmin.addRoleToUser(addRoleToUserCommand);
			fail();
		}
		catch (NoPermission e) {
			// wanted
		}
		securityAdmin.setContext(SessionContextMother.authenticatedAs(actor4.getUserName()));
		securityAdmin.addRoleToUser(addRoleToUserCommand);
	}

	@Test
	public void proofPermission() {
		securityAdmin.setContext(SessionContextMother.authenticatedAs("admin"));
		Actor actor = new Actor(userRepository.getUser(new UserName("daniel")).getUserName().toString());
		AddRoleToUser addRoleToUserCommand = new AddRoleToUser("daniel", "entwickler", true);
		securityAdmin.addRoleToUser(addRoleToUserCommand);
		PermissionData permissionData = new PermissionData("env1", "serv1", Action.all.name());
		AddPermissionToRole addPermissionToRoleCommand = new AddPermissionToRole("entwickler", permissionData);
		securityAdmin.addPermissionToRole(addPermissionToRoleCommand);

		securityAdmin.proofPerformAction(new EnvironmentName("env1"), new ServiceName("serv1"), Action.all);
		securityAdmin.proofActorAdminAccess();

		securityAdmin.setContext(SessionContextMother.authenticatedAs(actor.getUserName()));
		try {
			securityAdmin.proofPerformAction(new EnvironmentName("env2"), new ServiceName("serv1"), Action.all);
			fail();
		}
		catch (NoPermission e) {
			// wanted
		}
		try {
			securityAdmin.proofPerformAction(new EnvironmentName("env1"), new ServiceName("serv2"), Action.all);
			fail();
		}
		catch (NoPermission e) {
			// wanted
		}
		try {
			securityAdmin.proofActorAdminAccess();
			fail();
		}
		catch (NoPermission e) {
			// wanted
		}
	}

	@Test
	public void proofPermission2() {
		securityAdmin.setContext(SessionContextMother.authenticatedAs("admin"));

		Actor actor = new Actor(userRepository.getUser(new UserName("daniel")).getUserName().toString());

		AddRoleToUser addRoleToUserCommand = new AddRoleToUser("daniel", "entwickler", true);
		securityAdmin.setContext(SessionContextMother.authenticatedAs("admin"));
		securityAdmin.addRoleToUser(addRoleToUserCommand);

		PermissionData permissionData = new PermissionData("env1", "serv1", Action.start.name());
		AddPermissionToRole addPermissionToRoleCommand = new AddPermissionToRole("entwickler", permissionData);
		securityAdmin.addPermissionToRole(addPermissionToRoleCommand);

		securityAdmin.setContext(SessionContextMother.authenticatedAs(actor.getUserName()));
		securityAdmin.proofPerformAction(new EnvironmentName("env1"), new ServiceName("serv1"), Action.start);
		try {
			securityAdmin.proofPerformAction(new EnvironmentName("env2"), new ServiceName("serv1"), Action.start);
			fail();
		}
		catch (NoPermission e) {
			// wanted
		}
		try {
			securityAdmin.proofPerformAction(new EnvironmentName("env1"), new ServiceName("serv2"), Action.start);
			fail();
		}
		catch (NoPermission e) {
			// wanted
		}
		try {
			securityAdmin.proofPerformAction(new EnvironmentName("env1"), new ServiceName("serv1"), Action.stop);
			fail();
		}
		catch (NoPermission e) {
			// wanted
		}
		try {
			securityAdmin.proofPerformAction(new EnvironmentName("*"), new ServiceName("serv1"), Action.stop);
			fail();
		}
		catch (NoPermission e) {
			// wanted
		}
	}

	@Test
	public void addPermissiontoRole() {
		Actor admin = new Actor(userRepository.getUser(new UserName("admin")).getUserName().toString());
		Actor actor = new Actor(userRepository.getUser(new UserName("daniel")).getUserName().toString());
		PermissionData permissionData = new PermissionData("env1", "serv1", Action.start.name());
		AddPermissionToRole addPermissionToRoleCommand = new AddPermissionToRole("entwickler", permissionData);

		securityAdmin.setContext(SessionContextMother.authenticatedAs(actor.getUserName()));
		try {
			securityAdmin.addPermissionToRole(addPermissionToRoleCommand);
			fail();
		}
		catch (NoPermission e) {
			// wanted
		}

		securityAdmin.setContext(SessionContextMother.authenticatedAs(admin.getUserName()));
		securityAdmin.addPermissionToRole(addPermissionToRoleCommand);
	}

	@Test
	public void proofPermission3() {
		Actor actor = new Actor(userRepository.getUser(new UserName("daniel")).getUserName().toString());
		Actor admin = new Actor(userRepository.getUser(new UserName("admin")).getUserName().toString());
		AddRoleToUser addRoleToUserCommand = new AddRoleToUser("daniel", "entwickler", true);

		securityAdmin.setContext(SessionContextMother.authenticatedAs(admin.getUserName()));
		securityAdmin.addRoleToUser(addRoleToUserCommand);
		try {
			securityAdmin.setContext(SessionContextMother.authenticatedAs(actor.getUserName()));
			securityAdmin.proofPerformAction(new EnvironmentName("env1"), new ServiceName("serv1"), Action.start);
			fail();
		}
		catch (NoPermission e) {
			// wanted
		}
		PermissionData permissionData = new PermissionData("env1", "serv1", Action.start.name());
		AddPermissionToRole addPermissionToRoleCommand = new AddPermissionToRole("entwickler", permissionData);
		PermissionData permissionData2 = new PermissionData("*", "serv1", Action.all.name());
		AddPermissionToRole addPermissionToRoleCommand2 = new AddPermissionToRole("entwickler", permissionData2);

		securityAdmin.setContext(SessionContextMother.authenticatedAs(admin.getUserName()));
		securityAdmin.addPermissionToRole(addPermissionToRoleCommand2);
		securityAdmin.addPermissionToRole(addPermissionToRoleCommand);

		securityAdmin.setContext(SessionContextMother.authenticatedAs(actor.getUserName()));
		securityAdmin.proofPerformAction(new EnvironmentName("env1"), new ServiceName("serv1"), Action.start);
		securityAdmin.proofPerformAction(new EnvironmentName("env1"), new ServiceName("serv1"), Action.stop);
		securityAdmin.proofPerformAction(new EnvironmentName("env2"), new ServiceName("serv1"), Action.start);
		try {
			securityAdmin.proofPerformAction(new EnvironmentName("env1"), new ServiceName("serv2"), Action.start);
			fail();
		}
		catch (NoPermission e) {
			// wanted
		}
	}

	@Test
	public void addMoreRolesToUser() {
		roleRepository.addRole(new RoleName("role1"));
		roleRepository.addRole(new RoleName("role2"));
		roleRepository.addRole(new RoleName("role3"));

		AddRoleToUser addRoleToUserCommand = new AddRoleToUser("daniel", "role1", true);
		Actor admin = new Actor(userRepository.getUser(new UserName("admin")).getUserName().toString());
		Actor actor = new Actor(userRepository.getUser(new UserName("daniel")).getUserName().toString());

		securityAdmin.setContext(SessionContextMother.authenticatedAs(actor.getUserName()));
		assertEquals(0, securityAdmin.getHandoverRolesFromActor().getRoles().size());

		securityAdmin.setContext(SessionContextMother.authenticatedAs(admin.getUserName()));
		securityAdmin.addRoleToUser(addRoleToUserCommand);

		securityAdmin.setContext(SessionContextMother.authenticatedAs(actor.getUserName()));
		assertEquals(1, securityAdmin.getHandoverRolesFromActor().getRoles().size());

		securityAdmin.setContext(SessionContextMother.authenticatedAs(admin.getUserName()));
		AddRoleToUser addRoleToUserCommand2 = new AddRoleToUser("daniel", "role2", false);
		securityAdmin.addRoleToUser(addRoleToUserCommand2);

		securityAdmin.setContext(SessionContextMother.authenticatedAs(actor.getUserName()));
		assertEquals(1, securityAdmin.getHandoverRolesFromActor().getRoles().size());
		AddRoleToUser addRoleToUserCommand3 = new AddRoleToUser("daniel", "role3", true);

		securityAdmin.setContext(SessionContextMother.authenticatedAs(admin.getUserName()));
		securityAdmin.addRoleToUser(addRoleToUserCommand3);

		securityAdmin.setContext(SessionContextMother.authenticatedAs(actor.getUserName()));
		assertEquals(2, securityAdmin.getHandoverRolesFromActor().getRoles().size());
	}
}
