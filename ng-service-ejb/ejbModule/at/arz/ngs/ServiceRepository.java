package at.arz.ngs;

import java.util.List;

import at.arz.ngs.api.ServiceName;

public interface ServiceRepository {
	Service getService(ServiceName serviceName);
	
	List<Service> getAllServices();

	void addService(ServiceName name);

	void removeService(Service service);

	void updateService(Service oldService, ServiceName newServiceName);
}
