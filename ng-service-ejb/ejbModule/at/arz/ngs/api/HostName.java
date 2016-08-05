package at.arz.ngs.api;

import java.io.Serializable;

public class HostName
		implements Serializable {

	private static final long serialVersionUID = 1L;
	private String hostName;

	public HostName(String hostName) {
		this.hostName = hostName;
	}

	public String getName() {
		return hostName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HostName other = (HostName) obj;
		if (hostName == null) {
			if (other.hostName != null)
				return false;
		} else if (!hostName.equals(other.hostName))
			return false;
		return true;
	}
}
