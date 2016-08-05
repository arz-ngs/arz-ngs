package at.arz.ngs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.Status;

@Entity
@Table(	name = "SERVICEINSTANCE",
		uniqueConstraints = { @UniqueConstraint(columnNames = {	"SERVICE",
																"HOST",
																"ENVIRONMENT",
																"SERVICEINSTANCE_NAME" }) })
@NamedQueries({	@NamedQuery(name = "getAllServiceInstances", query = "SELECT si FROM ServiceInstance si"),
				@NamedQuery(name = "getServiceInstance", query = "SELECT si "
						+ "FROM ServiceInstance si "
						+ "Inner JOIN si.HOST host "
						+ "INNER JOIN si.SERVICE service "
						+ "INNER JOIN si.ENVIRONMENT env "
						+ "WHERE si.serviceInstanceName = :siname AND "
																	+ "host.host_name = :hname AND "
																	+ "service.service_name = :sname AND "
																	+ "env.environment_name = :ename"),
				@NamedQuery(name = "getServiceInstance2", query = "SELECT si "+ "FROM ServiceInstance si "
																	+ "WHERE si.host = :host AND "
																	+ "si.service = :service AND "
																	+ "si.environment = :environment AND "
																	+ "serviceinstance_name = :siname"),
				@NamedQuery(name = "getInstances/service/environment/host",
							query = "SELECT si "+ "FROM ServiceInstance si "
									+ "WHERE si.service = :service AND "
									+ "si.environment = :environment AND "
									+ "si.host = :host"),
				@NamedQuery(name = "getInstances/service/environment",
							query = "SELECT si "+ "FROM ServiceInstance si "
									+ "WHERE si.service = :service AND "
									+ "si.environment = :environment"),
				@NamedQuery(name = "getInstances/service",
							query = "SELECT si "+ "FROM ServiceInstance si "
									+ "WHERE si.service = :service") })
public class ServiceInstance {

	@Id
	@GeneratedValue(generator = "ngs.serviceinstance", strategy = GenerationType.TABLE)
	private long oid;

	@Column(name = "SERVICEINSTANCE_NAME")
	private ServiceInstanceName serviceInstanceName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE_OID")
	@Column(name = "SERVICE")
	private Service service;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HOST_OID")
	@Column(name = "HOST")
	private Host host;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENVIRONMENT_OID")
	@Column(name = "ENVIRONMENT")
	private Environment environment;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCRIPT_OID")
	@Column(name = "SCRIPT")
	private Script script;

	protected ServiceInstance() {
			// jpa constructor
	}

	public ServiceInstance(	ServiceInstanceName serviceName,
							Service service,
							Host host,
							Environment environment,
							Script script,
							Status status) {
		this.environment = environment;
		this.serviceInstanceName = serviceName;
		this.host = host;
		this.service = service;
		this.script = script;
		this.status = status;
	}

	public long getOid() {
		return oid;
	}

	public ServiceInstanceName getServiceInstanceName() {
		return serviceInstanceName;
	}

	public Service getService() {
		return service;
	}

	public Host getHost() {
		return host;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public Status getStatus() {
		return status;
	}

	public Script getScript() {
		return script;
	}

	public void setServiceInstanceName(ServiceInstanceName serviceInstanceName) {
		this.serviceInstanceName = serviceInstanceName;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setScript(Script script) {
		this.script = script;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((environment == null) ? 0 : environment.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		result = prime * result + ((serviceInstanceName == null) ? 0 : serviceInstanceName.hashCode());
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
		ServiceInstance other = (ServiceInstance) obj;
		if (environment == null) {
			if (other.environment != null)
				return false;
		} else if (!environment.equals(other.environment))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		if (serviceInstanceName == null) {
			if (other.serviceInstanceName != null)
				return false;
		} else if (!serviceInstanceName.equals(other.serviceInstanceName))
			return false;
		return true;
	}
}
