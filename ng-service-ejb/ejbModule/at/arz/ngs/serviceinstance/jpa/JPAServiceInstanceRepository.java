package at.arz.ngs.serviceinstance.jpa;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.Environment;
import at.arz.ngs.Host;
import at.arz.ngs.Script;
import at.arz.ngs.Service;
import at.arz.ngs.ServiceInstance;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.Status;
import at.arz.ngs.api.exception.ServiceInstanceNotFound;

@Dependent
public class JPAServiceInstanceRepository
		implements ServiceInstanceRepository {

	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	public JPAServiceInstanceRepository() {
		// ejb constructor
	}

	/**
	 * Only for JUnit-Tests to use!!
	 * 
	 * @param entityManager
	 */
	public JPAServiceInstanceRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<ServiceInstance> getAllInstances() {
			TypedQuery<ServiceInstance> getAllInstances =
														entityManager.createNamedQuery(	ServiceInstance.QUERY_ALL,
																							ServiceInstance.class);
		return getAllInstances.getResultList();
	}

	@Override
	public ServiceInstance getServiceInstance(	ServiceInstanceName serviceInstanceName,
												Service service,
												Host host,
												Environment environment) {

		try {
			TypedQuery<ServiceInstance> getInstance =
													entityManager.createNamedQuery(	ServiceInstance.QUERY_BY_SERVICE_ENVIRONMENT_HOST_SERVICEINSTANCENAME,
																							ServiceInstance.class);
			getInstance.setParameter("siname", serviceInstanceName);
			getInstance.setParameter("host", host);
			getInstance.setParameter("service", service);
			getInstance.setParameter("environment", environment);

			return getInstance.getSingleResult();
		} catch (NoResultException e) {
			throw new ServiceInstanceNotFound(	serviceInstanceName,
												service.getServiceName(),
												host.getHostName(),
												environment.getEnvironmentName());
		}
	}

	@Override
	public List<ServiceInstance> getServiceInstances(Service service, Environment environment, Host host) {
		TypedQuery<ServiceInstance> getInstances = entityManager.createNamedQuery(	ServiceInstance.QUERY_BY_SERVICE_ENVIRONMENT_HOST,
																						ServiceInstance.class);

		getInstances.setParameter("host", host);
		getInstances.setParameter("service", service);
		getInstances.setParameter("environment", environment);
		return getInstances.getResultList();
	}

	@Override
	public List<ServiceInstance> getServiceInstances(Service service, Environment environment) {
		TypedQuery<ServiceInstance> getInstances = entityManager.createNamedQuery(	ServiceInstance.QUERY_BY_SERVICE_ENVIRONMENT,
																						ServiceInstance.class);

		getInstances.setParameter("service", service);
		getInstances.setParameter("environment", environment);
		return getInstances.getResultList();
	}

	@Override
	public List<ServiceInstance> getServiceInstances(Service service) {
		TypedQuery<ServiceInstance> getInstances = entityManager.createNamedQuery(	ServiceInstance.QUERY_BY_SERVICE,
																						ServiceInstance.class);
			getInstances.setParameter("service", service);
		return getInstances.getResultList();
	}

	@Override
	public void addServiceInstance(	Host host,
									Service service,
									Environment environment,
									Script script,
									ServiceInstanceName serviceInstanceName,
									Status status,
									String information) {

			ServiceInstance instance = new ServiceInstance(	serviceInstanceName,
															service,
															host,
															environment,
															script,
															status);
		instance.setInformation(information);
			entityManager.persist(instance);
	}

	@Override
	public void removeServiceInstance(ServiceInstance serviceInstance) {
			entityManager.remove(serviceInstance);
	}
}
