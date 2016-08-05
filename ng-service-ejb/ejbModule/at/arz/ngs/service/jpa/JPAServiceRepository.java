package at.arz.ngs.service.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.arz.ngs.Service;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.exception.JPAException;
import at.arz.ngs.api.exception.ServiceNotFoundException;

public class JPAServiceRepository
		implements ServiceRepository {

	private EntityManager entityManager;

	public JPAServiceRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Service getService(ServiceName serviceName) {
		entityManager.getTransaction().begin();
		try {
			TypedQuery<Service> getService = entityManager.createNamedQuery("getService", Service.class);
			getService.setParameter("sname", serviceName);
			Service result = getService.getSingleResult();

			entityManager.getTransaction().commit();

			return result;

		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new ServiceNotFoundException();
		}
	}

	@Override
	public List<Service> getAllServices() {
		entityManager.getTransaction().begin();
		try {
			TypedQuery<Service> allServices = entityManager.createNamedQuery("getAllServices", Service.class);
			List<Service> resultList = allServices.getResultList();

			entityManager.getTransaction().commit();

			return resultList;

		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void addService(ServiceName name) {
		entityManager.getTransaction().begin();
		try {
			Service service = new Service(name);
			entityManager.persist(service);

			entityManager.getTransaction().commit();

		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void removeService(Service service) {
		entityManager.getTransaction().begin();
		try {
			entityManager.remove(service);

			entityManager.getTransaction().commit();

		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void updateService(Service oldService, ServiceName newServiceName) {
		entityManager.getTransaction().begin();
		try {
			oldService.setServiceName(newServiceName);

			entityManager.getTransaction().commit();

		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}
}
