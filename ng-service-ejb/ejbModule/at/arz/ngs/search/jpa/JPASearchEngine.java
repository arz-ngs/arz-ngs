package at.arz.ngs.search.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.ServiceInstance;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;

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
		
		ServiceName serviceRegex = new ServiceName(serviceNameRegex);
		EnvironmentName environmentRegex = new EnvironmentName(envNameRegex);
		HostName hostRegex = new HostName(hostNameRegex);
		ServiceInstanceName instanceRegex = new ServiceInstanceName(instanceNameRegex);

		String query = "SELECT SI.serviceInstanceName a, SI.host.hostName b, SI.environment.environmentName c, "
						+ "SI.service.serviceName d from ServiceInstance SI WHERE d LIKE :serviceNameRegex AND "
						+ "c LIKE :envNameRegex AND "
						+ "b LIKE :hostNameRegex AND "
						+ "a LIKE :instanceNameRegex";

		// String query = "SELECT si FROM ServiceInstance si WHERE si.service.serviceName LIKE :serviceNameRegex AND "
		// + "si.environment.environmentName LIKE :envNameRegex AND "
		// + "si.host.hostName LIKE :hostNameRegex AND "
		// + "si.serviceInstanceName LIKE :instanceNameRegex";


		TypedQuery<ServiceInstance> getInstances = entityManager.createQuery(query, ServiceInstance.class);

		getInstances.setParameter("serviceNameRegex", serviceRegex);
		getInstances.setParameter("envNameRegex", environmentRegex);
		getInstances.setParameter("hostNameRegex", hostRegex);
		getInstances.setParameter("instanceNameRegex", instanceRegex);

		// String query = "SELECT si FROM ServiceInstance si WHERE si.service.serviceName LIKE :serviceNameRegex AND "
		// + "si.environment.environmentName LIKE :envNameRegex AND "
		// + "si.host.hostName LIKE :hostNameRegex AND "
		// + "si.serviceInstanceName LIKE :instanceNameRegex";
		//
		// TypedQuery<ServiceInstance> getInstances = entityManager.createQuery(query, ServiceInstance.class);
		//
		// getInstances.setParameter("serviceNameRegex", serviceNameRegex);
		// getInstances.setParameter("envNameRegex", envNameRegex);
		// getInstances.setParameter("hostNameRegex", hostNameRegex);
		// getInstances.setParameter("instanceNameRegex", instanceNameRegex);

		return getInstances.getResultList();

		// CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		// CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
		//
		// Root instanceRoot = criteriaQuery.from(ServiceInstance.class);
		//
		// Predicate predicate = criteriaBuilder.like(instanceRoot.<String> get("host.hostName"), hostNameRegex);
		// criteriaQuery.where(predicate);
		//
		// criteriaQuery.select(instanceRoot);
		// TypedQuery query = entityManager.createQuery(criteriaQuery);
		// return query.getResultList();
	}

	public List<ServiceInstance> orderByHostNameTest() {
		String query = "SELECT si FROM ServiceInstance si ORDER BY si.host.hostName ASC";

		TypedQuery<ServiceInstance> getInstances = entityManager.createQuery(query, ServiceInstance.class);

		return getInstances.getResultList();
	}
}
