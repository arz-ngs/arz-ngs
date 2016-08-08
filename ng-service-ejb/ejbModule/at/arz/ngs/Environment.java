package at.arz.ngs;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.converter.jpa.EnvironmentNameConverter;

@Entity
@NamedQueries({	@NamedQuery(name = Environment.QUERY_ALL, query = "SELECT e FROM Environment e"),
				@NamedQuery(name = Environment.QUERY_BY_ENVIRONMENTNAME,
							query = "SELECT e FROM Environment e WHERE e.environmentName = :ename") })
public class Environment {

	public static final String QUERY_ALL = "Environment.getAll";
	public static final String QUERY_BY_ENVIRONMENTNAME = "Environment.findByUniqueKey";

	@Id
	@GeneratedValue(generator = "ngs.environment", strategy = GenerationType.TABLE)
	private long oid;

	@Column(name = "ENVIRONMENT_NAME", unique = true)
	@Convert(converter = EnvironmentNameConverter.class)
	private EnvironmentName environmentName;

	protected Environment() {
		// jpa constructor
	}

	public Environment(EnvironmentName environmentName) {
		this.environmentName = environmentName;
	}

	public long getOid() {
		return oid;
	}

	public EnvironmentName getEnvironmentName() {
		return environmentName;
	}

	public void renameEnvironment(EnvironmentName environmentName) {
		this.environmentName = environmentName;
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
