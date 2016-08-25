package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.create.CreateNewServiceInstance;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;

@RequestScoped
@Named("newServiceInstance")
public class NewServiceInstanceController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	ServiceInstanceAdmin admin;

	@Inject
	private ServiceInstanceController serviceInstanceController;

	@Inject
	private UserController userController;

	private String instance;
	private String service;
	private String environment;
	private String host;

	private String scriptName;
	private String pathStart;
	private String pathStop;
	private String pathRestart;
	private String pathStatus;
	private String information;

	private ErrorCollection errorCollection;

	public String addNewServiceInstance() {
		return "newServiceInstance";
	}

	@PostConstruct
	public void init() {
	}

	public String saveNewServiceInstance() {

		CreateNewServiceInstance command = new CreateNewServiceInstance();
		command.setServiceName(this.service);
		command.setEnvironmentName(this.environment);
		command.setHostName(this.host);
		command.setInstanceName(this.instance);
		command.setInformation(this.information);
		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName(this.scriptName);
		scriptData.setPathStart(this.pathStart);
		scriptData.setPathStop(this.pathStop);
		scriptData.setPathRestart(this.pathRestart);
		scriptData.setPathStatus(this.pathStatus);
		command.setScript(scriptData);

		errorCollection = new ErrorCollection();
		try {
			admin.createNewServiceInstance(userController.getCurrentActor(), command);
		} catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			return null;
		}

		serviceInstanceController.formSubmit();
		return "overview";
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

	public ErrorCollection getErrorCollection() {
		return errorCollection;
	}

	public void setErrorCollection(ErrorCollection errorCollection) {
		this.errorCollection = errorCollection;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

}
