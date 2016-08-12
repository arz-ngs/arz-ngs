package at.arz.ngs.ui.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverview;

@RequestScoped
@Named("serviceinstance")
public class ServiceInstanceController {

	@Inject
	private ServiceInstanceAdmin service;

	private List<ServiceInstanceOverview> instances;

	@PostConstruct
	public void init() {
		instances = service.getServiceInstances("*", "*", "*", "*").getServiceInstances();
	}

	public List<ServiceInstanceOverview> getInstances() {
		return instances;
	}
}