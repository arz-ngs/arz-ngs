package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.user.commands.UserData;

@SessionScoped
@Named("user")
public class UserController
		implements Serializable {

	private static final long serialVersionUID = 1L;
	private UserData userData;

	@Inject
	private LoginController loginController;

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public String logout() {
		userData = null;
		loginController.logout();
		return "login";
	}
}
