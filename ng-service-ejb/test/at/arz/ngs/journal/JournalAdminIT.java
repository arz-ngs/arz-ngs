package at.arz.ngs.journal;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.Query;

import org.junit.After;
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
import at.arz.ngs.security.UserRepository;
import at.arz.ngs.security.User_RoleRepository;
import at.arz.ngs.security.commands.Actor;
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

public class JournalAdminIT extends AbstractJpaIT{

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

	private Actor actor;
	
	@Before
	public void setUpBefore(){
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
		securityAdmin = new SecurityAdmin(permissionRepository, roleRepository, userRepository, userRoleRepository, journalRepository);
		admin = new ServiceInstanceAdmin(	services,
											hosts,
											environments,
											instances,
											scripts,
											new SearchEngine(getEntityManager()),
											securityAdmin,
											journalRepository);
		journalAdmin = new JournalAdmin(journalRepository, instances, roleRepository);

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
		CreateNewServiceInstance command = new CreateNewServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);
		userRepository.addUser(new UserName("admin"), new FirstName(""), new LastName(""), new Email(""));
		actor = new Actor(userRepository.getUser(new UserName("admin")).getUserName().toString()); // preset admin
		roleRepository.addRole(new RoleName(SecurityAdmin.ADMIN));
		AddRoleToUser addRoleToUserCommand = new AddRoleToUser("admin", SecurityAdmin.ADMIN, true);
		securityAdmin.addRoleToUser(actor, addRoleToUserCommand);
		admin.createNewServiceInstance(actor, command);
	}
	
	@Test
	public void checkServiceInstanceJournal() {
		List<JournalResponse> response = journalAdmin.getAllJournalEntries();
		JournalResponse jr = response.get(0);
		assertEquals("admin", jr.getUserName());
		assertEquals("Role", jr.getTargetObject_class());
		assertEquals("Administrator", jr.getTargetObject());
		
		
		JournalResponse jr2 = response.get(1);
		assertEquals("admin", jr2.getUserName());
		assertEquals("ServiceInstance", jr2.getTargetObject_class());
		assertEquals("service1/environment1/host1/instance1", jr2.getTargetObject());
	}
	
	@After
	public void cleanup() {
		Query d1 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICEINSTANCE");
		d1.executeUpdate();
		Query d2 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICE");
		d2.executeUpdate();
		Query d3 = super.getEntityManager().createNativeQuery("DROP TABLE HOST");
		d3.executeUpdate();
		Query d4 = super.getEntityManager().createNativeQuery("DROP TABLE ENVIRONMENT");
		d4.executeUpdate();
		Query d5 = super.getEntityManager().createNativeQuery("DROP TABLE SCRIPT");
		d5.executeUpdate();
		Query d7 = super.getEntityManager().createNativeQuery("DROP TABLE USER_ROLE");
		d7.executeUpdate();
		Query d8 = super.getEntityManager().createNativeQuery("DROP TABLE USER_");
		d8.executeUpdate();
		Query d10 = super.getEntityManager().createNativeQuery("DROP TABLE PERMISSION_ROLE"); // jpa generated table
		d10.executeUpdate();
		Query d9 = super.getEntityManager().createNativeQuery("DROP TABLE ROLE");
		d9.executeUpdate();
		Query d6 = super.getEntityManager().createNativeQuery("DROP TABLE PERMISSION");
		d6.executeUpdate();
		Query d11 = super.getEntityManager().createNativeQuery("DROP TABLE JOURNALENTRY");
		d11.executeUpdate();
	}
}
