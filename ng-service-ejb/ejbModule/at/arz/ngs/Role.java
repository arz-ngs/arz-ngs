package at.arz.ngs;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import at.arz.ngs.api.RoleName;
import at.arz.ngs.converter.jpa.RoleNameConverter;

@Entity
@NamedQueries({ @NamedQuery(name = Role.QUERY_ALL, query = "SELECT r FROM Role r"),
				@NamedQuery(name = Role.QUERY_BY_ROLENAME, query = "SELECT r FROM Role r WHERE r.roleName = :rname")})
public class Role {
	
	public static final String QUERY_ALL = "Role..getAll";
	public static final String QUERY_BY_ROLENAME = "Role.findByUniqueKey";
	
	@Id
	@GeneratedValue(generator = "ngs.service", strategy = GenerationType.TABLE)
	private long oid;
	
	@Column(name = "ROLE_NAME", unique = true)
	@Convert(converter = RoleNameConverter.class)
	private RoleName roleName;

	public Role() {
		
	}
	
	public Role(RoleName roleName) {
		this.roleName = roleName;
	}
	
	public long getOid() {
		return oid;
	}
	
	public RoleName getRoleName() {
		return roleName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
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
		Role other = (Role) obj;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		return true;
	}
	
	
}
