package at.arz.ngs.security;

import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import at.arz.ngs.api.Email;
import at.arz.ngs.api.EmailConverter;
import at.arz.ngs.api.FirstName;
import at.arz.ngs.api.LastName;
import at.arz.ngs.api.UserName;
import at.arz.ngs.converter.jpa.FirstNameConverter;
import at.arz.ngs.converter.jpa.LastNameConverter;
import at.arz.ngs.converter.jpa.UserNameConverter;

@Entity
@Table(name = "USER_")
@NamedQueries({@NamedQuery(name = User.QUERY_ALL, query = "SELECT u FROM User u"),
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

	@Column(name = "FIRST_NAME", unique = false)
	@Convert(converter = FirstNameConverter.class)
	private FirstName firstName;

	@Column(name = "LAST_NAME", unique = false)
	@Convert(converter = LastNameConverter.class)
	private LastName lastName;

	@Column(name = "EMAIL", unique = false)
	@Convert(converter = EmailConverter.class)
	private Email email;

	@OneToMany(mappedBy = "user")
	private List<User_Role> user_roles;

	protected User() {
		//jpa constructor
	}

	public User(UserName applicationName, FirstName firstName, LastName lastName, Email email) {
		this.userName = applicationName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public long getOid() {
		return oid;
	}

	public UserName getUserName() {
		return userName;
	}

	public void addUser_Role(User_Role user_role) {
		user_roles.add(user_role);
	}

	public void removeUser_Role(User_Role user_role) {
		user_roles.remove(user_role);
	}

	public List<User_Role> getUser_roles() {
		return Collections.unmodifiableList(user_roles);
	}

	
	public FirstName getFirstName() {
		return firstName;
	}

	
	public void setFirstName(FirstName firstName) {
		this.firstName = firstName;
	}

	
	public LastName getLastName() {
		return lastName;
	}

	
	public void setLastName(LastName lastName) {
		this.lastName = lastName;
	}

	
	public Email getEmail() {
		return email;
	}

	
	public void setEmail(Email email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + (int) (oid ^ (oid >>> 32));
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((user_roles == null) ? 0 : user_roles.hashCode());
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
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (oid != other.oid)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (user_roles == null) {
			if (other.user_roles != null)
				return false;
		} else if (!user_roles.equals(other.user_roles))
			return false;
		return true;
	}
	
	

}
