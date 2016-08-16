package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;

@RequestScoped
@Named("saveEdit")
public class SaveEditServiceController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	ServiceInstanceAdmin admin;

	private String instance;
	private String service;
	private String environment;
	private String host;

	private String scriptName;
	private String pathStart;
	private String pathStop;
	private String pathRestart;
	private String pathStatus;

	public String saveNewServiceInstance() {
		System.err.println("\n\n\n\n\n\n\n" + this.instance + " " + this.service);
		// CreateNewServiceInstance command = new CreateNewServiceInstance();
		// command.setServiceName(this.service);
		// command.setEnvironmentName(this.environment);
		// command.setHostName(this.host);
		// command.setInstanceName(this.instance);
		// ScriptData scriptData = new ScriptData();
		// scriptData.setScriptName(this.scriptName);
		// scriptData.setPathStart(this.pathStart);
		// scriptData.setPathStop(this.pathStop);
		// scriptData.setPathRestart(this.pathRestart);
		// scriptData.setPathStatus(this.pathStatus);
		// command.setScript(scriptData);
		// admin.createNewServiceInstance(command);

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
