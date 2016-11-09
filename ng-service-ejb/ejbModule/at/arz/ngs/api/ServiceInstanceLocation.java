package at.arz.ngs.api;

import java.util.Objects;

public class ServiceInstanceLocation {

	private HostName hostName;
	private ServiceInstanceName instanceName;

	public ServiceInstanceLocation(HostName hostName, ServiceInstanceName instanceName) {
		this.hostName = hostName;
		this.instanceName = instanceName;
	}

	public ServiceInstanceName getInstanceName() {
		return instanceName;
	}

	public HostName getHostName() {
		return hostName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(hostName, instanceName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceInstanceLocation other = (ServiceInstanceLocation) obj;
		if (different(hostName, other.hostName)) {
			return false;
		}
		if (different(instanceName, other.instanceName)) {
			return false;
		}
		return true;
	}

	private <T> boolean different(T a, T b) {
		return !Objects.equals(a, b);
	}

}
