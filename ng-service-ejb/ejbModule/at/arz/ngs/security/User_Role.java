package at.arz.ngs.security;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
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

import at.arz.ngs.converter.jpa.BooleanTFConverter;

@Entity
@Table(name = "USER_ROLE", uniqueConstraints = { @UniqueConstraint(columnNames = { "USER_OID", "ROLE_OID" }) })
@NamedQueries({ @NamedQuery(name = User_Role.QUERY_BY_USER_ROLE,
							query = "SELECT ur FROM User_Role ur WHERE ur.user = :uruser AND ur.role = :urrole"),
				@NamedQuery(name = User_Role.QUERY_BY_USER,
							query = "SELECT ur FROM User_Role ur WHERE ur.user = :uruser")})
public class User_Role {

	public static final String QUERY_BY_USER_ROLE = "User_Role.findbyUniqueKey";
	public static final String QUERY_BY_USER = "User_Role.findbyUser";

	@Id
	@GeneratedValue(generator = "ngs.user_role", strategy = GenerationType.TABLE)
	private long oid;

	@JoinColumn(name = "USER_OID")
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@JoinColumn(name = "ROLE_OID")
	@ManyToOne(fetch = FetchType.LAZY)
	private Role role;

	@Column(name = "HANDOVER")
	@Convert(converter = BooleanTFConverter.class)
	private Boolean handover;

	protected User_Role() {
		// jpa constructor
	}

	public User_Role(User user, Role role, boolean handover) {
		super();
		this.user = user;
		this.role = role;
		this.handover = handover;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isHandover() {
		return handover;
	}

	public void setHandover(boolean handover) {
		this.handover = handover;
	}

	public long getOid() {
		return oid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		User_Role other = (User_Role) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		}
		else if (!role.equals(other.role))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		}
		else if (!user.equals(other.user))
			return false;
		return true;
	}
}
