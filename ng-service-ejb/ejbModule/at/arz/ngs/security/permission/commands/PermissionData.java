package at.arz.ngs.security.permission.commands;


public class PermissionData {

	private String environmentName;

	private String serviceName;

	private String action;

	public PermissionData(String environmentName, String serviceName, String action) {
		this.environmentName = environmentName;
		this.serviceName = serviceName;
		this.action = action;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
