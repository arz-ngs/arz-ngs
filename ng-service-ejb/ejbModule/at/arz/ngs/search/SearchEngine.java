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
		searchEngine = new JPASearchEngine(em);
	}

	public List<ServiceInstance> findServiceInstances(	String serviceNameRegex,
														String envNameRegex,
														String hostNameRegex,
														String instanceNameRegex) {
		return searchEngine.findServiceInstances(serviceNameRegex, envNameRegex, hostNameRegex, instanceNameRegex);
	}

	public List<ServiceInstance> orderByHostNameTest() {
		return searchEngine.orderByHostNameTest();
	}

	private static List<ServiceInstance> containingEnvironment(	List<ServiceInstance> toFilter,
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
	private static List<ServiceInstance> containingServiceInstance(	List<ServiceInstance> toFilter,
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
	private static List<ServiceInstance> containingHost(List<ServiceInstance> toFilter, String hostNameRegex) {
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
	private static List<ServiceInstance> containingService(List<ServiceInstance> toFilter, String serviceNameRegex) {
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
