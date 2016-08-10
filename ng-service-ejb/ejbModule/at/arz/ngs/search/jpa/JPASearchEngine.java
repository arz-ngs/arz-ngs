package at.arz.ngs.search.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.ServiceInstance;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class JPASearchEngine {

	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	protected JPASearchEngine() {
		// ejb constructor
	}

	/**
	 * Only for JUnit-Tests to use!!
	 * 
	 * @param entityManager
	 */
	public JPASearchEngine(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<ServiceInstance> findServiceInstances(	String serviceNameRegex,
														String envNameRegex,
														String hostNameRegex,
														String instanceNameRegex) {

		String query = "SELECT SI from ServiceInstance SI "
						+ "WHERE "
						+ "CAST(SI.service.serviceName VARCHAR(255)) LIKE :serviceNameRegex AND "
						+ "CAST(SI.environment.environmentName VARCHAR(255)) LIKE :envNameRegex AND "
						+ "CAST(SI.host.hostName VARCHAR(255)) LIKE :hostNameRegex AND "
						+ "CAST(SI.serviceInstanceName VARCHAR(255)) LIKE :instanceNameRegex";

		TypedQuery<ServiceInstance> getInstances = entityManager.createQuery(query, ServiceInstance.class);

		getInstances.setParameter("serviceNameRegex", serviceNameRegex.replace('*', '%'));
		getInstances.setParameter("envNameRegex", envNameRegex.replace('*', '%'));
		getInstances.setParameter("hostNameRegex", hostNameRegex.replace('*', '%'));
		getInstances.setParameter("instanceNameRegex", instanceNameRegex.replace('*', '%'));

		return getInstances.getResultList();
	}

	public List<ServiceInstance> orderByHostNameTest() {
		String query = "SELECT si FROM ServiceInstance si ORDER BY si.host.hostName ASC";

		TypedQuery<ServiceInstance> getInstances = entityManager.createQuery(query, ServiceInstance.class);

		return getInstances.getResultList();
	}
}
