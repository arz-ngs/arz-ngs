package at.arz.ngs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import at.arz.ngs.api.EnvironmentName;

@Entity
public class Environment {

	@Id
	@GeneratedValue(generator = "ngs.envId", strategy = GenerationType.TABLE)
	private long oid;

	@Column(name = "ENVIRONMENT_NAME", unique = true)
	private EnvironmentName environmentName;

	public Environment() {
		// jpa constructor
	}

	public Environment(long oid, EnvironmentName environmentName) {
		this.oid = oid;
		this.environmentName = environmentName;
	}

	public long getOid() {
		return oid;
	}

	public EnvironmentName getEnvironmentName() {
		return environmentName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((environmentName == null) ? 0 : environmentName.hashCode());
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
		Environment other = (Environment) obj;
		if (environmentName == null) {
			if (other.environmentName != null)
				return false;
		} else if (!environmentName.equals(other.environmentName))
			return false;
		return true;
	}
}
