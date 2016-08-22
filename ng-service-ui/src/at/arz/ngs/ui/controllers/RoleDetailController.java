package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.security.SecurityAdmin;
import at.arz.ngs.security.permission.commands.PermissionData;

@RequestScoped
@Named("roleDetail")
public class RoleDetailController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	SecurityAdmin admin;

	List<PermissionData> permissions;

	public String goToRoleDetail(String role) {
		permissions = admin.getPermissions(role).getPermissions();
		return "userdetail";
	}
}
