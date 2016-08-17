package at.arz.ngs.ui.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.ui.data_collections.OverviewCollection;

@RequestScoped
@Named("actionController")
public class ActionController {
	
	@Inject
	private ServiceInstanceAdmin service;
	
	@PostConstruct
	public void init() {
	}
	
	public void start(List<OverviewCollection> overviewList) {
		for (OverviewCollection oc : overviewList) {
			if (oc.isChecked()) {
				System.out.println("start service instance: " + oc.getServiceInstance().toString());
				// invoke admin to perform action + exception handling
			}
			oc.setChecked(false); // set default, no checkbox checked
		}
	}
	
	public void stop(List<OverviewCollection> overviewList) {
		for (OverviewCollection oc : overviewList) {
			if (oc.isChecked()) {
				System.out.println("stop service instance: " + oc.getServiceInstance().toString());
				// invoke admin to perform action + exception handling
			}
			oc.setChecked(false); // set default, no checkbox checked
		}
	}
	
	public void restart(List<OverviewCollection> overviewList) {
		for (OverviewCollection oc : overviewList) {
			if (oc.isChecked()) {
				System.out.println("restart service instance: " + oc.getServiceInstance().toString());
				// invoke admin to perform action + exception handling
			}
			oc.setChecked(false); // set default, no checkbox checked
		}
	}

	public void status(List<OverviewCollection> overviewList) {
		for (OverviewCollection oc : overviewList) {
			if (oc.isChecked()) {
				System.out.println("get status for service instance: " + oc.getServiceInstance().toString());
				// invoke admin to perform action + exception handling
			}
			oc.setChecked(false); // set default, no checkbox checked
		}
	}
}
