package at.arz.ngs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import at.arz.ngs.api.ServiceName;

@Entity
@NamedQueries({	@NamedQuery(name = "getAllServices", query = "SELECT s FROM Service s"),
				@NamedQuery(name = "getService", query = "SELECT s FROM Service s WHERE s.serviceName = :sname") })
public class Service {

	@Id
	@GeneratedValue(generator = "ngs.service", strategy = GenerationType.TABLE)
	@Column(name = "SERVICE_OID")
	private long oid;

	@Column(name = "SERVICE_NAME", unique = true)
	private ServiceName serviceName;

	protected Service() {
		// jpa constructor
	}

	public Service(ServiceName applicationName) {
		this.serviceName = applicationName;
	}

	public long getOid() {
		return oid;
	}

	public ServiceName getServiceName() {
		return serviceName;
	}

	public void renameService(ServiceName serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
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
		if (serviceName == null) {
			if (other.serviceName != null)
				return false;
		} else if (!serviceName.equals(other.serviceName))
			return false;
		return true;
	}
}
