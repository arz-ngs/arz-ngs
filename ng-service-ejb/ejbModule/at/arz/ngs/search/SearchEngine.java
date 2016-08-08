package at.arz.ngs.search;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;

import at.arz.ngs.ServiceInstance;

@Stateless
public class SearchEngine {

	/**
	 * If one parameter is null or an empty String, the value is expected to be a "*"
	 * 
	 * @param serviceRegex
	 * @param serverRegex
	 * @return
	 */
	public List<ServiceInstance> findServices(String serviceRegex, String serverRegex) {
		return Collections.emptyList();
	}

	/**
	 * Filters a List. Return a List only with Elements which regex matches the targeted entry.
	 * 
	 * @param toFilter
	 * @param environmentName
	 * @return
	 */
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
	 * 
	 * @param toFilter
	 * @param environmentName
	 * @return
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
	 * 
	 * @param toFilter
	 * @param environmentName
	 * @return
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
	 * 
	 * @param toFilter
	 * @param environmentName
	 * @return
	 */
	public static List<ServiceInstance> containingService(List<ServiceInstance> toFilter, String serviceNameRegex) {
		List<ServiceInstance> res = new LinkedList<ServiceInstance>();
		for (ServiceInstance si : toFilter) {
			if (si.getService().renameService().getName().matches(serviceNameRegex)) {
				res.add(si);
			}
		}
		return res;
	}
}
