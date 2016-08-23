package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.role.commands.UserRole;
import at.arz.ngs.security.user.commands.UserData;

@SessionScoped
@Named("userDetail")
public class UserDetailController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityAdmin admin;

	private UserData currentUser;

	private boolean handover;

	private List<UserRole> currentUserRoles;

	private List<String> availableRoles;

	private String chosenElement;

	@PostConstruct
	public void init() {
		if (currentUser == null) {
			currentUser = new UserData("", "please choose", " an user", "");
			currentUserRoles = new LinkedList<>();
			availableRoles = new LinkedList<>();
			chosenElement = "Bitte auswählen...";
			availableRoles.add(chosenElement);
			return;
		}
		formSubmit();
	}

	public void formSubmit() {
		currentUserRoles = admin.getRolesForUser(currentUser.getUserName()).getUserRoles();
		availableRoles = new LinkedList<>();
		chosenElement = "Bitte auswählen...";
		availableRoles.add("Bitte auswählen...");
		availableRoles.addAll(admin.getAllRoles().getRoles());
	}

	public String goToUserDetail(String name, String firstName, String lastName) {
		currentUser = new UserData(name, firstName, lastName, "");
		formSubmit();

		return "userdetail";
	}

	public String addRoleToUser(String role, String user) {
		// admin.addRoleToUser(actor, command); //TODO
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
}
