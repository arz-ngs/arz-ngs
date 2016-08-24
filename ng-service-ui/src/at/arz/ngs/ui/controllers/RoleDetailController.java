package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.permission.commands.PermissionData;
import at.arz.ngs.ui.data_collections.ErrorCollection;

@RequestScoped
@Named("roleDetail")
public class RoleDetailController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityAdmin admin;

	private List<PermissionData> permissions;

	private String chosenEnvID;

	private String chosenService;

	private String chosenAction;

	private List<String> availableEnvIDs;

	private List<String> availableServices;

	private List<String> availableActions;

	private boolean serviceDisabled;

	private ErrorCollection errorCollection;

	public String goToRoleDetail(String role) {
		permissions = admin.getPermissions(role).getPermissions();
		return "userdetail";
	}

	public void envIDchanged(ValueChangeEvent event) {
		chosenEnvID = (String) event.getNewValue();
	}

	public List<PermissionData> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionData> permissions) {
		this.permissions = permissions;
	}

	public String getChosenEnvID() {
		return chosenEnvID;
	}

	public void setChosenEnvID(String chosenEnvID) {
		this.chosenEnvID = chosenEnvID;
	}

	public String getChosenService() {
		return chosenService;
	}

	public void setChosenService(String chosenService) {
		this.chosenService = chosenService;
	}

	public String getChosenAction() {
		return chosenAction;
	}

	public void setChosenAction(String chosenAction) {
		this.chosenAction = chosenAction;
	}

	public List<String> getAvailableEnvIDs() {
		return availableEnvIDs;
	}

	public void setAvailableEnvIDs(List<String> availableEnvIDs) {
		this.availableEnvIDs = availableEnvIDs;
	}

	public List<String> getAvailableServices() {
		return availableServices;
	}

	public void setAvailableServices(List<String> availableServices) {
		this.availableServices = availableServices;
	}

	public List<String> getAvailableActions() {
		return availableActions;
	}

	public void setAvailableActions(List<String> availableActions) {
		this.availableActions = availableActions;
	}

	public boolean isServiceDisabled() {
		return serviceDisabled;
	}

	public void setServiceDisabled(boolean serviceDisabled) {
		this.serviceDisabled = serviceDisabled;
	}

	public ErrorCollection getErrorCollection() {
		return errorCollection;
	}

	public void setErrorCollection(ErrorCollection errorCollection) {
		this.errorCollection = errorCollection;
	}

}
