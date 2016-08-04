package at.arz.ngs;

import java.util.List;

import at.arz.ngs.api.ServiceInstanceName;

public interface ServiceInstanceRepository {

	List<ServiceInstance> getAllServices();

	ServiceInstance getService(ServiceInstanceName serviceName);

	void addService(ServiceInstance service);

	void removeService(ServiceInstance service);

	void updateService(ServiceInstance service);
}
