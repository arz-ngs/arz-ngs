package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.search.OrderCondition;
import at.arz.ngs.search.PaginationCondition;
import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverview;

@ViewScoped
@Named("serviceinstance")
public class ServiceInstanceController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ServiceInstanceAdmin service;

	private List<ServiceInstanceOverview> instances;

	private PaginationCondition pagination;
	private OrderCondition order;

	@PostConstruct
	public void init() {
		pagination = new PaginationCondition(50, 1); // default is first page with 50 elements
		order = new OrderCondition(OrderCondition.ORDERBY_SERVICEINSTANCE, OrderCondition.ASCENDING);
		instances = service.getServiceInstances("*", "*", "*", "*", order, pagination).getServiceInstances();
	}

	public List<ServiceInstanceOverview> getInstances() {
		return instances;
	}

	public String addNewServiceInstance() {
		// is able to be invoked!!
		return null;
	}
}