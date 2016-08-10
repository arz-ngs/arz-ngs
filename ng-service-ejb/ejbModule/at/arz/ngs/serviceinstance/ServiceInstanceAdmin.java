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
import at.arz.ngs.ServiceInstance;
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
import at.arz.ngs.api.exception.AlreadyModified;
import at.arz.ngs.api.exception.AlreadyPerform;
import at.arz.ngs.api.exception.EmptyField;
import at.arz.ngs.api.exception.EnvironmentNotFound;
import at.arz.ngs.api.exception.HostNotFound;
import at.arz.ngs.api.exception.ScriptNotFound;
import at.arz.ngs.api.exception.ServiceInstanceAlreadyExist;
import at.arz.ngs.api.exception.ServiceInstanceNotFound;
import at.arz.ngs.api.exception.ServiceNotFound;
import at.arz.ngs.api.exception.WrongParam;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.action.PerformAction;
import at.arz.ngs.serviceinstance.commands.create.CreateNewServiceInstance;
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
											Host host,
											Environment environment,
											Script script) {
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

		Host newHost = getOrCreateNewHost(hostName);
		Service newService = getOrCreateNewService(serviceName);
		Environment newEnvironment = getOrCreateNewEnvironment(environmentName);
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
		Environment oldEnvironment = environments.getEnvironment(oldEnvironmentName); // OldEnvironment should already
		// exist
		Host oldHost = hosts.getHost(oldHostName);
		Service oldService = services.getService(oldServiceName);
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
				oldServiceInstance.incrementVersion();
				oldServiceInstance.setStatus(Status.not_active);
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
		long version = command.getVersion();

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
		if (serviceInstance != null) {
			if (serviceInstance.getVersion() == version) {
				serviceInstances.removeServiceInstance(serviceInstance);
			} else {
				throw new AlreadyModified(serviceInstance.toString());
			}
		} else {
			throw new ServiceInstanceNotFound(serviceInstanceName, serviceName, hostName, environmentName);
		}
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
		Host host = hosts.getHost(hostName);
		Environment environment = environments.getEnvironment(environmentName);
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
			String scriptName = script.getScriptName().toString();
			String pathStart = script.getPathStart().toString();
			String pathStop = script.getPathStop().toString();
			String pathRestart = script.getPathRestart().toString();
			String pathStatus = script.getPathStatus().toString();
			ScriptData scriptData = new ScriptData();
			scriptData.setScriptName(scriptName);
			scriptData.setPathStart(pathStart);
			scriptData.setPathStop(pathStop);
			scriptData.setPathRestart(pathRestart);
			scriptData.setPathStatus(pathStatus);
			response.setScript(scriptData);

			return response;
		} else {
			throw new ServiceInstanceNotFound(serviceInstanceName, serviceName, hostName, environmentName);
		}
	}

	public void performAction(	PerformAction perform,
	                          	String serviceInstanceNameString,
	                          	String serviceNameString,
	                          	String environmentNameString,
	                          	String hostNameString) {
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
					path = script.getPathStart().toString();
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
				throw new AlreadyPerform(serviceInstance.toString()+ " "
											+ status
											+ " Sie können derzeit keine andere Aktion ausführen!");
			}
		} else {
			throw new WrongParam("Falscher Parameter: "+ perform.getPerformAction()
									+ "\n Es können folgende Parameter gesetzt werden: start, stop, restart, status");
		}
		// TODO execute method on server with the path as parameter
		// execute(path)
	}


}
