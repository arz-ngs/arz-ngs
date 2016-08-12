package at.arz.ngs.serviceinstance;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.Environment;
import at.arz.ngs.EnvironmentRepository;
import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.Script;
import at.arz.ngs.ScriptRepository;
import at.arz.ngs.Service;
import at.arz.ngs.ServiceInstance;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.Path;
import at.arz.ngs.api.PathRestart;
import at.arz.ngs.api.PathStart;
import at.arz.ngs.api.PathStatus;
import at.arz.ngs.api.PathStop;
import at.arz.ngs.api.ScriptName;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.Status;
import at.arz.ngs.api.exception.ActionInProgress;
import at.arz.ngs.api.exception.AlreadyModified;
import at.arz.ngs.api.exception.EmptyField;
import at.arz.ngs.api.exception.EnvironmentNotFound;
import at.arz.ngs.api.exception.HostNotFound;
import at.arz.ngs.api.exception.ScriptNotFound;
import at.arz.ngs.api.exception.ServiceInstanceAlreadyExist;
import at.arz.ngs.api.exception.ServiceInstanceNotFound;
import at.arz.ngs.api.exception.ServiceNotFound;
import at.arz.ngs.api.exception.WrongParam;
import at.arz.ngs.search.SearchEngine;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.action.PerformAction;
import at.arz.ngs.serviceinstance.commands.create.CreateNewServiceInstance;
import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverview;
import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverviewList;
import at.arz.ngs.serviceinstance.commands.get.ServiceInstanceResponse;
import at.arz.ngs.serviceinstance.commands.remove.RemoveServiceInstance;
import at.arz.ngs.serviceinstance.commands.update.UpdateServiceInstance;

/**
 * Manages the logic and the methods of the repositores methods
 * 
 * @author rpci334
 *
 */
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

	@Inject
	private SearchEngine engine;

	/**
	 * Only for JUnit Tests
	 * 
	 * @param services
	 * @param hosts
	 * @param environments
	 * @param serviceInstances
	 * @param scripts
	 * @param engine
	 */
	public ServiceInstanceAdmin(ServiceRepository services,
								HostRepository hosts,
								EnvironmentRepository environments,
								ServiceInstanceRepository serviceInstances,
								ScriptRepository scripts,
								SearchEngine engine) {
		super();
		this.services = services;
		this.hosts = hosts;
		this.environments = environments;
		this.serviceInstances = serviceInstances;
		this.scripts = scripts;
		this.engine = engine;
	}

	protected ServiceInstanceAdmin() {
		// EJB Constructor
	}

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

		Service newService = getOrCreateNewService(serviceName);
		Environment newEnvironment = getOrCreateNewEnvironment(environmentName);
		Host newHost = getOrCreateNewHost(hostName);
		if (scriptNameString == null || scriptNameString.equals("")) {
			scriptNameString = new ScriptName(environmentName, serviceInstanceName, hostName, serviceName).getName();
		}
		Script newScript = getOrCreateNewScript(scriptNameString,
												pathStartString,
												pathStopString,
												pathRestartString,
												pathStatusString);
		createNewServiceInstance(serviceInstanceName, newService, newHost, newEnvironment, newScript);
	}

	private Host getOrCreateNewHost(HostName hostName) { // TODO exchange for multithreading: start with adding host,
															// then getHost if AlreadyExists!!!!
		if (hostName == null || hostName.getName() == null || hostName.getName().equals("")) {
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
		if (serviceName == null || serviceName.getName() == null || serviceName.getName().equals("")) {
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
		if (environmentName == null || environmentName.getName() == null || environmentName.getName().equals("")) {
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
											Host host,
											Environment environment,
											Script script) {
		if (serviceInstanceName == null|| serviceInstanceName.getName() == null
			|| serviceInstanceName.getName().equals("")) {
			throw new EmptyField("ServiceInstanceName");
		}

		try {
			serviceInstances.getServiceInstance(serviceInstanceName, service, host, environment);
			throw new ServiceInstanceAlreadyExist(service.getServiceName().toString()+ "/"
													+ host.getHostName().toString()
													+ "/"
													+ environment.getEnvironmentName().toString()
													+ "/"
													+ serviceInstanceName);
		} catch (ServiceInstanceNotFound e) {
			// wanted
		}
		serviceInstances.addServiceInstance(host, service, environment, script, serviceInstanceName, Status.not_active);
	}

	/**
	 * Removes unused scripts, services, environments, hosts.
	 */
	private void removeAllUnusedElements() {
		scripts.removeUnusedScripts();
		services.removeUnusedServices();
		environments.removeUnusedEnvironments();
		hosts.removeUnusedHosts();
	}

	public void updateServiceInstance(	UpdateServiceInstance command,
										String oldServiceNameString,
										String oldEnvironmentNameString,
										String oldHostNameString,
										String oldServiceInstanceNameString) {
		ServiceName oldServiceName = new ServiceName(oldServiceNameString);
		EnvironmentName oldEnvironmentName = new EnvironmentName(oldEnvironmentNameString);
		HostName oldHostName = new HostName(oldHostNameString);
		ServiceInstanceName oldServiceInstanceName = new ServiceInstanceName(oldServiceInstanceNameString);

		String hostNameString = command.getHostName();
		String serviceNameString = command.getServiceName();
		String environmentNameString = command.getEnvironmentName();
		String serviceInstanceNameString = command.getInstanceName();
		ScriptData scriptData = command.getScript();
		long version = command.getVersion();

		HostName hostName = new HostName(hostNameString);
		ServiceName serviceName = new ServiceName(serviceNameString);
		EnvironmentName environmentName = new EnvironmentName(environmentNameString);
		ServiceInstanceName serviceInstanceName = new ServiceInstanceName(serviceInstanceNameString);

		String scriptNameString = scriptData.getScriptName();
		String pathStartString = scriptData.getPathStart();
		String pathStopString = scriptData.getPathStop();
		String pathRestartString = scriptData.getPathRestart();
		String pathStatusString = scriptData.getPathStatus();

		Service newService = getOrCreateNewService(serviceName);
		Environment newEnvironment = getOrCreateNewEnvironment(environmentName);
		Host newHost = getOrCreateNewHost(hostName);
		if (scriptNameString == null || scriptNameString.equals("")) {
			scriptNameString = new ScriptName(environmentName, serviceInstanceName, hostName, serviceName).getName();
		}
		Script newScript = getOrCreateNewScript(scriptNameString,
												pathStartString,
												pathStopString,
												pathRestartString,
												pathStatusString);
		updateServiceInstance(	oldServiceName,
								oldEnvironmentName,
								oldHostName,
								oldServiceInstanceName,
								newHost,
								newService,
								newEnvironment,
								newScript,
								serviceInstanceName,
								version);

		removeAllUnusedElements();
	}

	private void updateServiceInstance(	ServiceName oldServiceName,
										EnvironmentName oldEnvironmentName,
										HostName oldHostName,
										ServiceInstanceName oldServiceInstanceName,
										Host newHost,
										Service newService,
										Environment newEnvironment,
										Script newScript,
										ServiceInstanceName serviceInstanceName,
										long version) {
		Service oldService = services.getService(oldServiceName);
		Environment oldEnvironment = environments.getEnvironment(oldEnvironmentName); // OldEnvironment should already
		// exist
		Host oldHost = hosts.getHost(oldHostName);
		ServiceInstance oldServiceInstance = serviceInstances.getServiceInstance(	oldServiceInstanceName,
																					oldService,
																					oldHost,
																					oldEnvironment);
		if (oldServiceInstance != null) {
			if (version == oldServiceInstance.getVersion()) {
				oldServiceInstance.setEnvironment(newEnvironment);
				oldServiceInstance.setHost(newHost);
				oldServiceInstance.setScript(newScript);
				oldServiceInstance.setService(newService);
				oldServiceInstance.renameServiceInstance(serviceInstanceName);
				oldServiceInstance.incrementVersion();
				// oldServiceInstance.setStatus(Status.not_active); //current status should not be overwritten
			} else {
				throw new AlreadyModified(oldServiceInstance.toString());
			}

		} else {
			throw new ServiceInstanceNotFound(oldServiceInstanceName, oldServiceName, oldHostName, oldEnvironmentName);
		}
	}

	public void removeServiceInstance(	RemoveServiceInstance command,
										String serviceNameString,
										String environmentNameString,
										String hostNameString,
										String serviceInstanceNameString) {
		if (command == null) {
			throw new EmptyField("The version must be set to remove a Service Instance.");
		}
		long version = command.getVersion();

		ServiceName serviceName = new ServiceName(serviceNameString);
		EnvironmentName environmentName = new EnvironmentName(environmentNameString);
		HostName hostName = new HostName(hostNameString);
		ServiceInstanceName serviceInstanceName = new ServiceInstanceName(serviceInstanceNameString);

		Service service = services.getService(serviceName);
		Environment environment = environments.getEnvironment(environmentName);
		Host host = hosts.getHost(hostName);
		ServiceInstance serviceInstance = serviceInstances.getServiceInstance(	serviceInstanceName,
																				service,
																				host,
																				environment);
		if (serviceInstance != null) {
			if (serviceInstance.getVersion() == version) {
				serviceInstances.removeServiceInstance(serviceInstance);
			} else {
				throw new AlreadyModified(serviceInstance.toString());
			}
		} else {
			throw new ServiceInstanceNotFound(serviceInstanceName, serviceName, hostName, environmentName);
		}

		removeAllUnusedElements();
	}

	public ServiceInstanceResponse getServiceInstance(	String serviceNameString,
														String environmentNameString,
														String hostNameString,
														String serviceInstanceNameString) {

		HostName hostName = new HostName(hostNameString);
		ServiceName serviceName = new ServiceName(serviceNameString);
		EnvironmentName environmentName = new EnvironmentName(environmentNameString);
		ServiceInstanceName serviceInstanceName = new ServiceInstanceName(serviceInstanceNameString);

		Service service = services.getService(serviceName);
		Environment environment = environments.getEnvironment(environmentName);
		Host host = hosts.getHost(hostName);
		ServiceInstance serviceInstance = serviceInstances.getServiceInstance(	serviceInstanceName,
																				service,
																				host,
																				environment);
		if (serviceInstance != null) {
			ServiceInstanceResponse response = new ServiceInstanceResponse();
			response.setEnvironmentName(serviceInstance.getEnvironment().getEnvironmentName().toString());
			response.setServiceName(serviceInstance.getService().getServiceName().toString());
			response.setHostName(serviceInstance.getHost().getHostName().toString());
			response.setInstanceName(serviceInstance.getServiceInstanceName().toString());
			response.setStatus(serviceInstance.getStatus());
			response.setVersion(serviceInstance.getVersion());

			Script script = serviceInstance.getScript();
			if (script != null) {
				String scriptName = script.getScriptName().getName();
				String pathStart = getPath(script.getPathStart());
				String pathStop = getPath(script.getPathStop());
				String pathRestart = getPath(script.getPathRestart());
				String pathStatus = getPath(script.getPathStatus());
				ScriptData scriptData = new ScriptData();
				scriptData.setScriptName(scriptName);
				scriptData.setPathStart(pathStart);
				scriptData.setPathStop(pathStop);
				scriptData.setPathRestart(pathRestart);
				scriptData.setPathStatus(pathStatus);
				response.setScript(scriptData);
			}

			return response;
		} else {
			throw new ServiceInstanceNotFound(serviceInstanceName, serviceName, hostName, environmentName);
		}
	}

	private String getPath(Path path) {
		if (path == null) {
			return null;
		}

		return path.getPath();
	}

	public ServiceInstanceOverviewList getServiceInstances(	String serviceNameString,
															String environmentNameString,
															String hostNameString,
															String serviceInstanceNameString) {
		List<ServiceInstance> serviceInstances = engine.findServiceInstances(	serviceNameString,
																				environmentNameString,
																				hostNameString,
																				serviceInstanceNameString);
		ServiceInstanceOverviewList ovList = new ServiceInstanceOverviewList();
		List<ServiceInstanceOverview> list = new ArrayList<ServiceInstanceOverview>();
		for (ServiceInstance instance : serviceInstances) {
			ServiceInstanceOverview ov = new ServiceInstanceOverview();
			ov.setEnvironmentName(instance.getEnvironment().getEnvironmentName().toString());
			ov.setHostName(instance.getHost().getHostName().toString());
			ov.setInstanceName(instance.getServiceInstanceName().toString());
			ov.setServiceName(instance.getService().getServiceName().toString());
			ov.setStatus(instance.getStatus().toString());
			list.add(ov);
		}
		ovList.setServiceInstances(list);
		return ovList;
	}

	public void performAction(	String serviceNameString,
								String environmentNameString,
								String hostNameString,
								String serviceInstanceNameString,
								PerformAction perform) {
		HostName hostName = new HostName(hostNameString);
		ServiceName serviceName = new ServiceName(serviceNameString);
		EnvironmentName environmentName = new EnvironmentName(environmentNameString);
		ServiceInstanceName serviceInstanceName = new ServiceInstanceName(serviceInstanceNameString);

		Service service = services.getService(serviceName);
		Host host = hosts.getHost(hostName);
		Environment environment = environments.getEnvironment(environmentName);
		ServiceInstance serviceInstance = serviceInstances.getServiceInstance(	serviceInstanceName,
																				service,
																				host,
																				environment);
		String status = serviceInstance.getStatus().name();
		Script script = serviceInstance.getScript();
		String param = perform.getPerformAction().toLowerCase();
		String path;
		if (param.equals("start") || param.equals("stop") || param.equals("restart") || param.equals("status")) {
			if (!status.equals("is_starting") && !status.equals("is_stopping")) {
				if (perform.getPerformAction().toLowerCase().equals("start")) {
					if (status.equals("active") || status.equals("not_active") || status.equals("failed")) {
						path = script.getPathStart().toString();
						serviceInstance.setStatus(Status.is_starting);
					}
				} else if (status.equals("active") || status.equals("not_active") || status.equals("failed")) {
					path = script.getPathStop().toString();
					serviceInstance.setStatus(Status.is_stopping);
				} else if (perform.getPerformAction().toLowerCase().equals("restart")) {
					path = script.getPathRestart().toString();
					serviceInstance.setStatus(Status.is_stopping);
				}
				if (perform.getPerformAction().toLowerCase().equals("status")) {
					path = script.getPathStatus().toString();
				}
			} else {
				throw new ActionInProgress(serviceInstance.toString()+ " "
											+ status
											+ " Cannot perform action while another action is in progress.");
			}
		} else {
			throw new WrongParam(perform.getPerformAction()
									+ " -- Use only this action commands: start, stop, restart, status");
		}
		// TODO execute method on server with the path as parameter
		// execute(path)
	}

}
