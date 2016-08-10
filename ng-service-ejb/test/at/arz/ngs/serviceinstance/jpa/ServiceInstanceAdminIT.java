package at.arz.ngs.serviceinstance.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.api.exception.AlreadyModified;
import at.arz.ngs.api.exception.ServiceInstanceNotFound;
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

	@Before
	public void setUpBefore() throws Exception {
		admin = new ServiceInstanceAdmin(	new JPAServiceRepository(getEntityManager()),
											new JPAHostRepository(getEntityManager()),
											new JPAEnvironmentRepository(getEntityManager()),
											new JPAServiceInstanceRepository(getEntityManager()),
											new JPAScriptRepository(getEntityManager()),
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
	public void addServiceInstance() {
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
	}

	public void deleteServiceInstance() {
		String environmentName = "environment1";
		String hostName = "host1";
		String serviceName = "service1";
		String instanceName = "instance1";
		RemoveServiceInstance command = new RemoveServiceInstance();
		command.setVersion(0);
		admin.removeServiceInstance(command, serviceName, environmentName, hostName, instanceName);

		ServiceInstanceResponse response = admin.getServiceInstance(serviceName,
																	environmentName,
																	hostName,
																	instanceName);
		assertNull(response.getEnvironmentName());
		assertNull(response.getHostName());
		assertNull(response.getServiceName());
		assertNull(response.getInstanceName());
	}

	public void updateServiceInstance() {
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

		admin.updateServiceInstance(command,
									oldServiceName,
									oldEnvironmentName,
									oldHostName,
									oldInstanceName);

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
	}

	public void updateServiceInstance2() {
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
		assertEquals("restart1", response.getScript().getPathRestart());
	}

	public void updateServiceInstance3() {

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
		command.setVersion(1);

		String oldServiceName = "service1";
		String oldEnvironmentName = "environment1";
		String oldHostName = "host1";
		String oldInstanceName = "instance1";
		try {
		admin.updateServiceInstance(command, oldServiceName, oldEnvironmentName, oldHostName, oldInstanceName);
		} catch (AlreadyModified e) {

		}
		try {
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
		} catch (ServiceInstanceNotFound e) {

		}
	}

	public void updateServiceInstance4() {

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
		command.setVersion(1);

		String oldServiceName = "service1";
		String oldEnvironmentName = "environment1";
		String oldHostName = "host1";
		String oldInstanceName = "instance1";
		try {
			admin.updateServiceInstance(command, oldServiceName, oldEnvironmentName, oldHostName, oldInstanceName);
		} catch (AlreadyModified e) {

		}
		try {
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
		} catch (ServiceInstanceNotFound e) {

		}
	}



}
