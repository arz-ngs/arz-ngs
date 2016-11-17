package at.arz.ngs.journal;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.EnvironmentRepository;
import at.arz.ngs.HostRepository;
import at.arz.ngs.ScriptRepository;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.Email;
import at.arz.ngs.api.FirstName;
import at.arz.ngs.api.LastName;
import at.arz.ngs.api.RoleName;
import at.arz.ngs.api.UserName;
import at.arz.ngs.environment.jpa.JPAEnvironmentRepository;
import at.arz.ngs.host.jpa.JPAHostRepository;
import at.arz.ngs.journal.commands.get.JournalResponse;
import at.arz.ngs.journal.jpa.JPAJournalRepository;
import at.arz.ngs.script.jpa.JPAScriptRepository;
import at.arz.ngs.search.SearchEngine;
import at.arz.ngs.security.PermissionRepository;
import at.arz.ngs.security.RoleRepository;
import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.SessionContextMother;
import at.arz.ngs.security.UserRepository;
import at.arz.ngs.security.User_RoleRepository;
import at.arz.ngs.security.permission.jpa.JPAPermissionRepository;
import at.arz.ngs.security.role.jpa.JPARoleRepository;
import at.arz.ngs.security.user.commands.addRole.AddRoleToUser;
import at.arz.ngs.security.user.jpa.JPAUserRepository;
import at.arz.ngs.security.userrole.jpa.JPAUser_RoleRepository;
import at.arz.ngs.service.jpa.JPAServiceRepository;
import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.create.CreateNewServiceInstance;
import at.arz.ngs.serviceinstance.jpa.JPAServiceInstanceRepository;

public class JournalAdminIT extends AbstractJpaIT {

	private ServiceInstanceAdmin admin;

	private ServiceRepository services;
	private HostRepository hosts;
	private EnvironmentRepository environments;
	private ScriptRepository scripts;
	private ServiceInstanceRepository instances;

	private SecurityAdmin securityAdmin;
	private PermissionRepository permissionRepository;
	private RoleRepository roleRepository;
	private UserRepository userRepository;
	private User_RoleRepository userRoleRepository;
	private JournalRepository journalRepository;
	private JournalAdmin journalAdmin;

	@Before
	public void setUpBefore() {
		services = new JPAServiceRepository(getEntityManager());
		hosts = new JPAHostRepository(getEntityManager());
		environments = new JPAEnvironmentRepository(getEntityManager());
		scripts = new JPAScriptRepository(getEntityManager());
		instances = new JPAServiceInstanceRepository(getEntityManager());
		permissionRepository = new JPAPermissionRepository(getEntityManager());
		roleRepository = new JPARoleRepository(getEntityManager());
		userRepository = new JPAUserRepository(getEntityManager());
		userRoleRepository = new JPAUser_RoleRepository(getEntityManager());
		journalRepository = new JPAJournalRepository(getEntityManager());
		journalAdmin = new JournalAdmin(SessionContextMother.authenticatedAs("admin"), journalRepository);

		securityAdmin = new SecurityAdmin(permissionRepository, roleRepository, userRepository, userRoleRepository,
				journalAdmin, SessionContextMother.authenticatedAs("admin"));
		admin = new ServiceInstanceAdmin(services, hosts, environments, instances, scripts,
				new SearchEngine(getEntityManager()), securityAdmin, journalAdmin);

		String environmentName = "environment1";
		String hostName = "host1";
		String serviceName = "service1";
		String instanceName = "instance1";

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName("scriptName1");
		scriptData.setPathStart("start1");
		scriptData.setPathStop("stop1");
		scriptData.setPathRestart("restart1");
		scriptData.setPathStatus("status1");

		userRepository.addUser(new UserName("admin"), new FirstName(""), new LastName(""), new Email(""));

		roleRepository.addRole(new RoleName(SecurityAdmin.ADMIN));
		AddRoleToUser addRoleToUserCommand = new AddRoleToUser("admin", SecurityAdmin.ADMIN, true);

		securityAdmin.addRoleToUser(addRoleToUserCommand);

		CreateNewServiceInstance command = new CreateNewServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);
		admin.createNewServiceInstance(command);
	}

	@Test
	public void checkServiceInstanceJournal() {
		List<JournalResponse> response = journalAdmin.getAllJournalEntries();

		JournalResponse jr = response.get(1); //the last element is the oldest one (chronologically)
		assertEquals("admin", jr.getUserName());
		assertEquals("User_Role", jr.getTargetObject_class());
		assertEquals("Administrator/admin", jr.getTargetObject_uniqueKey());

		JournalResponse jr2 = response.get(0);
		assertEquals("admin", jr2.getUserName());
		assertEquals("ServiceInstance", jr2.getTargetObject_class());
		assertEquals("service1/environment1/host1/instance1", jr2.getTargetObject_uniqueKey());
	}
}
