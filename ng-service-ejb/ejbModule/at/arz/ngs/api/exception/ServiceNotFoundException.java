package at.arz.ngs.api.exception;

import at.arz.ngs.api.ServiceName;

public class ServiceNotFoundException
		extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private ServiceName serviceName;

	public ServiceNotFoundException(ServiceName serviceName) {
		super(serviceName.toString());
		this.serviceName = serviceName;
	}

	public ServiceName getServiceName() {
		return serviceName;
	}


}
