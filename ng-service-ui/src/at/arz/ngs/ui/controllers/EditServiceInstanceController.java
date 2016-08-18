package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.get.ServiceInstanceResponse;
import at.arz.ngs.serviceinstance.commands.update.UpdateServiceInstance;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;

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
	private String oldInformation;

	private String oldScriptName;
	private String oldPathStart;
	private String oldPathStop;
	private String oldPathRestart;
	private String oldPathStatus;
	private long version;

	private String instance;
	private String service;
	private String environment;
	private String host;

	private ServiceInstanceResponse response;

	private String completeName;

	@Inject
	ServiceInstanceAdmin admin;

	@Inject
	private ServiceInstanceController serviceInstanceController;

	private ErrorCollection errorCollection;

	@PostConstruct
	public void init() {
		response = new ServiceInstanceResponse();
	}

	public ServiceInstanceResponse getResponse() {
		return response;
	}

	public void setResponse(ServiceInstanceResponse response) {
		this.response = response;
	}

	public String editServiceInstance(String instance, String service, String environment, String host) {
		this.instance = instance;
		this.service = service;
		this.environment = environment;
		this.host = host;

		errorCollection = new ErrorCollection();
		try {
			this.response = admin.getServiceInstance(service, environment, host, instance);
		} catch (RuntimeException e) {
			System.err.println("\n\n\n\n\n" + "ERROR");
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			return null;
		}

		this.oldService = response.getServiceName();
		this.oldEnvironment = response.getEnvironmentName();
		this.oldHost = response.getHostName();
		this.oldInstance = response.getInstanceName();
		this.version = response.getVersion();
		ScriptData oldScriptData = response.getScript();
		this.oldScriptName = oldScriptData.getScriptName();
		this.oldPathStart = oldScriptData.getPathStart();
		this.oldPathStop = oldScriptData.getPathStop();
		this.oldPathRestart = oldScriptData.getPathRestart();
		this.oldPathStatus = oldScriptData.getPathStatus();
		this.oldInformation = response.getInformation();
		this.completeName = service + "/" + environment + "/" + host + "/" + instance;
		return "editServiceInstance";
	}

	public String getCompleteName() {
		return completeName;
	}

	public String saveEditedServiceInstance() {
		UpdateServiceInstance command = new UpdateServiceInstance();
		command.setServiceName(this.oldService);
		command.setEnvironmentName(this.oldEnvironment);
		command.setHostName(this.oldHost);
		command.setInstanceName(this.oldInstance);
		command.setInformation(oldInformation);

		ScriptData scriptData = new ScriptData();
		scriptData.setScriptName(this.oldScriptName);
		scriptData.setPathStart(this.oldPathStart);
		scriptData.setPathStop(this.oldPathStop);
		scriptData.setPathRestart(this.oldPathRestart);
		scriptData.setPathStatus(this.oldPathStatus);
		command.setScript(scriptData);
		command.setVersion(this.version);

		errorCollection = new ErrorCollection();
		try {
			admin.updateServiceInstance(command,
										this.response.getServiceName(),
										this.response.getEnvironmentName(),
										this.response.getHostName(),
										this.response.getInstanceName());
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

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public ErrorCollection getErrorCollection() {
		return errorCollection;
	}

	public void setErrorCollection(ErrorCollection errorCollection) {
		this.errorCollection = errorCollection;
	}

	public String getOldInformation() {
		return oldInformation;
	}

	public void setOldInformation(String oldInformation) {
		this.oldInformation = oldInformation;
	}

}
