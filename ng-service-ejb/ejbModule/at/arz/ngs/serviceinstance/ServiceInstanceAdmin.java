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
import at.arz.ngs.serviceinstance.commands.update.UpdateStatus;

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

		ScriptData scriptData = command.getScript();
		String information = command.getInformation();

		HostName hostName = getHostName(command.getHostName());
		ServiceName serviceName = getServiceName(command.getServiceName());
		EnvironmentName environmentName = getEnvironmentName(command.getEnvironmentName());
		ServiceInstanceName serviceInstanceName = getServiceInstanceName(command.getInstanceName());

		String scriptNameString = scriptData.getScriptName();

		Service service = getOrCreateNewService(serviceName);
		Environment environment = getOrCreateNewEnvironment(environmentName);
		Host host = getOrCreateNewHost(hostName);

		ScriptName scriptName = null;
		if (scriptNameString == null || scriptNameString.trim().equals("")) {
			scriptName = new ScriptName(environmentName, serviceInstanceName, hostName, serviceName);
		}
		else {
			scriptName = new ScriptName(scriptNameString);
		}
		Script script = getOrCreateNewScript(scriptName, getPathStart(scriptData.getPathStart()),
				getPathStop(scriptData.getPathStop()), getPathRestart(scriptData.getPathRestart()),
				getPathStatus(scriptData.getPathStatus()));

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

	public void updateServiceInstanceStatus(Actor actor, String service, String environment, String host,
			String serviceInstance, UpdateStatus command) {
		securityAdmin.proofActorAdminAccess(actor);

		ServiceInstance si = getServiceInstanceFromStrings(service, environment, host, serviceInstance);
		si.setStatus(convertToStatus(command.getStatus()));
	}

	public void updateServiceInstance(Actor actor, UpdateServiceInstance command, String oldServiceNameString,
			String oldEnvironmentNameString, String oldHostNameString, String oldServiceInstanceNameString) {
		securityAdmin.proofActorAdminAccess(actor);

		ServiceName oldServiceName = getServiceName(oldServiceNameString);
		EnvironmentName oldEnvironmentName = getEnvironmentName(oldEnvironmentNameString);
		HostName oldHostName = getHostName(oldHostNameString);
		ServiceInstanceName oldServiceInstanceName = getServiceInstanceName(oldServiceInstanceNameString);

		ScriptData scriptData = command.getScript();
		String information = command.getInformation();
		long version = command.getVersion();

		HostName hostName = getHostName(command.getHostName());
		ServiceName serviceName = getServiceName(command.getServiceName());
		EnvironmentName environmentName = getEnvironmentName(command.getEnvironmentName());
		ServiceInstanceName serviceInstanceName = getServiceInstanceName(command.getInstanceName());

		String scriptNameString = scriptData.getScriptName();

		if (information == null) {
			information = "";
		}

		ScriptName scriptName = null;
		if (scriptNameString == null || scriptNameString.trim().equals("")) {
			scriptName = new ScriptName(environmentName, serviceInstanceName, hostName, serviceName);
		}
		else {
			scriptName = new ScriptName(scriptNameString);
		}
		Script newScript = getOrCreateNewScript(scriptName, getPathStart(scriptData.getPathStart()),
				getPathStop(scriptData.getPathStop()), getPathRestart(scriptData.getPathRestart()),
				getPathStatus(scriptData.getPathStatus()));

		ServiceInstance oldServiceInstance = getServiceInstance(oldServiceName, oldEnvironmentName, oldHostName,
				oldServiceInstanceName);

		Service newService = getOrCreateNewService(serviceName);
		Environment newEnvironment = getOrCreateNewEnvironment(environmentName);
		Host newHost = getOrCreateNewHost(hostName);

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

		removeAllUnusedElements();
	}

	public void removeServiceInstance(Actor actor, String service, String environment, String host,
			String serviceInstance) {
		securityAdmin.proofActorAdminAccess(actor);

		ServiceInstance si = getServiceInstanceFromStrings(service, environment, host, serviceInstance);

		serviceInstanceRepository.removeServiceInstance(si);

		removeAllUnusedElements();
	}

	public ServiceInstanceResponse getServiceInstance(String service, String environment, String host,
			String serviceInstance) {

		ServiceInstance si = getServiceInstanceFromStrings(service, environment, host, serviceInstance);

		if (si != null) {
			ServiceInstanceResponse response = new ServiceInstanceResponse();
			response.setEnvironmentName(si.getEnvironment().getEnvironmentName().toString());
			response.setServiceName(si.getService().getServiceName().toString());
			response.setHostName(si.getHost().getHostName().toString());
			response.setInstanceName(si.getServiceInstanceName().toString());
			response.setStatus(si.getStatus());
			response.setVersion(si.getVersion());
			response.setInformation(si.getInformation());

			Script script = si.getScript();
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
			throw new ServiceInstanceNotFound(serviceInstance, serviceInstance, host, environment);
		}
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

	public void performAction(Actor actor, String serviceNameString, String environmentNameString,
			String hostNameString, String serviceInstanceNameString, PerformAction perform) {

		securityAdmin.proofPerformAction(new EnvironmentName(environmentNameString), new ServiceName(serviceNameString),
				Action.valueOf(perform.getPerformAction()), actor);

		HostName hostName = getHostName(hostNameString);
		ServiceName serviceName = getServiceName(serviceNameString);
		EnvironmentName environmentName = getEnvironmentName(environmentNameString);
		ServiceInstanceName serviceInstanceName = getServiceInstanceName(serviceInstanceNameString);

		ServiceInstance serviceInstance = getServiceInstance(serviceName, environmentName, hostName,
				serviceInstanceName);

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
					perform.getPerformAction() + " -- Only use this action commands: start, stop, restart, status");
		}

		scriptExecutor.executeScript(serviceName, environmentName, hostName, serviceInstanceName, path, perform); //note: this is asynchronously executed
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

	private Host getOrCreateNewHost(HostName hostName) {
		try {
			return hostRepository.getHost(hostName);
		}
		catch (HostNotFound eHost) {
			hostRepository.addHost(hostName);
			return hostRepository.getHost(hostName);
		}
	}

	private Service getOrCreateNewService(ServiceName serviceName) {
		try {
			return serviceRepository.getService(serviceName);
		}
		catch (ServiceNotFound eService) {
			serviceRepository.addService(serviceName);
			return serviceRepository.getService(serviceName);
		}
	}

	private Environment getOrCreateNewEnvironment(EnvironmentName environmentName) {
		try {
			return environmentRepository.getEnvironment(environmentName);
		}
		catch (EnvironmentNotFound eEnvironment) {
			environmentRepository.addEnvironment(environmentName);
			return environmentRepository.getEnvironment(environmentName);
		}
	}

	private Script getOrCreateNewScript(ScriptName scriptName, PathStart pathStart, PathStop pathStop,
			PathRestart pathRestart, PathStatus pathStatus) {

		Script sc;
		try {
			sc = scriptRepository.getScript(scriptName);
		}
		catch (ScriptNotFound e) {
			scriptRepository.addScript(scriptName, pathStart, pathStop, pathRestart, pathStatus);
			sc = scriptRepository.getScript(scriptName);
		}
		sc.setPathStart(pathStart);
		sc.setPathStop(pathStop);
		sc.setPathRestart(pathRestart);
		sc.setPathStatus(pathStatus);
		return sc;
	}

	private PathStart getPathStart(String pathStart) {
		if (pathStart == null || pathStart.trim().equals("")) {
			return new PathStart("");
		}
		return new PathStart(pathStart);
	}

	private PathRestart getPathRestart(String pathRestart) {
		if (pathRestart == null || pathRestart.trim().equals("")) {
			return new PathRestart("");
		}
		return new PathRestart(pathRestart);
	}

	private PathStop getPathStop(String pathStop) {
		if (pathStop == null || pathStop.trim().equals("")) {
			return new PathStop("");
		}
		return new PathStop(pathStop);
	}

	private PathStatus getPathStatus(String pathStatus) {
		if (pathStatus == null || pathStatus.trim().equals("")) {
			return new PathStatus("");
		}
		return new PathStatus(pathStatus);
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

	private ServiceName getServiceName(String service) {
		if (service == null || service.trim().equals("")) {
			throw new EmptyField("The service field must not be null or empty!");
		}
		return new ServiceName(service);
	}

	private EnvironmentName getEnvironmentName(String environment) {
		if (environment == null || environment.trim().equals("")) {
			throw new EmptyField("The environment field must not be null or empty!");
		}
		return new EnvironmentName(environment);
	}

	private HostName getHostName(String host) {
		if (host == null || host.trim().equals("")) {
			throw new EmptyField("The host field must not be null or empty!");
		}
		return new HostName(host);
	}

	private ServiceInstanceName getServiceInstanceName(String serviceInstance) {
		if (serviceInstance == null || serviceInstance.trim().equals("")) {
			throw new EmptyField("The serviceInstance field must not be null or empty!");
		}
		return new ServiceInstanceName(serviceInstance);
	}

	private Host getHost(String host) {
		return getHost(getHostName(host));
	}

	private Host getHost(HostName hostName) {
		return hostRepository.getHost(hostName);
	}

	private Environment getEnvironment(String environment) {
		return getEnvironment(getEnvironmentName(environment));
	}

	private Environment getEnvironment(EnvironmentName environmentName) {
		return environmentRepository.getEnvironment(environmentName);
	}

	private Service getService(String service) {
		return getService(getServiceName(service));
	}

	private Service getService(ServiceName serviceName) {
		return serviceRepository.getService(serviceName);
	}

	private ServiceInstance getServiceInstanceFromStrings(String service, String environment, String host,
			String serviceInstance) {
		return getServiceInstance(getService(service), getEnvironment(environment), getHost(host),
				getServiceInstanceName(serviceInstance));
	}

	private ServiceInstance getServiceInstance(ServiceName serviceName, EnvironmentName environmentName,
			HostName hostName, ServiceInstanceName serviceInstanceName) {
		return getServiceInstance(getService(serviceName), getEnvironment(environmentName), getHost(hostName),
				serviceInstanceName);
	}

	private ServiceInstance getServiceInstance(Service service, Environment environment, Host host,
			ServiceInstanceName serviceInstanceName) {
		return serviceInstanceRepository.getServiceInstance(serviceInstanceName, service, host, environment);
	}

	private Status convertToStatus(String status) {
		switch (status) {
			case "active":
				return Status.active;
			case "not_active":
				return Status.not_active;
			case "is_starting":
				return Status.is_starting;
			case "is_stopping":
				return Status.is_stopping;
			case "failed":
				return Status.failed;
			default:
				return Status.unknown;
		}
	}

	private String getPath(Path path) {
		if (path == null) {
			return null;
		}

		return path.getPath();
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
				return true;
			case "stop":
				if (script.getPathStop() == null || script.getPathStop().getPath() == null
						|| script.getPathStop().getPath().equals("")) {
					return false;
				}
				return true;
			case "restart":
				if (script.getPathRestart() == null || script.getPathRestart().getPath() == null
						|| script.getPathRestart().getPath().equals("")) {
					return false;
				}
				return true;
			case "status":
				if (script.getPathStatus() == null || script.getPathStatus().getPath() == null
						|| script.getPathStatus().getPath().equals("")) {
					return false;
				}
				return true;
			default:
				return false;
		}
	}

	private String resolvePath(Path path) {
		String p = getPath(path);
		if (p == null) {
			throw new EmptyField("To perform an action a valid path must be set!");
		}
		return p;
	}
}
