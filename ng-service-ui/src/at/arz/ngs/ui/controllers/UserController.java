package at.arz.ngs.ui.controllers;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SessionScoped
@Named("user")
public class UserController {
	
	String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
