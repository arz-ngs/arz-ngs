package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.SecurityAdmin;

@RequestScoped
@Named("roleoverview")
public class RoleOverviewController implements Serializable{

	private static final long serialVersionUID = 1L;	

	@Inject
	private SecurityAdmin admin;

	private List<String> roleOverview;
	
	private String createRoleName;

	@PostConstruct
	public void init() {

	}

	public String goToRoleOverview() {
		roleOverview = admin.getAllRoles().getRoles();
		return "roleoverview";
	}

	public String addRole(String newRole) {
		// try {
		// admin.creatRole(newRole);
		// } catch (RuntimeException e) {
		//
		// }
		return "";
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
}
