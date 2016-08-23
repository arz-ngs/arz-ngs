package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.role.commands.create.CreateRole;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;

@RequestScoped
@Named("roleoverview")
public class RoleOverviewController implements Serializable{

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

	}

	public String goToRoleOverview() {
		roleOverview = admin.getAllRoles().getRoles();
		return "roleoverview";
	}

	public String addRole(String newRole) {
		errorCollection = new ErrorCollection();
		try {
			admin.createRole(userController.getCurrentActor(), new CreateRole(newRole));
		} catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
		}
		return "roleoverview.xhtml?faces-redirct=true";
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
