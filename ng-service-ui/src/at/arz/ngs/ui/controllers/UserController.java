package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named("users")
public class UserController implements Serializable {
	
	private static final long serialVersionUID = 1L;


	public String goToUserOverview() {
		return "useroverview";
	}
	
	
}
