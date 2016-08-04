package at.arz.ngs.serviceinstance.jpa;

import java.util.List;

import at.arz.ngs.ServiceInstance;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.api.ServiceInstanceName;

public class JPAServiceInstanceRepository
		implements ServiceInstanceRepository {

	@Override
	public List<ServiceInstance> getAllServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceInstance getService(ServiceInstanceName serviceName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addService(ServiceInstance service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeService(ServiceInstance service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateService(ServiceInstance service) {
		// TODO Auto-generated method stub

	}

}
