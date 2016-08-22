package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.user.commands.UserData;

@RequestScoped
@Named("users")
public class UserOverviewController implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject
	SecurityAdmin admin;

	List<UserData> userOverview;

	@PostConstruct
	public void init() {
		// users = admin.getAllUsers().getUsers();
	}

	public String goToUserOverview() {
		return "useroverview";
	}

	public List<UserData> getUserOverview() {
		return userOverview;
	}

	public void setUserOverview(List<UserData> userOverview) {
		this.userOverview = userOverview;
	}

	
}
