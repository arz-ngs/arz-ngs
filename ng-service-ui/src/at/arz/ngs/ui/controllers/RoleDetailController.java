package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.api.Action;
import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.permission.commands.PermissionData;
import at.arz.ngs.security.permission.commands.addToRole.AddPermissionToRole;
import at.arz.ngs.security.permission.commands.removeFromRole.RemovePermissionFromRole;
import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;

@ViewScoped
@Named("roleDetail")
public class RoleDetailController
		implements Serializable {

	public static final String PLEASE_CHOOSE = "Bitte auswählen...";

	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityAdmin admin;

	@Inject
	private ServiceInstanceAdmin si_admin;

	@Inject
	private UserController userController;

	private String currentRole;
	private List<PermissionData> permissions;
	private String chosenEnvID;
	private String chosenService;
	private String chosenAction;
	private List<String> availableEnvIDs;
	private List<String> availableServices;
	private List<String> availableActions;
	private boolean serviceDisabled;
	private ErrorCollection errorCollection;
	private String serviceCSSclass;

	@PostConstruct
	public void init() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		currentRole = params.get("role");

		refresh();
	}

	public void addPermission() {
		if (chosenAction.equals(PLEASE_CHOOSE) || chosenEnvID.equals(PLEASE_CHOOSE)
				|| chosenService.equals(PLEASE_CHOOSE)) {
			errorCollection = new ErrorCollection();
			errorCollection.addError(new Error(new IllegalArgumentException(
					"All fields must be set. Please choose one item from each dropdown.")));
			errorCollection.setShowPopup(true);
			return;
		}

		String envId = chosenEnvID.equals("alle") ? "*" : chosenEnvID;
		String service = chosenService.equals("alle") ? "*" : chosenService;
		String action = chosenAction.equals("alle") ? Action.all.name() : chosenAction;

		PermissionData permissionData = new PermissionData(envId, service, action);
		AddPermissionToRole command = new AddPermissionToRole(currentRole, permissionData);
		errorCollection = new ErrorCollection();
		try {
			admin.addPermissionToRole(userController.getCurrentActor(), command);
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			return;
		}

		chosenEnvID = PLEASE_CHOOSE;
		chosenService = PLEASE_CHOOSE;
		chosenAction = PLEASE_CHOOSE;
		refresh();
	}

	public String removePermission(PermissionData data) {
		errorCollection = new ErrorCollection();
		try {
			admin.removePermissionFromRole(userController.getCurrentActor(),
					new RemovePermissionFromRole(currentRole, data));
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			return "";
		}

		chosenEnvID = PLEASE_CHOOSE;
		chosenService = PLEASE_CHOOSE;
		chosenAction = PLEASE_CHOOSE;
		validateDropdowns();

		refresh();
		return "";
	}

	private void refresh() {
		if (chosenEnvID == null) {
			chosenEnvID = PLEASE_CHOOSE;
		}
		if (chosenService == null) {
			chosenService = PLEASE_CHOOSE;
		}
		if (chosenAction == null) {
			chosenAction = PLEASE_CHOOSE;
		}

		errorCollection = new ErrorCollection();
		try {
			permissions = admin.getPermissions(currentRole).getPermissions();

			validateDropdowns();
		}
		catch (RuntimeException e) {
			chosenEnvID = PLEASE_CHOOSE;
			chosenService = PLEASE_CHOOSE;
			chosenAction = PLEASE_CHOOSE;

			availableEnvIDs = new LinkedList<>();
			availableEnvIDs.add(PLEASE_CHOOSE);
			availableServices = new LinkedList<>();
			availableServices.add(PLEASE_CHOOSE);
			availableActions = new LinkedList<>();
			availableActions.add(PLEASE_CHOOSE);
			permissions = new LinkedList<>();

			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			return;
		}
	}

	private void validateDropdowns() {
		if (chosenEnvID.equals(PLEASE_CHOOSE)) {
			availableEnvIDs = new LinkedList<>();

			availableEnvIDs.add(PLEASE_CHOOSE);
			availableEnvIDs.add("alle");
			availableEnvIDs.addAll(si_admin.getAllEnvironments());

			serviceDisabled = true;
			serviceCSSclass = "dropdown_disabled";

			availableServices = new LinkedList<>();
			availableServices.add("Zuerst eine EnvId auswählen");
		}
		else {
			serviceDisabled = false;
			serviceCSSclass = ""; // no disabled class

			if (chosenService.equals(PLEASE_CHOOSE)) {
				availableServices = new LinkedList<>();
				availableServices.add(PLEASE_CHOOSE);
				availableServices.add("alle");

				String envQuery = chosenEnvID;
				if (chosenEnvID.equals("alle")) {
					envQuery = "*";
				}
				availableServices.addAll(si_admin.getServicesByEnvironmentName(envQuery));
			}
		}

		if (chosenAction.equals(PLEASE_CHOOSE)) {
			availableActions = new LinkedList<>();
			availableActions.add(PLEASE_CHOOSE);
			availableActions.add("alle");
			availableActions.add(Action.start.name());
			availableActions.add(Action.stop.name());
			availableActions.add(Action.restart.name());
			availableActions.add(Action.status.name());
		}

	}

	public void envIDchanged(ValueChangeEvent event) {
		chosenEnvID = (String) event.getNewValue();
		chosenService = PLEASE_CHOOSE;
		refresh();
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

	public String getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}

	public String getServiceCSSclass() {
		return serviceCSSclass;
	}

	public void setServiceCSSclass(String serviceCSSclass) {
		this.serviceCSSclass = serviceCSSclass;
	}

}
