package at.arz.ngs.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.Service;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.ServiceName;

@Stateless
public class ServiceAdmin {

	@Inject
	private ServiceRepository services;

	public void renameService(ServiceName oldName, ServiceName newName) {
		Service service = services.getService(oldName);
		service.renameService(newName);
	}

}
