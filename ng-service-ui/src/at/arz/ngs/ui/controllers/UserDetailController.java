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
	SecurityAdmin admin;

	private UserData user;
	private String name;
	private String firstLastName;

	private boolean handover;

	private List<UserRole> userRoles;

	@PostConstruct
	public void init() {

	}

	public String goToUserDetail(String name, String firstName, String lastName) {
		// userRoles = admin.getRoles();
		this.name = name;
		this.firstLastName = firstName + " " + lastName;
		return "userdetail";
	}

	public String addRoleToUser(String role, String user) {
		// admin.addRoleToUser(role, user, handover);
		return "";
	}

	public UserData getUser() {
		return user;
	}

	public void setUser(UserData user) {
		this.user = user;
	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public boolean isHandover() {
		return handover;
	}

	public void setHandover(boolean handover) {
		this.handover = handover;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstLastName() {
		return firstLastName;
	}

	public void setFirstLastName(String firstLastName) {
		this.firstLastName = firstLastName;
	}

}
