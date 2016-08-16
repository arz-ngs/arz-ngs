package at.arz.ngs.ui.controllers;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;

@RequestScoped
@Named("newServiceInstance")
public class NewServiceInstanceController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	ServiceInstanceAdmin admin;

	public String addNewServiceInstance() {
		return "editNewServiceInstance";
	}
}
