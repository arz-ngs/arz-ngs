package at.arz.ngs.service.jpa;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.Service;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.exception.ServiceNotFound;

@Dependent
public class JPAServiceRepository
		implements ServiceRepository {

	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	public JPAServiceRepository() {
		// ejb constructor
	}

	/**
	 * Only for JUnit-Tests to use!!
	 * 
	 * @param entityManager
	 */
	public JPAServiceRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Service getService(ServiceName serviceName) {
		try {
			TypedQuery<Service> getService = entityManager.createNamedQuery(Service.QUERY_BY_SERVICENAME,
																			Service.class);
			getService.setParameter("sname", serviceName);

			return getService.getSingleResult();

		} catch (NoResultException e) {
			throw new ServiceNotFound(serviceName);
		}
	}

	@Override
	public List<Service> getAllServices() {
		TypedQuery<Service> allServices = entityManager.createNamedQuery(Service.QUERY_ALL, Service.class);
		return allServices.getResultList();
	}

	@Override
	public void addService(ServiceName name) {
		Service service = new Service(name);
		entityManager.persist(service);
	}

	@Override
	public void removeService(Service service) {
		entityManager.remove(service);
	}
}
