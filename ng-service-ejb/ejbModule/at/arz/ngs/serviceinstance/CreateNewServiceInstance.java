package at.arz.ngs.serviceinstance;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateNewServiceInstance {

	private EnvironmentName environmentName;

	private HostName hostName;

	private ServiceName serviceName;

	private ServiceInstanceName instanceName;

	public CreateNewServiceInstance(EnvironmentName environmentName,
									HostName hostName,
									ServiceName serviceName,
									ServiceInstanceName instanceName) {
		this.environmentName = environmentName;
		this.hostName = hostName;
		this.serviceName = serviceName;
		this.instanceName = instanceName;
	}

	public EnvironmentName getEnvironmentName() {
		return environmentName;
	}

	public HostName getHostName() {
		return hostName;
	}

	public ServiceInstanceName getInstanceName() {
		return instanceName;
	}

	public ServiceName getServiceName() {
		return serviceName;
	}
}