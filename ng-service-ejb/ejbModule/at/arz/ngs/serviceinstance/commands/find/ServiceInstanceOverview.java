package at.arz.ngs.serviceinstance.commands.find;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "service-instance")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceInstanceOverview {

	@XmlElement(required = true)
	private String serviceName;

	@XmlElement(required = true)
	private String environmentName;

	@XmlElement(required = true)
	private String hostName;

	@XmlElement(required = true)
	private String instanceName;

	@XmlElement(required = true)
	private String status;

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
