package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.get.ServiceInstanceResponse;

@RequestScoped
@Named("detail")
public class DetailViewController
		implements Serializable {


	private static final long serialVersionUID = 1L;

	@Inject
	private ServiceInstanceAdmin admin;

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


	@PostConstruct
	public void init() {
		// ServiceInstanceResponse response = admin.getServiceInstance("arctis", "pebk123", "lnx002", "arctis_1");
		// instance = response.getInstanceName();
		// service = response.getServiceName();
		// environment = response.getEnvironmentName();
		// host = response.getHostName();
		// status = response.getStatus().toString();
		//
		// ScriptData scriptData = response.getScript();
		// scriptName = scriptData.getScriptName();
		// pathStart = scriptData.getPathStart();
		// pathStop = scriptData.getPathStop();
		// pathRestart = scriptData.getPathRestart();
		// pathStatus = scriptData.getPathStatus();
	}


	public String detail(String instance, String service, String environment, String host) {
		ServiceInstanceResponse response = admin.getServiceInstance(service, environment, host, instance);
		instance = response.getInstanceName();
		service = response.getServiceName();
		environment = response.getEnvironmentName();
		host = response.getHostName();

		ScriptData scriptData = response.getScript();
		scriptName = scriptData.getScriptName();
		pathStart = scriptData.getPathStart();
		pathStop = scriptData.getPathStop();
		pathRestart = scriptData.getPathRestart();
		pathStatus = scriptData.getPathStatus();
		return "";
	}

	// public String detail() {
	//
	// instance = "testInstance";
	// service = "testService";
	// environment = "testEnvironment";
	// host = "testHost";
	// return "";
	// }

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
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
