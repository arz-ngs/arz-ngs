package at.arz.ngs.serviceinstance.jpa;

import java.util.List;

import at.arz.ngs.Environment;
import at.arz.ngs.Host;
import at.arz.ngs.Script;
import at.arz.ngs.Service;
import at.arz.ngs.ServiceInstance;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.Status;

public class JPAServiceInstanceRepository
		implements ServiceInstanceRepository {

	@Override
	public List<ServiceInstance> getAllInstances() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceInstance getServiceInstance(	ServiceInstance serviceInstance,
												Service service,
												Host host,
												Environment environment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServiceInstance> getServiceInstances(Service service, Host host, Environment environment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServiceInstance> getServiceInstances(Service service, Environment environment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServiceInstance> getServiceInstances(Service service) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addServiceInstance(ServiceInstance serviceInstance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeServiceInstance(ServiceInstance serviceInstance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateServiceInstance(	ServiceInstance serviceInstanceOld,
										Host newHost,
										Service newService,
										Environment newEnvironment,
										Script newScript,
										ServiceInstanceName newServiceInstanceName,
										Status newStatus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateStatus(ServiceInstance serviceInstance, Status newStatus) {
		// TODO Auto-generated method stub

	}
}
