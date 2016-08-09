package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.ServiceName;

/**
 * This exception will be thrown, if an expected Service wasn't found in the database.
 * 
 * @author rpci334
 *
 */
@ApplicationException(rollback = true)
public class ServiceNotFound
		extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private ServiceName serviceName;

	public ServiceNotFound(ServiceName serviceName) {
		super(serviceName.toString());
		this.serviceName = serviceName;
	}

	public ServiceName getServiceName() {
		return serviceName;
	}


}
