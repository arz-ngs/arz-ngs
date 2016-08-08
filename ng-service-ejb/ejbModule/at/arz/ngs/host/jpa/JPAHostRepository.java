package at.arz.ngs.host.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.exception.HostNotFound;

@Stateless
@Local(HostRepository.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class JPAHostRepository
		implements HostRepository {

	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	public JPAHostRepository() {
		// ejb constructor
	}

	/**
	 * Only for JUnit-Tests to use!!
	 * 
	 * @param entityManager
	 */
	public JPAHostRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Host getHost(HostName hostName) {
		try {
			TypedQuery<Host> getHost = entityManager.createNamedQuery(Host.QUERY_BY_HOSTNAME, Host.class);
			getHost.setParameter("hname", hostName);

			return getHost.getSingleResult();
		} catch (NoResultException e) {
			throw new HostNotFound(hostName);
		}
	}

	@Override
	public List<Host> getAllHosts() {
		TypedQuery<Host> getAllHosts = entityManager.createNamedQuery(Host.QUERY_ALL, Host.class);
		return getAllHosts.getResultList();
	}

	@Override
	public void removeHost(Host host) {
		entityManager.remove(host);
	}

	@Override
	public void addHost(HostName hostName) {
		Host host = new Host(hostName);
		entityManager.persist(host);
	}
}
