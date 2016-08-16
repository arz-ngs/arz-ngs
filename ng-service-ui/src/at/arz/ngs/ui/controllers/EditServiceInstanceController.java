package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.create.CreateNewServiceInstance;

@RequestScoped
@Named("editServiceInstance")
public class EditServiceInstanceController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	private String oldInstance;
	private String oldService;
	private String oldEnvironment;
	private String oldHost;
	private String oldStatus;

	private String oldScriptName;
	private String oldPathStart;
	private String oldPathStop;
	private String oldPathRestart;
	private String oldPathStatus;

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

	// private String completeName;

	@Inject
	ServiceInstanceAdmin admin;

	@PostConstruct
	public void init() {
	}

	public String editServiceInstance(String a) {
		System.err.println("\n\n\n\n\n" + a);

		System.err.println("\n\n\n\n" + this.instance + " edit");
		return "editServiceInstance";
	}

	public String saveEditedServiceInstance() {

		CreateNewServiceInstance command = new CreateNewServiceInstance();
		command.setServiceName(this.service);
		command.setEnvironmentName(this.environment);
		command.setHostName(this.host);
		command.setInstanceName(this.instance);
		// version
		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName(this.scriptName);
		scriptData.setPathStart(this.pathStart);
		scriptData.setPathStop(this.pathStop);
		scriptData.setPathRestart(this.pathRestart);
		scriptData.setPathStatus(this.pathStatus);
		command.setScript(scriptData);
		admin.createNewServiceInstance(command);
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

	public String getOldInstance() {
		return oldInstance;
	}

	public void setOldInstance(String oldInstance) {
		this.oldInstance = oldInstance;
	}

	public String getOldService() {
		return oldService;
	}

	public void setOldService(String oldService) {
		this.oldService = oldService;
	}

	public String getOldEnvironment() {
		return oldEnvironment;
	}

	public void setOldEnvironment(String oldEnvironment) {
		this.oldEnvironment = oldEnvironment;
	}

	public String getOldHost() {
		return oldHost;
	}

	public void setOldHost(String oldHost) {
		this.oldHost = oldHost;
	}

	public String getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getOldScriptName() {
		return oldScriptName;
	}

	public void setOldScriptName(String oldScriptName) {
		this.oldScriptName = oldScriptName;
	}

	public String getOldPathStart() {
		return oldPathStart;
	}

	public void setOldPathStart(String oldPathStart) {
		this.oldPathStart = oldPathStart;
	}

	public String getOldPathStop() {
		return oldPathStop;
	}

	public void setOldPathStop(String oldPathStop) {
		this.oldPathStop = oldPathStop;
	}

	public String getOldPathRestart() {
		return oldPathRestart;
	}

	public void setOldPathRestart(String oldPathRestart) {
		this.oldPathRestart = oldPathRestart;
	}

	public String getOldPathStatus() {
		return oldPathStatus;
	}

	public void setOldPathStatus(String oldPathStatus) {
		this.oldPathStatus = oldPathStatus;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public void setPathStart(String pathStart) {
		this.pathStart = pathStart;
	}

	public void setPathStop(String pathStop) {
		this.pathStop = pathStop;
	}

	public void setPathRestart(String pathRestart) {
		this.pathRestart = pathRestart;
	}

	public void setPathStatus(String pathStatus) {
		this.pathStatus = pathStatus;
	}

}
