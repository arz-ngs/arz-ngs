package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.role.commands.UserRole;
import at.arz.ngs.security.user.commands.UserData;

@RequestScoped
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

	@PostConstruct
	public void init() {

	}

	public String goToUserDetail(String name, String firstName, String lastName) {
		currentUserRoles = admin.getRolesForUser(name).getUserRoles();
		currentUser = new UserData(name, firstName, lastName, "");
		availableRoles = admin.getAllRoles().getRoles();
		return "userdetail";
	}

	public String addRoleToUser(String role, String user) {
		// admin.addRoleToUser(role, user, handover);
		return "";
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
