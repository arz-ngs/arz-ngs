package at.arz.ngs.service.jpa;

import java.util.List;

import at.arz.ngs.Service;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.ServiceName;

public class JPAServiceRepository
		implements ServiceRepository {

	@Override
	public Service getService(ServiceName serviceName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Service> getAllServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addService(ServiceName name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeService(Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateService(Service oldService, ServiceName newServiceName) {
		// TODO Auto-generated method stub

	}
}
