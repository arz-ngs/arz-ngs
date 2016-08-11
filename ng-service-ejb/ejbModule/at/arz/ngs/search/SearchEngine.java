package at.arz.ngs.search;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import at.arz.ngs.ServiceInstance;
import at.arz.ngs.search.jpa.JPASearchEngine;

@Stateless
public class SearchEngine {

	@Inject
	private JPASearchEngine searchEngine;

	public SearchEngine() {
	}

	/**
	 * Only for JUnit Tests!!!
	 * 
	 * @param em
	 */
	public SearchEngine(EntityManager em) {
		searchEngine = new JPASearchEngine(em, true);
	}

	public List<ServiceInstance> findServiceInstances(	String serviceNameRegex,
														String envNameRegex,
														String hostNameRegex,
														String instanceNameRegex) {
		return searchEngine.findServiceInstances(serviceNameRegex, envNameRegex, hostNameRegex, instanceNameRegex);
	}

	public List<ServiceInstance> findServiceInstances(	String serviceNameRegex,
														String envNameRegex,
														String hostNameRegex,
														String instanceNameRegex,
														OrderCondition condition) {
		
		return searchEngine.findServiceInstances(	serviceNameRegex,
													envNameRegex,
													hostNameRegex,
													instanceNameRegex,
													getOrderByField(condition),
													getOrder(condition)); 
	}
	
	public List<ServiceInstance> findServiceInstances(	String serviceNameRegex,
														String envNameRegex,
														String hostNameRegex,
														String instanceNameRegex,
														OrderCondition orderCondition,
														PaginationCondition paginationCondition) {
		
		return searchEngine.findServiceInstances(	serviceNameRegex,
													envNameRegex,
													hostNameRegex,
													instanceNameRegex,
													getOrderByField(orderCondition),
													getOrder(orderCondition),
													getElementsPerPage(paginationCondition),
													getFirstElement(paginationCondition));
	}
	
	private int getElementsPerPage(PaginationCondition condition) {
		if (condition.getElementsPerPage() < 1) {
			condition.setElementsPerPage(50); // default show 50
		}
		return condition.getElementsPerPage();
	}

	private int getFirstElement(PaginationCondition condition) {
		if (condition.getCurrentPage() < 1) {
			condition.setCurrentPage(1); // default page 1
		}
		int currentFirstElement = (condition.getCurrentPage() - 1) * condition.getElementsPerPage();
		return currentFirstElement;
	}

	private String getOrderByField(OrderCondition condition) {
		if (condition == null) {
			condition = new OrderCondition();
		}
		if (condition.getOrderByField() == null) {
			condition.setOrderByField(OrderCondition.ORDERBY_SERVICEINSTANCE);
		}
		switch (condition.getOrderByField()) {
			case OrderCondition.ORDERBY_SERVICE:
				return "service.serviceName";
			case OrderCondition.ORDERBY_ENVIRONMENT:
				return "environment.environmentName";
			case OrderCondition.ORDERBY_HOST:
				return "host.hostName";
			case OrderCondition.ORDERBY_SERVICEINSTANCE:
				return "serviceInstanceName";
			default:
				return "serviceInstanceName";
		}
	}

	private String getOrder(OrderCondition condition) {
		if (condition == null) {
			condition = new OrderCondition();
		}
		if (condition.getOrder() == null) {
			condition.setOrder(OrderCondition.ASCENDING);
		}
		switch (condition.getOrder()) {
			case OrderCondition.ASCENDING:
				return "ASC";
			case OrderCondition.DESCENDING:
				return "DESC";
			default:
				return "ASC";
		}
	}

	public List<ServiceInstance> orderByHostNameTest() {
		return searchEngine.orderByHostNameTest();
	}

	public static List<ServiceInstance> containingEnvironment(	List<ServiceInstance> toFilter,
																String environmentNameRegex) {
		if (environmentNameRegex.equals("*")) {
			return toFilter;
		}
		if (environmentNameRegex.equals("")) {
			return Collections.emptyList();
		}

		List<ServiceInstance> res = new LinkedList<ServiceInstance>();
		for (ServiceInstance si : toFilter) {
			if (si.getEnvironment().getEnvironmentName().getName().matches(environmentNameRegex)) {
				res.add(si);
			}
		}
		return res;
	}

	/**
	 * Filters a List. Return a List only with Elements which regex matches the targeted entry.
	 */
	public static List<ServiceInstance> containingServiceInstance(	List<ServiceInstance> toFilter,
																	String serviceInstanceNameRegex) {
		if (serviceInstanceNameRegex.equals("*")) {
			return toFilter;
		}
		if (serviceInstanceNameRegex.equals("")) {
			return Collections.emptyList();
		}

		List<ServiceInstance> res = new LinkedList<ServiceInstance>();
		for (ServiceInstance si : toFilter) {
			if (si.getServiceInstanceName().getName().matches(serviceInstanceNameRegex)) {
				res.add(si);
			}
		}
		return res;
	}

	/**
	 * Filters a List. Return a List only with Elements which regex matches the targeted entry.
	 */
	public static List<ServiceInstance> containingHost(List<ServiceInstance> toFilter, String hostNameRegex) {
		if (hostNameRegex.equals("*")) {
			return toFilter;
		}
		if (hostNameRegex.equals("")) {
			return Collections.emptyList();
		}

		List<ServiceInstance> res = new LinkedList<ServiceInstance>();
		for (ServiceInstance si : toFilter) {
			if (si.getHost().getHostName().getName().matches(hostNameRegex)) {
				res.add(si);
			}
		}
		return res;
	}

	/**
	 * Filters a List. Return a List only with Elements which regex matches the targeted entry.
	 */
	public static List<ServiceInstance> containingService(List<ServiceInstance> toFilter, String serviceNameRegex) {
		if (serviceNameRegex.equals("*")) {
			return toFilter;
		}
		if (serviceNameRegex.equals("")) {
			return Collections.emptyList();
		}

		List<ServiceInstance> res = new LinkedList<ServiceInstance>();
		for (ServiceInstance si : toFilter) {
			if (si.getService().getServiceName().getName().matches(serviceNameRegex)) {
				res.add(si);
			}
		}
		return res;
	}
}
