package at.arz.ngs.serviceinstance.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import at.arz.ngs.api.exception.AlreadyModified;
import at.arz.ngs.api.exception.EmptyField;
import at.arz.ngs.api.exception.ServiceNotFound;
import at.arz.ngs.environment.jpa.JPAEnvironmentRepository;
import at.arz.ngs.host.jpa.JPAHostRepository;
import at.arz.ngs.script.jpa.JPAScriptRepository;
import at.arz.ngs.search.SearchEngine;
import at.arz.ngs.service.jpa.JPAServiceRepository;
import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.create.CreateNewServiceInstance;
import at.arz.ngs.serviceinstance.commands.get.ServiceInstanceResponse;
import at.arz.ngs.serviceinstance.commands.remove.RemoveServiceInstance;
import at.arz.ngs.serviceinstance.commands.update.UpdateServiceInstance;

public class ServiceInstanceAdminIT
		extends AbstractJpaIT {

	private ServiceInstanceAdmin admin;

	private ServiceRepository services;
	private HostRepository hosts;
	private EnvironmentRepository environments;
	private ScriptRepository scripts;
	private ServiceInstanceRepository instances;

	@Before
	public void setUpBefore() throws Exception {
		services = new JPAServiceRepository(getEntityManager());
		hosts = new JPAHostRepository(getEntityManager());
		environments = new JPAEnvironmentRepository(getEntityManager());
		scripts = new JPAScriptRepository(getEntityManager());
		instances = new JPAServiceInstanceRepository(getEntityManager());
		admin = new ServiceInstanceAdmin(	services,
											hosts,
											environments,
											instances,
											scripts,
											new SearchEngine(getEntityManager()));
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
		admin.createNewServiceInstance(command);
	}

	@Test
	public void addServiceInstanceTest() {
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());

		String environmentName = "environment2";
		String hostName = "host2";
		String serviceName = "service2";
		String instanceName = "instance2";

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName("scriptName2");
		scriptData.setPathStart("start2");
		scriptData.setPathStop("stop2");
		scriptData.setPathRestart("restart2");
		scriptData.setPathStatus("status2");
		CreateNewServiceInstance command = new CreateNewServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);
		admin.createNewServiceInstance(command);

		ServiceInstanceResponse response = admin.getServiceInstance(serviceName,
																	environmentName,
																	hostName,
																	instanceName);
		assertEquals("environment2", response.getEnvironmentName());
		assertEquals("host2", response.getHostName());
		assertEquals("service2", response.getServiceName());
		assertEquals("instance2", response.getInstanceName());
		assertEquals("start2", response.getScript().getPathStart());
		assertEquals(0, response.getVersion());
		assertFalse(response.getScript().getScriptName().contains("@@"));
		assertEquals(2, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
	}

	@Test
	public void addServiceInstanceParamNullTest() {
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());

		String environmentName = "";
		String hostName = "host2";
		String serviceName = null;
		String instanceName = null;

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName("scriptName2");
		scriptData.setPathStart("start2");
		scriptData.setPathStop("stop2");
		scriptData.setPathRestart("restart2");
		scriptData.setPathStatus("status2");
		CreateNewServiceInstance command = new CreateNewServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);

		try {
			admin.createNewServiceInstance(command);
			fail();
		} catch (EmptyField e) {
			// ok
		}
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
	}

	@Test
	public void addServiceInstanceParamNullTest2() {
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());

		String environmentName = "asdf";
		String hostName = "";
		String serviceName = null;
		String instanceName = "instance2";

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName("scriptName2");
		scriptData.setPathStart("start2");
		scriptData.setPathStop("stop2");
		scriptData.setPathRestart("restart2");
		scriptData.setPathStatus("status2");
		CreateNewServiceInstance command = new CreateNewServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);

		try {
			admin.createNewServiceInstance(command);
			fail();
		} catch (EmptyField e) {
			// ok
		}
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
	}

	@Test
	public void addServiceInstanceNullTest3() {
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());

		String environmentName = "asdfasdf";
		String hostName = "host2";
		String serviceName = "adsfasdf";
		String instanceName = "instance2";

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName("scriptName2");
		scriptData.setPathStart(null);
		scriptData.setPathStop("");
		scriptData.setPathRestart("restart2");
		scriptData.setPathStatus("status2");
		CreateNewServiceInstance command = new CreateNewServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);

		admin.createNewServiceInstance(command);
		assertEquals(2, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
	}

	@Test
	public void addServiceInstanceScriptNameNullorEmptyTestShouldGenerateScriptName() {
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());

		String environmentName = "asdfasdfadf";
		String hostName = "host2";
		String serviceName = "adsfasdf";
		String instanceName = "instance2";

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName("");
		scriptData.setPathStart("1234");
		scriptData.setPathStop("stop");
		scriptData.setPathRestart("restart2");
		scriptData.setPathStatus("status2");
		CreateNewServiceInstance command = new CreateNewServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);

		admin.createNewServiceInstance(command);

		ServiceInstanceResponse serviceInstance = admin.getServiceInstance(	serviceName,
																			environmentName,
																			hostName,
																			instanceName);
		assertTrue(serviceInstance.getScript().getPathStop().equals("stop"));
		assertTrue(serviceInstance.getScript().getScriptName().contains("@@"));
		System.err.println("\n" + serviceInstance.getScript().getScriptName() + "\n");
		assertEquals(2, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
	}

	@Test
	public void deleteServiceInstanceTest() {
		String environmentName = "environment1";
		String hostName = "host1";
		String serviceName = "service1";
		String instanceName = "instance1";

		RemoveServiceInstance command = new RemoveServiceInstance();
		command.setVersion(1); // current version in DB is 0 so it should not work the first time

		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
		try {
			admin.removeServiceInstance(command, serviceName, environmentName, hostName, instanceName);
			fail();
		} catch (AlreadyModified e) {
			// ok
		}
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());

		command.setVersion(0);
		admin.removeServiceInstance(command, serviceName, environmentName, hostName, instanceName);

		try {
			admin.getServiceInstance(serviceName, environmentName, hostName, instanceName);
			fail();
		} catch (ServiceNotFound e) {
			// ok
		}

		assertEquals(0, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
	}

	@Test
	public void updateServiceInstanceTest() {
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
		String environmentName = "environment2";
		String hostName = "host2";
		String serviceName = "service2";
		String instanceName = "instance2";

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName("scriptName2");
		scriptData.setPathStart("start2");
		scriptData.setPathStop("stop2");
		scriptData.setPathRestart("restart2");
		scriptData.setPathStatus("status2");
		UpdateServiceInstance command = new UpdateServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);
		command.setVersion(0);

		String oldServiceName = "service1";
		String oldEnvironmentName = "environment1";
		String oldHostName = "host1";
		String oldInstanceName = "instance1";

		admin.updateServiceInstance(command, oldServiceName, oldEnvironmentName, oldHostName, oldInstanceName);

		ServiceInstanceResponse response = admin.getServiceInstance(serviceName,
																	environmentName,
																	hostName,
																	instanceName);
		assertEquals("environment2", response.getEnvironmentName());
		assertEquals("host2", response.getHostName());
		assertEquals("service2", response.getServiceName());
		assertEquals("instance2", response.getInstanceName());
		assertEquals("scriptName2", response.getScript().getScriptName());
		assertEquals("restart2", response.getScript().getPathRestart());
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
	}

	@Test
	public void updateServiceInstanceTest2() {
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());

		String environmentName = "environment2";
		String hostName = "host2";
		String serviceName = "service1";
		String instanceName = "instance1";

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName("scriptName2");
		scriptData.setPathStart("start2");
		scriptData.setPathStop("stop2");
		scriptData.setPathRestart("restart1");
		scriptData.setPathStatus("status2");
		UpdateServiceInstance command = new UpdateServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);
		command.setVersion(0);

		String oldServiceName = "service1";
		String oldEnvironmentName = "environment1";
		String oldHostName = "host1";
		String oldInstanceName = "instance1";

		admin.updateServiceInstance(command, oldServiceName, oldEnvironmentName, oldHostName, oldInstanceName);

		ServiceInstanceResponse response = admin.getServiceInstance(serviceName,
																	environmentName,
																	hostName,
																	instanceName);
		assertEquals("environment2", response.getEnvironmentName());
		assertEquals("host2", response.getHostName());
		assertEquals("service1", response.getServiceName());
		assertEquals("instance1", response.getInstanceName());
		assertEquals("scriptName2", response.getScript().getScriptName());
		assertEquals("status2", response.getScript().getPathStatus());
		assertEquals("restart1", response.getScript().getPathRestart());

		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
	}

	@Test
	public void updateServiceInstanceTestVersionAlreadyModifiedNegativeInteger() {
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());

		String environmentName = "environment2";
		String hostName = "host2";
		String serviceName = "service1";
		String instanceName = "instance1";

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName("scriptName2");
		scriptData.setPathStart("start2");
		scriptData.setPathStop("stop2");
		scriptData.setPathRestart("restart1");
		scriptData.setPathStatus("status2");
		UpdateServiceInstance command = new UpdateServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);
		command.setVersion(-1);

		String oldServiceName = "service1";
		String oldEnvironmentName = "environment1";
		String oldHostName = "host1";
		String oldInstanceName = "instance1";
		try {
			admin.updateServiceInstance(command, oldServiceName, oldEnvironmentName, oldHostName, oldInstanceName);
			fail();
		} catch (AlreadyModified e) {
			// ok
		}

		command.setVersion(0);
		admin.updateServiceInstance(command, oldServiceName, oldEnvironmentName, oldHostName, oldInstanceName);

		ServiceInstanceResponse response = admin.getServiceInstance(serviceName,
																	environmentName,
																	hostName,
																	instanceName);
		assertEquals("environment2", response.getEnvironmentName());
		assertEquals("host2", response.getHostName());
		assertEquals("service1", response.getServiceName());
		assertEquals("instance1", response.getInstanceName());
		assertEquals("scriptName2", response.getScript().getScriptName());
		assertEquals("restart1", response.getScript().getPathRestart());
		assertEquals(1, response.getVersion());

		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
	}

	@Test
	public void updateServiceInstanceTestVersionAlreadyModifiedPositiveInteger() {

		String environmentName = "environment1";
		String hostName = "host1";
		String serviceName = "service1";
		String instanceName = "instance1";

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName("scriptName2");
		scriptData.setPathStart("start2");
		scriptData.setPathStop("stop2");
		scriptData.setPathRestart("restart2");
		scriptData.setPathStatus("status2");
		UpdateServiceInstance command = new UpdateServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);
		command.setVersion(10000);

		String oldServiceName = "service1";
		String oldEnvironmentName = "environment1";
		String oldHostName = "host1";
		String oldInstanceName = "instance1";
		try {
			admin.updateServiceInstance(command, oldServiceName, oldEnvironmentName, oldHostName, oldInstanceName);
			fail();
		} catch (AlreadyModified e) {
		}

		command.setVersion(0);

		admin.updateServiceInstance(command, oldServiceName, oldEnvironmentName, oldHostName, oldInstanceName);

		ServiceInstanceResponse response = admin.getServiceInstance(serviceName,
																	environmentName,
																	hostName,
																	instanceName);
		assertEquals("environment1", response.getEnvironmentName());
		assertEquals("host1", response.getHostName());
		assertEquals("service1", response.getServiceName());
		assertEquals("instance1", response.getInstanceName());
		assertEquals("scriptName2", response.getScript().getScriptName());
		assertEquals("restart2", response.getScript().getPathRestart());

	}

	@Test
	public void updateServiceInstanceFieldsNullorEmptyTest() {
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
		String environmentName = "environment2";
		String hostName = null;
		String serviceName = "service2";
		String instanceName = "";

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName("scriptName2");
		scriptData.setPathStart("start2");
		scriptData.setPathStop("stop2");
		scriptData.setPathRestart("restart2");
		scriptData.setPathStatus("status2");
		UpdateServiceInstance command = new UpdateServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);
		command.setVersion(0);

		String oldServiceName = "service1";
		String oldEnvironmentName = "environment1";
		String oldHostName = "host1";
		String oldInstanceName = "instance1";

		try {
			admin.updateServiceInstance(command, oldServiceName, oldEnvironmentName, oldHostName, oldInstanceName);
			fail();
		} catch (EmptyField e) {
			// ok
		}

		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
	}

	@Test
	public void updateServiceInstanceScriptFieldsNullorEmptyTest() {
		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
		String environmentName = "environment2";
		String hostName = "asdf";
		String serviceName = "service2";
		String instanceName = "xxx";

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName("scriptName2");
		scriptData.setPathStart(null);
		scriptData.setPathStop("");
		scriptData.setPathRestart("restart2");
		scriptData.setPathStatus("status2");
		UpdateServiceInstance command = new UpdateServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);
		command.setVersion(0);

		String oldServiceName = "service1";
		String oldEnvironmentName = "environment1";
		String oldHostName = "host1";
		String oldInstanceName = "instance1";

		admin.updateServiceInstance(command, oldServiceName, oldEnvironmentName, oldHostName, oldInstanceName);

		assertEquals(1, admin.getServiceInstances("*", "*", "*", "*").getServiceInstances().size());
	}

	@Test
	public void implicitDeleteUnusedHostsTest() {
		String environmentName = "environment2";
		String hostName = "host1";
		String serviceName = "service2";
		String instanceName = "instance2";

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName("scriptName2");
		scriptData.setPathStart("start2");
		scriptData.setPathStop("stop2");
		scriptData.setPathRestart("restart2");
		scriptData.setPathStatus("status2");
		CreateNewServiceInstance command = new CreateNewServiceInstance();
		command.setEnvironmentName(environmentName);
		command.setHostName(hostName);
		command.setInstanceName(instanceName);
		command.setScript(scriptData);
		command.setServiceName(serviceName);
		assertEquals(1, hosts.getAllHosts().size());
		admin.createNewServiceInstance(command);
		assertEquals(1, hosts.getAllHosts().size());

		String environmentName3 = "environment3";
		String hostName3 = "host3";
		String serviceName3 = "service3";
		String instanceName3 = "instance3";

		ScriptData scriptData3 = new ScriptData();
		scriptData.setScriptName("scriptName3");
		scriptData.setPathStart("start3");
		scriptData.setPathStop("stop3");
		scriptData.setPathRestart("restart3");
		scriptData.setPathStatus("status3");
		CreateNewServiceInstance command3 = new CreateNewServiceInstance();
		command3.setEnvironmentName(environmentName3);
		command3.setHostName(hostName3);
		command3.setInstanceName(instanceName3);
		command3.setScript(scriptData3);
		command3.setServiceName(serviceName3);
		admin.createNewServiceInstance(command3);
		assertEquals(2, hosts.getAllHosts().size());
		RemoveServiceInstance commandDelete = new RemoveServiceInstance();
		commandDelete.setVersion(0);
		admin.removeServiceInstance(commandDelete,
									serviceName3,
									environmentName3,
									hostName3,
									instanceName3);
		assertEquals(1, hosts.getAllHosts().size());
		RemoveServiceInstance commandDelete2 = new RemoveServiceInstance();
		commandDelete2.setVersion(0);
		admin.removeServiceInstance(commandDelete2, serviceName, environmentName, hostName, instanceName);
		assertEquals(1, hosts.getAllHosts().size());

		String environmentName2 = "environment1";
		String hostName2 = "host1";
		String serviceName2 = "service1";
		String instanceName2 = "instance1";
		RemoveServiceInstance commandDelete3 = new RemoveServiceInstance();
		commandDelete3.setVersion(0);
		admin.removeServiceInstance(commandDelete3, serviceName2, environmentName2, hostName2, instanceName2);
		assertEquals(0, hosts.getAllHosts().size());
	}

	/**
	 * cleanup table entries
	 */
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

	}
}
