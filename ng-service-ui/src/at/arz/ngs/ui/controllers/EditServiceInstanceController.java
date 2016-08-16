package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;

@RequestScoped
@Named("editServiceInstance")
public class EditServiceInstanceController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	private String instance;
	private String service;
	private String environment;
	private String host;
	private String status;

	private String scriptName;
	private String pathStart;
	private String pathStop;
	private String pathRestart;
	private String pathStatus;

	@Inject
	ServiceInstanceAdmin admin;

	public String editServiceInstance(	String instance,
										String service,
										String environment,
										String host,
										String scriptName,
										String pathStart,
										String pathStop,
										String pathRestart,
										String pathStatus) {
		this.instance = instance;
		this.service = service;
		this.environment = environment;
		this.host = host;
		this.scriptName = scriptName;
		this.pathStart = pathStart;
		this.pathStop = pathStop;
		this.pathRestart = pathRestart;
		this.pathStatus = pathStatus;

		System.err.println("\n\n\n\n" + this.instance + " edit");
		return "editNewServiceInstance";
	}

	public String saveEditedServiceInstance() {
		// admin.updateServiceInstance(command,
		// oldServiceNameString,
		// oldEnvironmentNameString,
		// oldHostNameString,
		// oldServiceInstanceNameString);
		return "";
	}


	public String getInstance() {
		return instance;
	}

	public String getService() {
		return service;
	}

	public String getEnvironment() {
		return environment;
	}

	public String getHost() {
		return host;
	}

	public String getStatus() {
		return status;
	}

	public String getScriptName() {
		return scriptName;
	}

	public String getPathStart() {
		return pathStart;
	}

	public String getPathStop() {
		return pathStop;
	}

	public String getPathRestart() {
		return pathRestart;
	}

	public String getPathStatus() {
		return pathStatus;
	}

}
