package at.arz.ngs;

import java.util.List;

import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.Status;

public interface ServiceInstanceRepository {

	List<ServiceInstance> getAllInstances();

	ServiceInstance getServiceInstance(	ServiceInstanceName serviceInstanceName,
										Service service,
										Host host,
										Environment environment);

	List<ServiceInstance> getServiceInstances(Service service, Environment environment, Host host);

	List<ServiceInstance> getServiceInstances(Service service, Environment environment);

	List<ServiceInstance> getServiceInstances(Service service);

	/**
	 * All inserted paramenters MUST exist in DB before.
	 * @param newHost
	 * @param newService
	 * @param newEnvironment
	 * @param newScript
	 * @param newServiceInstanceName
	 * @param newStatus
	 */
	void addServiceInstance(Host newHost,
							Service newService,
							Environment newEnvironment,
							Script newScript,
							ServiceInstanceName newServiceInstanceName,
							Status newStatus,
							String information);

	void removeServiceInstance(ServiceInstance serviceInstance);

}
