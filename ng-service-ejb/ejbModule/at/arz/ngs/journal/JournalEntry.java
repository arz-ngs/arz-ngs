package at.arz.ngs.journal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import at.arz.ngs.api.UserName;
import at.arz.ngs.converter.jpa.UserNameConverter;

@Entity
@NamedQueries({@NamedQuery(name = JournalEntry.QUERY_ALL, query = "SELECT je FROM JournalEntry je")})
public class JournalEntry {
	
	public static final String QUERY_ALL = "JournalEntry.getAll";
	
	@Id
	@GeneratedValue(generator = "ngs.journalentry", strategy = GenerationType.TABLE)
	private long oid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP")
	private Date timestamp;
	
	@Column(name = "USER_NAME")
	@Convert(converter = UserNameConverter.class)
	private UserName userName;

	@Column(name = "TARGETOBJECT_CLASS")
	private String targetObject_class;
	
	@Column(name = "TARGETOBJECT_OID")
	private long targetObject_oid;
	
	@Column(name = "ACTION")
	private String action;
	
	protected JournalEntry() {
		// jpa constructor
	}
	
	public JournalEntry(UserName userName, String targetObject_class, long targetObject_oid, String action) {
		this.timestamp = new Date(System.currentTimeMillis());
		this.userName = userName;
		this.targetObject_class = targetObject_class;
		this.targetObject_oid = targetObject_oid;
		this.action = action;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((targetObject_class == null) ? 0 : targetObject_class.hashCode());
		result = prime * result + (int) (targetObject_oid ^ (targetObject_oid >>> 32));
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
		JournalEntry other = (JournalEntry) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (targetObject_class == null) {
			if (other.targetObject_class != null)
				return false;
		} else if (!targetObject_class.equals(other.targetObject_class))
			return false;
		if (targetObject_oid != other.targetObject_oid)
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public UserName getUserName() {
		return userName;
	}

	public String getTargetObject_class() {
		return targetObject_class;
	}

	public long getTargetObject_oid() {
		return targetObject_oid;
	}

	public String getAction() {
		return action;
	}
	
	
}
