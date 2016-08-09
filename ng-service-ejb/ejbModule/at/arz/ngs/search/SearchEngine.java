package at.arz.ngs.search;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;

import at.arz.ngs.ServiceInstance;

@Stateless
public class SearchEngine {

	public static List<ServiceInstance> containingEnvironment(	List<ServiceInstance> toFilter,
																String environmentNameRegex) {
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
		List<ServiceInstance> res = new LinkedList<ServiceInstance>();
		for (ServiceInstance si : toFilter) {
			if (si.getService().getServiceName().getName().matches(serviceNameRegex)) {
				res.add(si);
			}
		}
		return res;
	}
}
