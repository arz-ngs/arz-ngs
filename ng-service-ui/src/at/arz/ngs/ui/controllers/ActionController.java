package at.arz.ngs.ui.controllers;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.serviceinstance.commands.action.PerformAction;

@RequestScoped
@Named("action")
public class ActionController {
	
	@Inject
	private ServiceInstanceAdmin service;
	
	
	public void start() {
		PerformAction action = new PerformAction();
		action.setPerformAction("start");
		service.performAction("arctis", "pebk123", "ln003", "arctis_1", action);
	}
	
	public void stop() {
		PerformAction action = new PerformAction();
		action.setPerformAction("stop");
		service.performAction("arctis", "pebk123", "ln003", "arctis_1", action);
	}
	
	public void restart() {
		PerformAction action = new PerformAction();
		action.setPerformAction("restart");
		service.performAction("arctis", "pebk123", "ln003", "arctis_1", action);
	}
}
