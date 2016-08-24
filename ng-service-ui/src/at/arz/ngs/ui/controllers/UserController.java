package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.commands.Actor;
import at.arz.ngs.security.user.commands.UserData;

@SessionScoped
@Named("user")
public class UserController
		implements Serializable {

	private static final long serialVersionUID = 1L;
	private UserData userData;

	private boolean renderAdminOnlyElements;

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

	public Actor getCurrentActor() {
		return new Actor(userData.getUserName());
	}

	public boolean isRenderAdminOnlyElements() {
		return renderAdminOnlyElements;
	}

	public void setRenderAdminOnlyElements(boolean renderAdminOnlyElements) {
		this.renderAdminOnlyElements = renderAdminOnlyElements;
	}

}
