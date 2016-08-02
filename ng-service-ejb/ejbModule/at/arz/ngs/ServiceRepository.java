package at.arz.ngs;

import java.util.List;

import at.arz.ngs.api.ServiceName;

public interface ServiceRepository {

	List<Service> getAllServices();

	Service getService(ServiceName serviceName);

	void addService(Service service);

	void removeService(Service service);

	void updateService(Service service);
}
