package at.arz.ngs.search;

import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;

import at.arz.ngs.Service;

@Stateless
public class SearchEngine {

	/**
	 * If one parameter is null or an empty String, the value is expected to be a "*"
	 * 
	 * @param serviceRegex
	 * @param serverRegex
	 * @return
	 */
	public List<Service> findServices(String serviceRegex, String serverRegex) {
		return Collections.emptyList();
	}
}
