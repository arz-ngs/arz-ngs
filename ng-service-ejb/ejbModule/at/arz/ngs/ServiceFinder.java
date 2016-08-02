package at.arz.ngs;

import java.util.List;

public interface ServiceFinder {

	/**
	 * If one parameter is null or an empty String, the value is expected to be a "*"
	 * 
	 * @param serviceRegex
	 * @param serverRegex
	 * @return
	 */
	List<Service> findServices(String serviceRegex, String serverRegex);
}
