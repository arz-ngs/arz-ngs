package at.arz.ngs.host.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.exception.HostNotFoundException;
import at.arz.ngs.api.exception.JPAException;

public class JPAHostRepository
		implements HostRepository {

	private EntityManager entityManager;

	public JPAHostRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Host getHost(HostName hostName) {
		entityManager.getTransaction().begin();
		try {
			TypedQuery<Host> getHost = entityManager.createNamedQuery("getHost", Host.class);
			getHost.setParameter("hname", hostName);
			Host result = getHost.getSingleResult();

			entityManager.getTransaction().commit();

			return result;

		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new HostNotFoundException();
		}
	}

	@Override
	public List<Host> getAllHosts() {
		entityManager.getTransaction().begin();
		try {
			TypedQuery<Host> getAllHosts = entityManager.createNamedQuery("getAllHosts", Host.class);
			List<Host> resultList = getAllHosts.getResultList();

			entityManager.getTransaction().commit();

			return resultList;

		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void removeHost(Host host) {
		entityManager.getTransaction().begin();
		try {
			entityManager.remove(host);

			entityManager.getTransaction().commit();

		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void addHost(HostName hostName) {
		entityManager.getTransaction().begin();
		try {
			Host host = new Host(hostName);
			entityManager.persist(host);

			entityManager.getTransaction().commit();

		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void updateHost(Host oldHost, HostName newHostName) {
		entityManager.getTransaction().begin();
		try {
			oldHost.setHostName(newHostName);

			entityManager.getTransaction().commit();

		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}
}
