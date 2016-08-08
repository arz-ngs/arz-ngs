package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.ServiceInstanceName;

@ApplicationException(rollback = true)
public class ServiceInstanceNotFound
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private ServiceInstanceName instanceName;

	public ServiceInstanceNotFound(ServiceInstanceName instanceName) {
		super(instanceName.toString());
		this.instanceName = instanceName;
	}

	public ServiceInstanceName getInstanceName() {
		return instanceName;
	}
}
