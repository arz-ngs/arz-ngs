package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.commands.login.Login;

@RequestScoped
@Named("login")
public class LoginController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	SecurityAdmin admin;

	private String userName;
	private String password;
	private boolean wrongInput;

	public String sendUserData() {
		if ((userName != null) && (!userName.equals("")) && (password != null) && (!password.equals(""))) {
			// TODO send data to admin
			Login loginData = new Login(userName, password);
			if (userName.equals("admin") && password.equals("admin")) {
				return "overview";
			}
		}
		return "";
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

}
