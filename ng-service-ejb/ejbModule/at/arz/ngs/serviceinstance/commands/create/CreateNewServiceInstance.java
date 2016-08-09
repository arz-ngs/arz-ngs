package at.arz.ngs.serviceinstance.commands.create;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import at.arz.ngs.commands.ScriptData;

@XmlRootElement(name = "CreateNewServiceInstance")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "environmentName", "hostName", "serviceName", "instanceName" })
public class CreateNewServiceInstance {

	@XmlElement(required = true)
	private String environmentName;

	@XmlElement(required = true)
	private String hostName;

	@XmlElement(required = true)
	private String serviceName;

	@XmlElement(required = true)
	private String instanceName;

	@XmlElement(required = false)
	private ScriptData script;

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

	public ScriptData getScript() {
		return script;
	}

	public void setScript(ScriptData script) {
		this.script = script;
	}

}