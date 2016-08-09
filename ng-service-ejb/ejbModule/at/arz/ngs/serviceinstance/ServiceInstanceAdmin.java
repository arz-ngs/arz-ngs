package at.arz.ngs.serviceinstance;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.Environment;
import at.arz.ngs.EnvironmentRepository;
import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.Script;
import at.arz.ngs.ScriptRepository;
import at.arz.ngs.Service;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.PathRestart;
import at.arz.ngs.api.PathStart;
import at.arz.ngs.api.PathStatus;
import at.arz.ngs.api.PathStop;
import at.arz.ngs.api.ScriptName;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.Status;
import at.arz.ngs.api.exception.EmptyField;
import at.arz.ngs.api.exception.EnvironmentNotFound;
import at.arz.ngs.api.exception.HostNotFound;
import at.arz.ngs.api.exception.ScriptNotFound;
import at.arz.ngs.api.exception.ServiceInstanceAlreadyExist;
import at.arz.ngs.api.exception.ServiceNotFound;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.create.CreateNewServiceInstance;

@Stateless
public class ServiceInstanceAdmin {

	@Inject
	private ServiceRepository services;

	@Inject
	private HostRepository hosts;

	@Inject
	private EnvironmentRepository environments;

	@Inject
	private ServiceInstanceRepository serviceInstances;

	@Inject
	private ScriptRepository scripts;

	public void createNewServiceInstance(CreateNewServiceInstance command) {
		String hostNameString = command.getHostName();
		String serviceNameString = command.getServiceName();
		String environmentNameString = command.getEnvironmentName();
		String serviceInstanceNameString = command.getInstanceName();
		ScriptData scriptData = command.getScript();

		HostName hostName = new HostName(hostNameString);
		ServiceName serviceName = new ServiceName(serviceNameString);
		EnvironmentName environmentName = new EnvironmentName(environmentNameString);
		ServiceInstanceName serviceInstanceName = new ServiceInstanceName(serviceInstanceNameString);

		String scriptNameString = scriptData.getScriptName();
		String pathStartString = scriptData.getPathStart();
		String pathStopString = scriptData.getPathStop();
		String pathRestartString = scriptData.getPathRestart();
		String pathStatusString = scriptData.getPathStatus();


			Host newHost = getOrCreateNewHost(hostName);
			Service newService = getOrCreateNewService(serviceName);
			Environment newEnvironment = getOrCreateNewEnvironment(environmentName);
			Script newScript = getOrCreateNewScript(scriptNameString,
													pathStartString,
													pathStopString,
													pathRestartString,
													pathStatusString);
			createNewServiceInstance(serviceInstanceName, newService, newHost, newEnvironment, newScript);

	}

	private Host getOrCreateNewHost(HostName hostName) {
		if (hostName == null || hostName.toString().equals("")) {
			throw new EmptyField("HostName");
		}
		try {
			return hosts.getHost(hostName);
		} catch (HostNotFound eHost) {
			hosts.addHost(hostName);
			return hosts.getHost(hostName);
		}
	}

	private Service getOrCreateNewService(ServiceName serviceName) {
		if (serviceName == null || serviceName.toString().equals("")) {
			throw new EmptyField("ServiceName");
		}
		try {
			return services.getService(serviceName);
		} catch (ServiceNotFound eService) {
			services.addService(serviceName);
			return services.getService(serviceName);
		}
	}

	private Environment getOrCreateNewEnvironment(EnvironmentName environmentName) {
		if (environmentName == null || environmentName.toString().equals("")) {
			throw new EmptyField("EnvironmentName");
		}
		try {
			return environments.getEnvironment(environmentName);
		} catch (EnvironmentNotFound eEnvironment) {
			environments.addEnvironment(environmentName);
			return environments.getEnvironment(environmentName);
		}
	}

	private Script getOrCreateNewScript(String scriptNameString,
										String pathStartString,
										String pathStopString,
										String pathRestartString,
										String pathStatusString) {
		if (scriptNameString == null || scriptNameString.equals("")) {
			throw new EmptyField("Skriptname");
		}
		ScriptName scriptName = new ScriptName(scriptNameString);
		PathStart pathStart = new PathStart(pathStartString);
		PathStop pathStop = new PathStop(pathStopString);
		PathRestart pathRestart = new PathRestart(pathRestartString);
		PathStatus pathStatus = new PathStatus(pathStatusString);
		try {
			return scripts.getScript(scriptName);
		} catch (ScriptNotFound eScript) {
			scripts.addScript(scriptName, pathStart, pathStop, pathRestart, pathStatus);
			return scripts.getScript(scriptName);
		}
		
	}

	private void createNewServiceInstance(	ServiceInstanceName serviceInstanceName,
											Service service,
											Host
	host, Environment environment, Script script) {
		if (serviceInstanceName == null || serviceInstanceName.toString().equals("")) {
			throw new EmptyField("ServiceInstanceName");
		}
		if (serviceInstances.getServiceInstance(serviceInstanceName, service, host, environment) != null) {
			throw new ServiceInstanceAlreadyExist(service.getServiceName().toString()+ "/"
													+ host.getHostName().toString()
													+ "/"
													+ environment.getEnvironmentName().toString()
													+ "/"
													+ serviceInstanceName);
		}
		serviceInstances.addServiceInstance(host, service, environment, script, serviceInstanceName, Status.not_active);
	}


}
