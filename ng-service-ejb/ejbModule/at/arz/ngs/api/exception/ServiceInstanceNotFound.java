package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;

/**
 * This exception will be thrown, if an expected ServiceInstance wasn't found in the database.
 * 
 * @author rpci334
 *
 */
@ApplicationException(rollback = true)
public class ServiceInstanceNotFound
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private ServiceName serviceName;
	private EnvironmentName environmentName;
	private HostName hostName;
	private ServiceInstanceName serviceInstanceName;

	public ServiceInstanceNotFound(	ServiceInstanceName serviceInstanceName,
									ServiceName serviceName,
									HostName hostName,
									EnvironmentName environmentName) {
		super(serviceName.toString()+ "/"
				+ environmentName.toString()
				+ "/"
				+ hostName.toString()
				+ "/"
				+ serviceInstanceName.toString());
		this.serviceName = serviceName;
		this.environmentName = environmentName;
		this.hostName = hostName;
		this.serviceInstanceName = serviceInstanceName;
	}

	public ServiceName getServiceName() {
		return serviceName;
	}

	public EnvironmentName getEnvironmentName() {
		return environmentName;
	}

	public HostName getHostName() {
		return hostName;
	}

	public ServiceInstanceName getServiceInstanceName() {
		return serviceInstanceName;
	}

	public String getServiceInstance() {
		return serviceName.toString()+ "/"
				+ environmentName.toString()
				+ "/"
				+ hostName.toString()
				+ "/"
				+ serviceInstanceName.toString();
	}


}
