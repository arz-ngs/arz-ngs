package at.arz.ngs.serviceinstance;

import java.util.ArrayList;
import java.util.LinkedList;
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
import at.arz.ngs.api.Action;
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
import at.arz.ngs.script.ScriptExecutor;
import at.arz.ngs.search.OrderCondition;
import at.arz.ngs.search.PaginationCondition;
import at.arz.ngs.search.SearchEngine;
import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.commands.Actor;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.action.PerformAction;
import at.arz.ngs.serviceinstance.commands.create.CreateNewServiceInstance;
import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverview;
import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverviewList;
import at.arz.ngs.serviceinstance.commands.get.ServiceInstanceResponse;
import at.arz.ngs.serviceinstance.commands.update.UpdateServiceInstance;

/**
 * Manages the logic and the methods of the repositores methods
 * 
 * @author dani
 *
 */
@Stateless
public class ServiceInstanceAdmin {

	@Inject
	private ServiceRepository serviceRepository;

	@Inject
	private HostRepository hostRepository;

	@Inject
	private EnvironmentRepository environmentRepository;

	@Inject
	private ServiceInstanceRepository serviceInstanceRepository;

	@Inject
	private ScriptRepository scriptRepository;

	@Inject
	private SearchEngine searchEngine;

	@Inject
	private SecurityAdmin securityAdmin;

	@Inject
	private ScriptExecutor scriptExecutor;

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
	public ServiceInstanceAdmin(ServiceRepository services, HostRepository hosts, EnvironmentRepository environments,
			ServiceInstanceRepository serviceInstances, ScriptRepository scripts, SearchEngine engine,
			SecurityAdmin securityAdmin) {
		this.serviceRepository = services;
		this.hostRepository = hosts;
		this.environmentRepository = environments;
		this.serviceInstanceRepository = serviceInstances;
		this.scriptRepository = scripts;
		this.searchEngine = engine;
		this.securityAdmin = securityAdmin;
	}

	protected ServiceInstanceAdmin() {
		// EJB Constructor
	}

	public void createNewServiceInstance(Actor actor, CreateNewServiceInstance command) {
		securityAdmin.proofActorAdminAccess(actor);
		String hostNameString = command.getHostName();
		String serviceNameString = command.getServiceName();
		String environmentNameString = command.getEnvironmentName();
		String serviceInstanceNameString = command.getInstanceName();
		ScriptData scriptData = command.getScript();
		String information = command.getInformation();

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
		Script newScript = getOrCreateNewScript(scriptNameString, pathStartString, pathStopString, pathRestartString,
				pathStatusString);
		createNewServiceInstance(serviceInstanceName, newService, newHost, newEnvironment, newScript, information);
	}

	private Host getOrCreateNewHost(HostName hostName) {
		// then getHost if AlreadyExists!!!!
		if (hostName == null || hostName.getName() == null || hostName.getName().equals("")) {
			throw new EmptyField("HostName");
		}
		try {
			return hostRepository.getHost(hostName);
		}
		catch (HostNotFound eHost) {
			hostRepository.addHost(hostName);
			return hostRepository.getHost(hostName);
		}
	}

	private Service getOrCreateNewService(ServiceName serviceName) {
		if (serviceName == null || serviceName.getName() == null || serviceName.getName().equals("")) {
			throw new EmptyField("ServiceName");
		}
		try {
			return serviceRepository.getService(serviceName);
		}
		catch (ServiceNotFound eService) {
			serviceRepository.addService(serviceName);
			return serviceRepository.getService(serviceName);
		}
	}

	private Environment getOrCreateNewEnvironment(EnvironmentName environmentName) {
		if (environmentName == null || environmentName.getName() == null || environmentName.getName().equals("")) {
			throw new EmptyField("EnvironmentName");
		}
		try {
			return environmentRepository.getEnvironment(environmentName);
		}
		catch (EnvironmentNotFound eEnvironment) {
			environmentRepository.addEnvironment(environmentName);
			return environmentRepository.getEnvironment(environmentName);
		}
	}

	private Script getOrCreateNewScript(String scriptNameString, String pathStartString, String pathStopString,
			String pathRestartString, String pathStatusString) {
		if (scriptNameString == null || scriptNameString.equals("")) {
			throw new EmptyField("Skriptname");
		}
		ScriptName scriptName = new ScriptName(scriptNameString);
		PathStart pathStart = new PathStart(pathStartString);
		PathStop pathStop = new PathStop(pathStopString);
		PathRestart pathRestart = new PathRestart(pathRestartString);
		PathStatus pathStatus = new PathStatus(pathStatusString);
		Script sc;
		try {
			sc = scriptRepository.getScript(scriptName);
		}
		catch (ScriptNotFound eScript) {
			scriptRepository.addScript(scriptName, pathStart, pathStop, pathRestart, pathStatus);
			sc = scriptRepository.getScript(scriptName);
		}
		sc.setPathStart(pathStart);
		sc.setPathStop(pathStop);
		sc.setPathRestart(pathRestart);
		sc.setPathStatus(pathStatus);
		return sc;
	}

	private void createNewServiceInstance(ServiceInstanceName serviceInstanceName, Service service, Host host,
			Environment environment, Script script, String information) {
		if (serviceInstanceName == null || serviceInstanceName.getName() == null
				|| serviceInstanceName.getName().equals("")) {
			throw new EmptyField("ServiceInstanceName");
		}

		if (information == null) {
			information = "";
		}

		try {
			serviceInstanceRepository.getServiceInstance(serviceInstanceName, service, host, environment);
			throw new ServiceInstanceAlreadyExist(
					service.getServiceName().toString() + "/" + host.getHostName().toString() + "/"
							+ environment.getEnvironmentName().toString() + "/" + serviceInstanceName);
		}
		catch (ServiceInstanceNotFound e) {
			// wanted
		}
		serviceInstanceRepository.addServiceInstance(host, service, environment, script, serviceInstanceName,
				Status.not_active, information);
	}

	/**
	 * Removes unused scripts, services, environments, hosts.
	 */
	private void removeAllUnusedElements() {
		scriptRepository.removeUnusedScripts();
		serviceRepository.removeUnusedServices();
		environmentRepository.removeUnusedEnvironments();
		hostRepository.removeUnusedHosts();
	}

	public void updateServiceInstance(Actor actor, UpdateServiceInstance command, String oldServiceNameString,
			String oldEnvironmentNameString, String oldHostNameString, String oldServiceInstanceNameString) {
		securityAdmin.proofActorAdminAccess(actor);
		ServiceName oldServiceName = new ServiceName(oldServiceNameString);
		EnvironmentName oldEnvironmentName = new EnvironmentName(oldEnvironmentNameString);
		HostName oldHostName = new HostName(oldHostNameString);
		ServiceInstanceName oldServiceInstanceName = new ServiceInstanceName(oldServiceInstanceNameString);

		String hostNameString = command.getHostName();
		String serviceNameString = command.getServiceName();
		String environmentNameString = command.getEnvironmentName();
		String serviceInstanceNameString = command.getInstanceName();
		ScriptData scriptData = command.getScript();
		String information = command.getInformation();
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
		if (information == null) {
			information = "";
		}
		Script newScript = getOrCreateNewScript(scriptNameString, pathStartString, pathStopString, pathRestartString,
				pathStatusString);
		updateServiceInstance(oldServiceName, oldEnvironmentName, oldHostName, oldServiceInstanceName, newHost,
				newService, newEnvironment, newScript, serviceInstanceName, version, information);

		removeAllUnusedElements();
	}

	private void updateServiceInstance(ServiceName oldServiceName, EnvironmentName oldEnvironmentName,
			HostName oldHostName, ServiceInstanceName oldServiceInstanceName, Host newHost, Service newService,
			Environment newEnvironment, Script newScript, ServiceInstanceName serviceInstanceName, long version,
			String information) {
		Service oldService = serviceRepository.getService(oldServiceName);
		Environment oldEnvironment = environmentRepository.getEnvironment(oldEnvironmentName); // OldEnvironment should already
		// exist
		Host oldHost = hostRepository.getHost(oldHostName);
		ServiceInstance oldServiceInstance = serviceInstanceRepository.getServiceInstance(oldServiceInstanceName,
				oldService, oldHost, oldEnvironment);
		if (!(oldServiceInstanceName.equals(serviceInstanceName)
				&& oldEnvironmentName.equals(newEnvironment.getEnvironmentName())
				&& oldServiceName.equals(newService.getServiceName()) && oldHostName.equals(newHost.getHostName()))) {

			try {
				serviceInstanceRepository.getServiceInstance(serviceInstanceName, newService, newHost, newEnvironment);
				throw new ServiceInstanceAlreadyExist(
						newService + "/" + newEnvironment + "/" + newHost + "/" + serviceInstanceName);
			}
			catch (ServiceInstanceNotFound e) {
				// wanted here
			}
		}
		if (oldServiceInstance != null) {
			if (version == oldServiceInstance.getVersion()) {
				oldServiceInstance.setEnvironment(newEnvironment);
				oldServiceInstance.setHost(newHost);
				oldServiceInstance.setScript(newScript);
				oldServiceInstance.setService(newService);
				oldServiceInstance.renameServiceInstance(serviceInstanceName);
				oldServiceInstance.setInformation(information);
				oldServiceInstance.incrementVersion();
				// oldServiceInstance.setStatus(Status.not_active); //current status should not be overwritten
			}
			else {
				throw new AlreadyModified(oldServiceInstance.toString());
			}

		}
		else {
			throw new ServiceInstanceNotFound(oldServiceInstanceName, oldServiceName, oldHostName, oldEnvironmentName);
		}
	}

	public void removeServiceInstance(Actor actor, String serviceNameString, String environmentNameString,
			String hostNameString, String serviceInstanceNameString) {
		securityAdmin.proofActorAdminAccess(actor);
		ServiceName serviceName = new ServiceName(serviceNameString);
		EnvironmentName environmentName = new EnvironmentName(environmentNameString);
		HostName hostName = new HostName(hostNameString);
		ServiceInstanceName serviceInstanceName = new ServiceInstanceName(serviceInstanceNameString);

		Service service = serviceRepository.getService(serviceName);
		Environment environment = environmentRepository.getEnvironment(environmentName);
		Host host = hostRepository.getHost(hostName);
		ServiceInstance serviceInstance = serviceInstanceRepository.getServiceInstance(serviceInstanceName, service,
				host, environment);
		serviceInstanceRepository.removeServiceInstance(serviceInstance);

		removeAllUnusedElements();

	}

	public ServiceInstanceResponse getServiceInstance(String serviceNameString, String environmentNameString,
			String hostNameString, String serviceInstanceNameString) {

		HostName hostName = new HostName(hostNameString);
		ServiceName serviceName = new ServiceName(serviceNameString);
		EnvironmentName environmentName = new EnvironmentName(environmentNameString);
		ServiceInstanceName serviceInstanceName = new ServiceInstanceName(serviceInstanceNameString);

		Service service = serviceRepository.getService(serviceName);
		Environment environment = environmentRepository.getEnvironment(environmentName);
		Host host = hostRepository.getHost(hostName);
		ServiceInstance serviceInstance = serviceInstanceRepository.getServiceInstance(serviceInstanceName, service,
				host, environment);
		if (serviceInstance != null) {
			ServiceInstanceResponse response = new ServiceInstanceResponse();
			response.setEnvironmentName(serviceInstance.getEnvironment().getEnvironmentName().toString());
			response.setServiceName(serviceInstance.getService().getServiceName().toString());
			response.setHostName(serviceInstance.getHost().getHostName().toString());
			response.setInstanceName(serviceInstance.getServiceInstanceName().toString());
			response.setStatus(serviceInstance.getStatus());
			response.setVersion(serviceInstance.getVersion());
			response.setInformation(serviceInstance.getInformation());

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
		}
		else {
			throw new ServiceInstanceNotFound(serviceInstanceName, serviceName, hostName, environmentName);
		}
	}

	private String getPath(Path path) {
		if (path == null) {
			return null;
		}

		return path.getPath();
	}

	public ServiceInstanceOverviewList getServiceInstances(String serviceNameString, String environmentNameString,
			String hostNameString, String serviceInstanceNameString) {
		List<ServiceInstance> serviceInstances = searchEngine.findServiceInstances(serviceNameString,
				environmentNameString, hostNameString, serviceInstanceNameString);
		return convert(serviceInstances, serviceInstances.size());
	}

	public ServiceInstanceOverviewList getServiceInstances(String serviceNameString, String environmentNameString,
			String hostNameString, String serviceInstanceNameString, OrderCondition order) {
		List<ServiceInstance> serviceInstances = searchEngine.findServiceInstances(serviceNameString,
				environmentNameString, hostNameString, serviceInstanceNameString, order);
		return convert(serviceInstances, serviceInstances.size());
	}

	public ServiceInstanceOverviewList getServiceInstances(String serviceNameString, String environmentNameString,
			String hostNameString, String serviceInstanceNameString, OrderCondition order,
			PaginationCondition pagination) {
		List<ServiceInstance> serviceInstances = searchEngine.findServiceInstances(serviceNameString,
				environmentNameString, hostNameString, serviceInstanceNameString, order, pagination);
		return convert(serviceInstances, getNumElementsFound(serviceNameString, environmentNameString, hostNameString,
				serviceInstanceNameString));
	}

	/**
	 * Returns the number of elements which are available in the current regex
	 * selection. Differs to the size of the
	 * {@link ServiceInstanceOverviewList.serviceInstances} only if pagination
	 * is enabled. If pagination is enabled the value is NOT the count of the
	 * current selection of the page, but the size of the regex selection, thus
	 * the size of the regex selection is greater.
	 */
	private int getNumElementsFound(String serviceNameString, String environmentNameString, String hostNameString,
			String serviceInstanceNameString) {
		return searchEngine.getNumElementsFound(serviceNameString, environmentNameString, hostNameString,
				serviceInstanceNameString);
	}

	private ServiceInstanceOverviewList convert(List<ServiceInstance> serviceInstances, int numElementsFound) {
		ServiceInstanceOverviewList ovList = new ServiceInstanceOverviewList();
		List<ServiceInstanceOverview> list = new ArrayList<>();
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
		ovList.setNumElementsFound(numElementsFound);

		return ovList;
	}

	public void performAction(Actor actor, String serviceNameString, String environmentNameString,
			String hostNameString, String serviceInstanceNameString, PerformAction perform) {
		securityAdmin.proofPerformAction(new EnvironmentName(environmentNameString), new ServiceName(serviceNameString),
				Action.valueOf(perform.getPerformAction()), actor);

		HostName hostName = new HostName(hostNameString);
		ServiceName serviceName = new ServiceName(serviceNameString);
		EnvironmentName environmentName = new EnvironmentName(environmentNameString);
		ServiceInstanceName serviceInstanceName = new ServiceInstanceName(serviceInstanceNameString);

		Service service = serviceRepository.getService(serviceName);
		Host host = hostRepository.getHost(hostName);
		Environment environment = environmentRepository.getEnvironment(environmentName);
		ServiceInstance serviceInstance = serviceInstanceRepository.getServiceInstance(serviceInstanceName, service,
				host, environment);
		String status = serviceInstance.getStatus().name();
		Script script = serviceInstance.getScript();
		if (!checkScriptIfPathNotMissing(perform, script)) {
			throw new EmptyField("Script must be set to perform an action on an instance!");
		}
		String param = perform.getPerformAction().toLowerCase();
		String path = "";
		if (param.equals("start") || param.equals("stop") || param.equals("restart")) {
			if (!status.equals("is_starting") && !status.equals("is_stopping")) {
				if (param.equals("start")) {
					path = resolvePath(script.getPathStart());
					serviceInstance.setStatus(Status.is_starting);
				}
				else if (param.equals("stop")) {
					path = resolvePath(script.getPathStop());
					serviceInstance.setStatus(Status.is_stopping);
				}
				else if (param.equals("restart")) {
					path = resolvePath(script.getPathRestart());
					serviceInstance.setStatus(Status.is_stopping);
				}
			}
			else {
				throw new ActionInProgress(serviceInstance.toString() + " " + status
						+ " Cannot perform action while another action is in progress.");
			}
		}
		else if (param.equals("status")) {
			path = resolvePath(script.getPathStatus());
		}
		else {
			throw new WrongParam(
					perform.getPerformAction() + " -- Use only this action commands: start, stop, restart, status");
		}

		scriptExecutor.executeScript(serviceName, environmentName, hostName, serviceInstanceName, path); //note: this is asynchronously executed
	}

	private boolean checkScriptIfPathNotMissing(PerformAction action, Script script) {
		if (script == null) {
			return false;
		}
		switch (action.getPerformAction()) {
			case "start":
				if (script.getPathStart() == null || script.getPathStart().getPath() == null
						|| script.getPathStart().getPath().equals("")) {
					return false;
				}
				break;
			case "stop":
				if (script.getPathStop() == null || script.getPathStop().getPath() == null
						|| script.getPathStop().getPath().equals("")) {
					return false;
				}
				break;
			case "restart":
				if (script.getPathRestart() == null || script.getPathRestart().getPath() == null
						|| script.getPathRestart().getPath().equals("")) {
					return false;
				}
				break;
			case "status":
				if (script.getPathStatus() == null || script.getPathStatus().getPath() == null
						|| script.getPathStatus().getPath().equals("")) {
					return false;
				}
				break;
		}
		return true;
	}

	private String resolvePath(Path path) {
		String p = getPath(path);
		if (p == null) {
			throw new EmptyField("To perform an action a valid path must be set!");
		}
		return p;
	}

	public List<String> getAllEnvironments() {
		List<String> environmentList = new LinkedList<>();
		for (Environment env : environmentRepository.getAllEnvironments()) {
			environmentList.add(env.getEnvironmentName().getName());
		}
		return environmentList;
	}

	public List<String> getServicesByEnvironmentName(String environmentName) {
		List<String> serviceList = new LinkedList<>();
		if (environmentName.equals("*")) {
			for (Service service : serviceRepository.getAllServices()) {
				if (!serviceList.contains(service.getServiceName().getName())) {
					serviceList.add(service.getServiceName().getName());
				}
			}
		}
		else {
			for (ServiceInstance instance : serviceInstanceRepository.getAllInstances()) {
				if (instance.getEnvironment().getEnvironmentName().getName().equals(environmentName)) {
					if (!serviceList.contains(instance.getService().getServiceName().getName())) {
						serviceList.add(instance.getService().getServiceName().getName());
					}
				}
			}
		}
		return serviceList;
	}
}
