package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ServiceInstanceAlreadyExist
		extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serviceInstanceInfo;

	public ServiceInstanceAlreadyExist(String serviceInstanceInfo) {
		super("ServiceInstance " + serviceInstanceInfo + " existiert bereits!");
		this.serviceInstanceInfo = serviceInstanceInfo;
	}

	public String getAlreadyExistingServiceInstance() {
		return "ServiceInstance " + serviceInstanceInfo + " existiert bereits!";
	}

}
