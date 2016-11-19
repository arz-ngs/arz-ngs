package at.arz.ngs.ui.data_collections;

import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.ServiceName;

public class Environment_Service {

	private EnvironmentName environmentName;
	private ServiceName serviceName;

	public Environment_Service(EnvironmentName environmentName, ServiceName serviceName) {
		this.environmentName = environmentName;
		this.serviceName = serviceName;
	}

	public EnvironmentName getEnvironmentName() {
		return environmentName;
	}

	public ServiceName getServiceName() {
		return serviceName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((environmentName == null) ? 0 : environmentName.hashCode());
		result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Environment_Service other = (Environment_Service) obj;
		if (environmentName == null) {
			if (other.environmentName != null) {
				return false;
			}
		}
		else if (!environmentName.equals(other.environmentName)) {
			return false;
		}
		if (serviceName == null) {
			if (other.serviceName != null) {
				return false;
			}
		}
		else if (!serviceName.equals(other.serviceName)) {
			return false;
		}
		return true;
	}
}
