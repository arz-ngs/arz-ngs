package at.arz.ngs;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import at.arz.ngs.api.UserName;
import at.arz.ngs.converter.jpa.UserNameConverter;

@Entity
@NamedQueries({ @NamedQuery(name = User.QUERY_ALL, query = "SELECT u FROM User u"),
				@NamedQuery(name = User.QUERY_BY_USERNAME, query = "SELECT u FROM User u WHERE u.userName = :uname")})
public class User {
	
	public static final String QUERY_ALL = "User.getAll";
	public static final String QUERY_BY_USERNAME = "User.findByUniqueKey";
	
	@Id
	@GeneratedValue(generator = "ngs.user", strategy = GenerationType.TABLE)
	private long oid;
	
	@Column(name = "USER_NAME", unique = true)
	@Convert(converter = UserNameConverter.class)
	private UserName userName;
	
	protected User() {
		
	}
	
	public User(UserName applicationName) {
		this.userName = applicationName;
	}

	public long getOid() {
		return oid;
	}
	
	public UserName getUserName() {
		return userName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		User other = (User) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	
}
