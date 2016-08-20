package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named("role")
public class RoleController implements Serializable{

	private static final long serialVersionUID = 1L;	

	
	public String goToRoleOverview() {
		return "roleoverview";
	}
}
