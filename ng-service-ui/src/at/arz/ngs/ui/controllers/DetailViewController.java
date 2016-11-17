package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.commands.getSIDetailPermissions.PerformActionPermissions;
import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.ScriptData;
import at.arz.ngs.serviceinstance.commands.get.ServiceInstanceResponse;
import at.arz.ngs.ui.data_collections.CommandButtonCollection;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;

@ViewScoped
@Named("serviceInstanceDetail")
public class DetailViewController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ServiceInstanceAdmin admin;

	@Inject
	private SecurityAdmin securityAdmin;

	private String instance;
	private String service;
	private String environment;
	private String host;
	private String status;
	private String version;
	private String completeName;
	private String information;

	private String scriptName;
	private String pathStart;
	private String pathStop;
	private String pathRestart;
	private String pathStatus;

	private CommandButtonCollection commandButtonCollection;

	private ErrorCollection errorCollection;

	@PostConstruct
	public void init() {
		errorCollection = new ErrorCollection();

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		showDetail(params.get("instance"), params.get("service"), params.get("env"), params.get("host"));
		validateCommandButtons();
	}

	private void showDetail(String instance, String service, String environment, String host) {
		ServiceInstanceResponse response = null;

		errorCollection = new ErrorCollection();
		try {
			response = admin.getServiceInstance(service, environment, host, instance);
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e.getClass().getSimpleName(), e.getMessage(), e.getStackTrace()));
			errorCollection.setShowPopup(true);
		}
		if (!errorCollection.isShowPopup()) {
			this.information = response.getInformation();
			this.instance = response.getInstanceName();
			this.service = response.getServiceName();
			this.environment = response.getEnvironmentName();
			this.host = response.getHostName();
			this.status = response.getStatus().name();
			this.version = String.valueOf(response.getVersion());
			this.completeName = this.service + "/" + this.environment + "/" + this.host + "/" + this.instance;

			ScriptData scriptData = response.getScript();
			scriptName = scriptData.getScriptName();
			pathStart = scriptData.getPathStart();
			pathStop = scriptData.getPathStop();
			pathRestart = scriptData.getPathRestart();
			pathStatus = scriptData.getPathStatus();
		}
	}

	private void validateCommandButtons() {
		PerformActionPermissions permissions = securityAdmin
				.isAuthorizedToPerformActions(new EnvironmentName(this.environment), new ServiceName(this.service));

		commandButtonCollection = new CommandButtonCollection(); //reset the collection to defaults
		if (pathEmpty(pathStart) || !permissions.isAbleToStart()) {
			commandButtonCollection.setStartCSSClass(CommandButtonCollection.DISABLED_CSS_CLASS);
			commandButtonCollection.setStartDisabled(true);
		}
		if (pathEmpty(pathStop) || !permissions.isAbleToStop()) {
			commandButtonCollection.setStopCSSClass(CommandButtonCollection.DISABLED_CSS_CLASS);
			commandButtonCollection.setStopDisabled(true);
		}
		if (pathEmpty(pathRestart) || !permissions.isAbleToRestart()) {
			commandButtonCollection.setRestartCSSClass(CommandButtonCollection.DISABLED_CSS_CLASS);
			commandButtonCollection.setRestartDisabled(true);
		}
		if (pathEmpty(pathStatus) || !permissions.isAbleToStatus()) {
			commandButtonCollection.setStatusCSSClass(CommandButtonCollection.DISABLED_CSS_CLASS);
			commandButtonCollection.setStatusDisabled(true);
		}
	}

	private boolean pathEmpty(String path) {
		return path == null || path.trim().equals("");
	}

	public String getCompleteName() {
		return completeName;
	}

	public String getVersion() {
		return version;
	}

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

	public void setVersion(String version) {
		this.version = version;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
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

	public CommandButtonCollection getCommandButtonCollection() {
		return commandButtonCollection;
	}

	public void setCommandButtonCollection(CommandButtonCollection commandButtonCollection) {
		this.commandButtonCollection = commandButtonCollection;
	}

}
