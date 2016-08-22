package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.role.commands.get.UserRole;
import at.arz.ngs.security.user.commands.UserData;

@RequestScoped
@Named("userDetail")
public class UserDetailController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	SecurityAdmin admin;

	private UserData user;
	private String completeName;

	private List<UserRole> userRoles;

	@PostConstruct
	public void init() {
		// userRoles = admin.getRoles();
	}

	public String goToUserDetail(String name, String firstName, String lastName) {
		this.completeName = name + " - " + firstName + " " + lastName;
		return "userdetail";
	}

	public UserData getUser() {
		return user;
	}

	public void setUser(UserData user) {
		this.user = user;
	}

	public String getCompleteName() {
		return completeName;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

}
