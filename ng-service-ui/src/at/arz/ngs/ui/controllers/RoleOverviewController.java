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
import at.arz.ngs.security.role.commands.create.CreateRole;
import at.arz.ngs.security.role.commands.remove.RemoveRole;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;

@RequestScoped
@Named("roleoverview")
public class RoleOverviewController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityAdmin admin;

	@Inject
	private UserController userController;

	private List<String> roleOverview;

	private String createRoleName;

	private ErrorCollection errorCollection;
	
	@PostConstruct
	public void init() {
		refresh();
	}

	public String goToRoleOverview() {
		createRoleName = "";
		refresh();
		return "roleoverview";
	}

	private void refresh() {
		errorCollection = new ErrorCollection();
		try {
			roleOverview = admin.getAllRoles().getRoles();;
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
		}
	}

	public String addRole() {
		if (createRoleName == null || createRoleName.trim().equals("")) {
			errorCollection = new ErrorCollection();
			errorCollection.addError(new Error(new IllegalArgumentException("The name of a role must not be empty!")));
			errorCollection.setShowPopup(true);
			return "";
		}

		errorCollection = new ErrorCollection();
		try {
			admin.createRole(userController.getCurrentActor(), new CreateRole(createRoleName));
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
		}
		refresh();
		createRoleName = "";
		return "roleoverview";
	}

	public String removeRole(String roleName) {
		errorCollection = new ErrorCollection();
		try {
			admin.removeRole(userController.getCurrentActor(), new RemoveRole(roleName));
		} catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
		}
		refresh();
		return "roleoverview";
	}

	public void goToRoleDetail(String role) {
		try {
			FacesContext.getCurrentInstance()
						.getExternalContext()
						.redirect("roledetail.xhtml?role=" + role);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> getRoleOverview() {
		return roleOverview;
	}

	public void setRoleOverview(List<String> roleOverview) {
		this.roleOverview = roleOverview;
	}

	public String getCreateRoleName() {
		return createRoleName;
	}

	public void setCreateRoleName(String createRoleName) {
		this.createRoleName = createRoleName;
	}

	public ErrorCollection getErrorCollection() {
		return errorCollection;
	}

	public void setErrorCollection(ErrorCollection errorCollection) {
		this.errorCollection = errorCollection;
	}
}
