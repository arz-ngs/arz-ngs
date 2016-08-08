package at.arz.ngs.serviceinstance.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
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
import at.arz.ngs.api.exception.JPAException;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class JPAServiceInstanceRepository
		implements ServiceInstanceRepository {

	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	public JPAServiceInstanceRepository() {
		// ejb constructor
	}

	JPAServiceInstanceRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<ServiceInstance> getAllInstances() {
		entityManager.getTransaction().begin();
		try {
			TypedQuery<ServiceInstance> getAllInstances =
														entityManager.createNamedQuery(	ServiceInstance.QUERY_ALL,
																							ServiceInstance.class);
			List<ServiceInstance> resultList = getAllInstances.getResultList();

			entityManager.getTransaction().commit();

			return resultList;
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public ServiceInstance getServiceInstance(	ServiceInstanceName serviceInstanceName,
												Service service,
												Host host,
												Environment environment) {

		entityManager.getTransaction().begin();
		try {
			TypedQuery<ServiceInstance> getInstance = entityManager.createNamedQuery(	"getServiceInstance2",
																							ServiceInstance.class);
			getInstance.setParameter("siname", serviceInstanceName);
			getInstance.setParameter("host", host);
			getInstance.setParameter("service", service);
			getInstance.setParameter("environment", environment);
			ServiceInstance result = getInstance.getSingleResult();

			entityManager.getTransaction().commit();

			return result;
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public List<ServiceInstance> getServiceInstances(Service service, Environment environment, Host host) {
		entityManager.getTransaction().begin();
		try {
			TypedQuery<ServiceInstance> getInstances = entityManager.createNamedQuery(	"getInstances/service/environment/host",
																						ServiceInstance.class);
			getInstances.setParameter("host", host);
			getInstances.setParameter("service", service);
			getInstances.setParameter("environment", environment);
			List<ServiceInstance> resultList = getInstances.getResultList();

			entityManager.getTransaction().commit();

			return resultList;
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public List<ServiceInstance> getServiceInstances(Service service, Environment environment) {
		entityManager.getTransaction().begin();
		try {
			TypedQuery<ServiceInstance> getInstances = entityManager.createNamedQuery(	"getInstances/service/environment",
																						ServiceInstance.class);
			getInstances.setParameter("service", service);
			getInstances.setParameter("environment", environment);
			List<ServiceInstance> resultList = getInstances.getResultList();

			entityManager.getTransaction().commit();

			return resultList;
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public List<ServiceInstance> getServiceInstances(Service service) {
		entityManager.getTransaction().begin();
		try {
			TypedQuery<ServiceInstance> getInstances = entityManager.createNamedQuery(	"getInstances/service",
																						ServiceInstance.class);
			getInstances.setParameter("service", service);
			List<ServiceInstance> resultList = getInstances.getResultList();

			entityManager.getTransaction().commit();

			return resultList;
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void addServiceInstance(	Host host,
									Service service,
									Environment environment,
									Script script,
									ServiceInstanceName serviceInstanceName,
									Status status) {

		entityManager.getTransaction().begin();
		try {
			ServiceInstance instance = new ServiceInstance(	serviceInstanceName,
															service,
															host,
															environment,
															script,
															status);

			entityManager.persist(instance);

			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void removeServiceInstance(ServiceInstance serviceInstance) {
		entityManager.getTransaction().begin();
		try {
			entityManager.remove(serviceInstance);

			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void updateServiceInstance(	ServiceInstance serviceInstanceOld,
										Host newHost,
										Service newService,
										Environment newEnvironment,
										Script newScript,
										ServiceInstanceName newServiceInstanceName,
										Status newStatus) {

		entityManager.getTransaction().begin();
		try {
			serviceInstanceOld.setStatus(newStatus);
			serviceInstanceOld.setHost(newHost);
			serviceInstanceOld.setService(newService);
			serviceInstanceOld.setEnvironment(newEnvironment);
			serviceInstanceOld.setScript(newScript);
			serviceInstanceOld.setServiceInstanceName(newServiceInstanceName);
			serviceInstanceOld.setStatus(newStatus);

			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void updateStatus(ServiceInstance serviceInstance, Status newStatus) {
		entityManager.getTransaction().begin();
		try {
			serviceInstance.setStatus(newStatus);

			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}
}
