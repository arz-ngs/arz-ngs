package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.role.commands.UserRole;
import at.arz.ngs.security.user.commands.UserData;
import at.arz.ngs.security.user.commands.addRole.AddRoleToUser;
import at.arz.ngs.security.user.commands.removeRole.RemoveRoleFromUser;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;

@SessionScoped
@Named("userDetail")
public class UserDetailController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityAdmin admin;

	@Inject
	private UserController userController;

	private UserData currentUser;

	private boolean handover;

	private List<UserRole> currentUserRoles;

	private List<String> availableRoles;

	private String chosenElement;

	private ErrorCollection errorCollection;

	@PostConstruct
	public void init() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		errorCollection = new ErrorCollection();
		try {
			currentUser = new UserData(params.get("username"), "", "", ""); //TODO get user from admin
		}
		catch (RuntimeException e) {
			availableRoles = new LinkedList<>();
			chosenElement = "Bitte auswählen...";
			availableRoles.add("Bitte auswählen...");
			currentUser = new UserData("", "", "", "");

			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			return;
		}
		formSubmit();
	}

	private void formSubmit() {
		currentUserRoles = admin.getRolesForUser(currentUser.getUserName()).getUserRoles();
		availableRoles = new LinkedList<>();
		availableRoles.add("Bitte auswählen...");

		errorCollection = new ErrorCollection();
		try {
			availableRoles.addAll(admin.getAllRoles().getRoles());
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
		}
	}

	public String removeRoleFromUser(String role) {
		RemoveRoleFromUser command = new RemoveRoleFromUser(role, currentUser.getUserName());

		errorCollection = new ErrorCollection();
		try {
			admin.removeRoleFromUser(userController.getCurrentActor(), command);
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			return "";
		}

		formSubmit();
		return "";
	}

	public String addRoleToUser() {
		AddRoleToUser command = new AddRoleToUser(currentUser.getUserName(), chosenElement, handover);

		errorCollection = new ErrorCollection();
		try {
			admin.addRoleToUser(userController.getCurrentActor(), command);
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			return "";
		}

		formSubmit();
		return "";
	}

	public String getChosenElement() {
		return chosenElement;
	}

	public void setChosenElement(String chosenElement) {
		this.chosenElement = chosenElement;
	}

	public UserData getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserData currentUser) {
		this.currentUser = currentUser;
	}

	public List<UserRole> getCurrentUserRoles() {
		return currentUserRoles;
	}

	public void setCurrentUserRoles(List<UserRole> currentUserRoles) {
		this.currentUserRoles = currentUserRoles;
	}

	public boolean isHandover() {
		return handover;
	}

	public void setHandover(boolean handover) {
		this.handover = handover;
	}

	public List<String> getAvailableRoles() {
		return availableRoles;
	}

	public void setAvailableRoles(List<String> availableRoles) {
		this.availableRoles = availableRoles;
	}

	public ErrorCollection getErrorCollection() {
		return errorCollection;
	}

	public void setErrorCollection(ErrorCollection errorCollection) {
		this.errorCollection = errorCollection;
	}
}
