package at.arz.ngs.ui.controllers;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.commands.login.LoginResponse;
import at.arz.ngs.security.user.commands.UserData;
import at.arz.ngs.ui.data_collections.LoginCollection;

@RequestScoped
@Named("login")
public class LoginController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityAdmin admin;

	@Inject
	private UserController userController;

	private String userName;
	private String password;
	private boolean wrongInput;

	private String originalRequestedURI;
	private LoginCollection loginCollection;

	@PostConstruct
	public void init() {
		originalRequestedURI = determineOriginalURI();
		userName = "";
		password = "";
		loginCollection = new LoginCollection("", false);
	}

	public String login() {
		if (userController.getUserData() != null) {
			return originalRequestedURI;
		}

		if ((userName != null) && (!userName.equals("")) && (password != null) && (!password.equals(""))) {
			HttpServletRequest httpRequest = getCurrentHttpRequest();
			try {
				httpRequest.login(userName, password);
			}
			catch (ServletException e) {
				e.printStackTrace();
				loginCollection = new LoginCollection("Login fehlgeschlagen. Bitte überprüfen Sie Ihre Eingabedaten",
						true);
				return null;
			}

			try {
				LoginResponse response = admin.login();

				UserData user = response.getUser();

				if (user != null) {
					userController.setUserData(user);
					userController.setRenderAdminOnlyElements(admin.isAdmin());
					return "overview.xhtml?faces-redirect=true";
				}
				else {
					loginCollection = new LoginCollection(
							"Login fehlgeschlagen. Bitte überprüfen Sie Ihre Eingabedaten", true);
					return "";
				}
			}
			catch (RuntimeException e) {
				e.printStackTrace();
				loginCollection = new LoginCollection("Login fehlgeschlagen. Bitte überprüfen Sie Ihre Eingabedaten",
						true);
			}
		}
		else {
			loginCollection = new LoginCollection("Die Felder dürfen nicht leer sein!", true);
		}
		return "";
	}

	private String determineOriginalURI() {
		String originalURI = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
				.get(RequestDispatcher.FORWARD_REQUEST_URI);

		if (originalURI == null) {
			return originalRequestedURI = "overview";
		}
		originalURI = originalURI.substring(originalURI.lastIndexOf("/") + 1);

		String originalQuery = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
				.get(RequestDispatcher.FORWARD_QUERY_STRING);
		return originalRequestedURI = originalURI + "?faces-redirect=true&" + originalQuery;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isWrongInput() {
		return wrongInput;
	}

	public void setWrongInput(boolean wrongInput) {
		this.wrongInput = wrongInput;
	}

	private HttpServletRequest getCurrentHttpRequest() {
		HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return httpRequest;
	}

	public void redirectToLogin() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logout() {
		try {
			getCurrentHttpRequest().getSession().invalidate();
			getCurrentHttpRequest().logout();
		}
		catch (ServletException e) {
			e.printStackTrace();
		}
	}

	public LoginCollection getLoginCollection() {
		return loginCollection;
	}

	public void setLoginCollection(LoginCollection loginCollection) {
		this.loginCollection = loginCollection;
	}

}
