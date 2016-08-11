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

	/**
	 * default is false. Is used to change in the queries from CHAR to VARCHAR (CHAR is needed for MySQL, VARCHAR for
	 * Derby)
	 */
	private boolean isUnitTestRun = false;

	protected JPASearchEngine() {
		// ejb constructor
	}

	/**
	 * Only for JUnit-Tests to use!!
	 * 
	 * @param entityManager
	 */
	public JPASearchEngine(EntityManager entityManager, boolean isUnitTestRun) {
		this.entityManager = entityManager;
		this.isUnitTestRun = isUnitTestRun;
	}

	public List<ServiceInstance> findServiceInstances(	String serviceNameRegex,
														String envNameRegex,
														String hostNameRegex,
														String instanceNameRegex) {

		String query = "SELECT SI from ServiceInstance SI "
						+ "WHERE "
						+ "CAST(SI.service.serviceName CHAR(255)) LIKE :serviceNameRegex AND "
						+ "CAST(SI.environment.environmentName CHAR(255)) LIKE :envNameRegex AND "
						+ "CAST(SI.host.hostName CHAR(255)) LIKE :hostNameRegex AND "
						+ "CAST(SI.serviceInstanceName CHAR(255)) LIKE :instanceNameRegex";

		if (isUnitTestRun) {
			query = query.replace("CHAR", "VARCHAR");
		}

		TypedQuery<ServiceInstance> getInstances = entityManager.createQuery(query, ServiceInstance.class);

		getInstances.setParameter("serviceNameRegex", serviceNameRegex.replace('*', '%'));
		getInstances.setParameter("envNameRegex", envNameRegex.replace('*', '%'));
		getInstances.setParameter("hostNameRegex", hostNameRegex.replace('*', '%'));
		getInstances.setParameter("instanceNameRegex", instanceNameRegex.replace('*', '%'));

		return getInstances.getResultList();
	}

	public List<ServiceInstance> findServiceInstances(	String serviceNameRegex,
														String envNameRegex,
														String hostNameRegex,
														String instanceNameRegex,
														String orderByField,
														String order) {

		String query = "SELECT SI from ServiceInstance SI "+ "WHERE "
						+ "CAST(SI.service.serviceName CHAR(255)) LIKE :serviceNameRegex AND "
						+ "CAST(SI.environment.environmentName CHAR(255)) LIKE :envNameRegex AND "
						+ "CAST(SI.host.hostName CHAR(255)) LIKE :hostNameRegex AND "
						+ "CAST(SI.serviceInstanceName CHAR(255)) LIKE :instanceNameRegex "
						+ "ORDER BY SI."
						+ orderByField
						+ " "
						+ order;

		if (isUnitTestRun) {
			query = query.replace("CHAR", "VARCHAR");
		}

		TypedQuery<ServiceInstance> getInstances = entityManager.createQuery(query, ServiceInstance.class);

		getInstances.setParameter("serviceNameRegex", serviceNameRegex.replace('*', '%'));
		getInstances.setParameter("envNameRegex", envNameRegex.replace('*', '%'));
		getInstances.setParameter("hostNameRegex", hostNameRegex.replace('*', '%'));
		getInstances.setParameter("instanceNameRegex", instanceNameRegex.replace('*', '%'));

		return getInstances.getResultList();
	}

	public List<ServiceInstance> findServiceInstances(	String serviceNameRegex,
														String envNameRegex,
														String hostNameRegex,
														String instanceNameRegex,
														String orderByField,
														String order,
														int elementsPerPage,
														int startByElement) {

		String query = "SELECT SI from ServiceInstance SI "+ "WHERE "
						+ "CAST(SI.service.serviceName CHAR(255)) LIKE :serviceNameRegex AND "
						+ "CAST(SI.environment.environmentName CHAR(255)) LIKE :envNameRegex AND "
						+ "CAST(SI.host.hostName CHAR(255)) LIKE :hostNameRegex AND "
						+ "CAST(SI.serviceInstanceName CHAR(255)) LIKE :instanceNameRegex "
						+ "ORDER BY SI."
						+ orderByField
						+ " "
						+ order;

		if (isUnitTestRun) {
			query = query.replace("CHAR", "VARCHAR");
		}

		TypedQuery<ServiceInstance> getInstances = entityManager.createQuery(query, ServiceInstance.class);

		getInstances.setParameter("serviceNameRegex", serviceNameRegex.replace('*', '%'));
		getInstances.setParameter("envNameRegex", envNameRegex.replace('*', '%'));
		getInstances.setParameter("hostNameRegex", hostNameRegex.replace('*', '%'));
		getInstances.setParameter("instanceNameRegex", instanceNameRegex.replace('*', '%'));

		getInstances.setMaxResults(elementsPerPage);
		getInstances.setFirstResult(startByElement);

		return getInstances.getResultList();
	}

	public List<ServiceInstance> orderByHostNameTest() {
		String query = "SELECT si FROM ServiceInstance si ORDER BY si.host.hostName ASC";

		TypedQuery<ServiceInstance> getInstances = entityManager.createQuery(query, ServiceInstance.class);

		return getInstances.getResultList();
	}
}
