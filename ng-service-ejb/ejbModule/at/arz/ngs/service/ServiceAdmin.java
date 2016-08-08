package at.arz.ngs.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.Service;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.ServiceName;

@Stateless
public class ServiceAdmin {

	@Inject
	private ServiceRepository repository;

	public Service getService(ServiceName serviceName) {
		return repository.getService(serviceName);
	}

	public List<Service> getAllServices() {
		return repository.getAllServices();
	}

	public void addService(ServiceName serviceName) {
		repository.addService(serviceName);
	}

	public void removeService(ServiceName serviceName) {
		Service toRemove = repository.getService(serviceName);
		repository.removeService(toRemove);
	}

	public void renameService(ServiceName oldName, ServiceName newName) {
		Service service = repository.getService(oldName);
		service.renameService(newName);
	}
}
