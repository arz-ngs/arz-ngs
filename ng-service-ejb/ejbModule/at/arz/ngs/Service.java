package at.arz.ngs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import at.arz.ngs.api.ServiceName;

@Entity
public class Service {

	@Id
	@GeneratedValue(generator = "ngs.application", strategy = GenerationType.TABLE)
	@Column(name = "APPLICATION_OID")
	private long oid;

	@Column(name = "APPLICATION_NAME", unique = true)
	private ServiceName applicationName;

	protected Service() {
		// jpa constructor
	}

	public Service(long oid, ServiceName applicationName) {
		this.oid = oid;
		this.applicationName = applicationName;
	}

	public long getOid() {
		return oid;
	}

	public ServiceName getApplicationName() {
		return applicationName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applicationName == null) ? 0 : applicationName.hashCode());
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
		Service other = (Service) obj;
		if (applicationName == null) {
			if (other.applicationName != null)
				return false;
		} else if (!applicationName.equals(other.applicationName))
			return false;
		return true;
	}
}
