package at.arz.ngs.security;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import at.arz.ngs.api.Action;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.converter.jpa.EnvironmentNameConverter;
import at.arz.ngs.converter.jpa.ServiceNameConverter;

@Entity
@Table(name = "PERMISSION", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"ENVIRONMENT_NAME", "SERVICE_NAME", "ACTION"})})
@NamedQueries({
		@NamedQuery(name = Permission.QUERY_BY_ENVIRONMENTandSERVICEandACTION, query = "SELECT p FROM Permission p "
				+ "WHERE p.environmentName = :ename " + "AND p.serviceName = :sname " + "AND p.action = :action")})
public class Permission {

	public static final String QUERY_BY_ENVIRONMENTandSERVICEandACTION = "Permission.findByUniqueKey";

	@Id
	@GeneratedValue(generator = "ngs.permission", strategy = GenerationType.TABLE)
	private long oid;

	@Column(name = "ENVIRONMENT_NAME")
	@Convert(converter = EnvironmentNameConverter.class)
	private EnvironmentName environmentName;

	@Column(name = "SERVICE_NAME")
	@Convert(converter = ServiceNameConverter.class)
	private ServiceName serviceName;

	@Enumerated(EnumType.STRING)
	@Column(name = "ACTION")
	private Action action;

	@JoinColumn(name = "ROLE_OID")
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Role> roles;

	protected Permission() {
		//jpa constructor
	}

	public Permission(EnvironmentName environmentName, ServiceName serviceName, Action action) {
		this.environmentName = environmentName;
		this.serviceName = serviceName;
		this.action = action;
		roles = new LinkedList<>();
	}

	public EnvironmentName getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(EnvironmentName environmentName) {
		this.environmentName = environmentName;
	}

	public ServiceName getServiceName() {
		return serviceName;
	}

	public void setServiceName(ServiceName serviceName) {
		this.serviceName = serviceName;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public long getOid() {
		return oid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((environmentName == null) ? 0 : environmentName.hashCode());
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
		Permission other = (Permission) obj;
		if (action != other.action)
			return false;
		if (environmentName == null) {
			if (other.environmentName != null)
				return false;
		} else if (!environmentName.equals(other.environmentName))
			return false;
		if (serviceName == null) {
			if (other.serviceName != null)
				return false;
		} else if (!serviceName.equals(other.serviceName))
			return false;
		return true;
	}


}
