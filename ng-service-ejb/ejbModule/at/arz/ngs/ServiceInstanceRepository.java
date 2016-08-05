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

	void addServiceInstance(Host newHost,
							Service newService,
							Environment newEnvironment,
							Script newScript,
							ServiceInstanceName newServiceInstanceName,
							Status newStatus);

	void removeServiceInstance(ServiceInstance serviceInstance);

	/**
	 * All inserted paramenters MUST exist in DB before.
	 * 
	 * @param serviceInstanceOld
	 * @param newHost
	 * @param newService
	 * @param newEnvironment
	 * @param newScript
	 * @param newServiceInstanceName
	 * @param newStatus
	 */
	void updateServiceInstance(	ServiceInstance serviceInstanceOld,
								Host newHost,
								Service newService,
								Environment newEnvironment,
								Script newScript,
								ServiceInstanceName newServiceInstanceName,
								Status newStatus);

	void updateStatus(ServiceInstance serviceInstance, Status newStatus);
}
