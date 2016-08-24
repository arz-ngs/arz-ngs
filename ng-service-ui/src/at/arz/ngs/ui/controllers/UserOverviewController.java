package at.arz.ngs.ui.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.user.commands.UserData;

@RequestScoped
@Named("useroverview")
public class UserOverviewController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityAdmin admin;

	private List<UserData> userOverview;

	@PostConstruct
	public void init() {
		userOverview = admin.getUserOverview().getUsers();
	}

	public void goToUserDetailView(String username) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("userdetail.xhtml?username=" + username);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
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
