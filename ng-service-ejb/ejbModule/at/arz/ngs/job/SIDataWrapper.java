package at.arz.ngs.job;

import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;

public class SIDataWrapper {
	private EnvironmentName nextEnvName;
	private ServiceName nextServiceName;
	private HostName nextHostName;
	private ServiceInstanceName nextServiceInstanceName;
	private String nextPathToExecute;

	public SIDataWrapper() {
	}

	public SIDataWrapper(EnvironmentName nextEnvName, ServiceName nextServiceName, HostName nextHostName,
			ServiceInstanceName nextServiceInstanceName, String nextPathToExecute) {
		this.nextEnvName = nextEnvName;
		this.nextServiceName = nextServiceName;
		this.nextHostName = nextHostName;
		this.nextServiceInstanceName = nextServiceInstanceName;
		this.nextPathToExecute = nextPathToExecute;
	}

	public EnvironmentName getNextEnvName() {
		return nextEnvName;
	}

	public ServiceName getNextServiceName() {
		return nextServiceName;
	}

	public HostName getNextHostName() {
		return nextHostName;
	}

	public ServiceInstanceName getNextServiceInstanceName() {
		return nextServiceInstanceName;
	}

	public String getNextPathToExecute() {
		return nextPathToExecute;
	}

	public void setNextEnvName(EnvironmentName nextEnvName) {
		this.nextEnvName = nextEnvName;
	}

	public void setNextServiceName(ServiceName nextServiceName) {
		this.nextServiceName = nextServiceName;
	}

	public void setNextHostName(HostName nextHostName) {
		this.nextHostName = nextHostName;
	}

	public void setNextServiceInstanceName(ServiceInstanceName nextServiceInstanceName) {
		this.nextServiceInstanceName = nextServiceInstanceName;
	}

	public void setNextPathToExecute(String nextPathToExecute) {
		this.nextPathToExecute = nextPathToExecute;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nextEnvName == null) ? 0 : nextEnvName.hashCode());
		result = prime * result + ((nextHostName == null) ? 0 : nextHostName.hashCode());
		result = prime * result + ((nextPathToExecute == null) ? 0 : nextPathToExecute.hashCode());
		result = prime * result + ((nextServiceInstanceName == null) ? 0 : nextServiceInstanceName.hashCode());
		result = prime * result + ((nextServiceName == null) ? 0 : nextServiceName.hashCode());
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
		SIDataWrapper other = (SIDataWrapper) obj;
		if (nextEnvName == null) {
			if (other.nextEnvName != null) {
				return false;
			}
		}
		else if (!nextEnvName.equals(other.nextEnvName)) {
			return false;
		}
		if (nextHostName == null) {
			if (other.nextHostName != null) {
				return false;
			}
		}
		else if (!nextHostName.equals(other.nextHostName)) {
			return false;
		}
		if (nextPathToExecute == null) {
			if (other.nextPathToExecute != null) {
				return false;
			}
		}
		else if (!nextPathToExecute.equals(other.nextPathToExecute)) {
			return false;
		}
		if (nextServiceInstanceName == null) {
			if (other.nextServiceInstanceName != null) {
				return false;
			}
		}
		else if (!nextServiceInstanceName.equals(other.nextServiceInstanceName)) {
			return false;
		}
		if (nextServiceName == null) {
			if (other.nextServiceName != null) {
				return false;
			}
		}
		else if (!nextServiceName.equals(other.nextServiceName)) {
			return false;
		}
		return true;
	}
}
