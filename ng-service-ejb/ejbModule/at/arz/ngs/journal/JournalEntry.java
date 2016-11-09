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
@NamedQueries({
		@NamedQuery(name = JournalEntry.QUERY_ALL, query = "SELECT je FROM JournalEntry je ORDER BY je.time DESC")})
public class JournalEntry {

	public static final String QUERY_ALL = "JournalEntry.getAll";

	@Id
	@GeneratedValue(generator = "ngs.journalentry", strategy = GenerationType.TABLE)
	private long oid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME")
	private Date time;

	@Column(name = "USER_NAME")
	@Convert(converter = UserNameConverter.class)
	private UserName userName;

	@Column(name = "TARGETOBJECT_CLASS")
	private String targetObject_class;

	@Column(name = "TARGETOBJECT_OID")
	private long targetObject_oid;

	@Column(name = "TARGETOBJECT_UNIQUEKEY")
	private String targetObject_uniqueKey;

	@Column(name = "ACTION")
	private String action;

	protected JournalEntry() {
		// jpa constructor
	}

	public JournalEntry(UserName userName, String targetObject_class, long targetObject_oid,
			String targetObject_uniqueKey, String action) {
		this.targetObject_uniqueKey = targetObject_uniqueKey;
		this.time = new Date(System.currentTimeMillis());
		this.userName = userName;
		this.targetObject_class = targetObject_class;
		this.targetObject_oid = targetObject_oid;
		this.action = action;
	}

	public long getOid() {
		return oid;
	}

	public void setOid(long oid) {
		this.oid = oid;
	}

	public Date getTime() {
		return new Date(time.getTime());
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public UserName getUserName() {
		return userName;
	}

	public void setUserName(UserName userName) {
		this.userName = userName;
	}

	public String getTargetObject_class() {
		return targetObject_class;
	}

	public void setTargetObject_class(String targetObject_class) {
		this.targetObject_class = targetObject_class;
	}

	public long getTargetObject_oid() {
		return targetObject_oid;
	}

	public void setTargetObject_oid(long targetObject_oid) {
		this.targetObject_oid = targetObject_oid;
	}

	public String getTargetObject_uniqueKey() {
		return targetObject_uniqueKey;
	}

	public void setTargetObject_uniqueKey(String targetObject_uniqueKey) {
		this.targetObject_uniqueKey = targetObject_uniqueKey;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((targetObject_class == null) ? 0 : targetObject_class.hashCode());
		result = prime * result + ((targetObject_uniqueKey == null) ? 0 : targetObject_uniqueKey.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		}
		else if (!action.equals(other.action))
			return false;
		if (targetObject_class == null) {
			if (other.targetObject_class != null)
				return false;
		}
		else if (!targetObject_class.equals(other.targetObject_class))
			return false;
		if (targetObject_uniqueKey == null) {
			if (other.targetObject_uniqueKey != null)
				return false;
		}
		else if (!targetObject_uniqueKey.equals(other.targetObject_uniqueKey))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		}
		else if (!time.equals(other.time))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		}
		else if (!userName.equals(other.userName))
			return false;
		return true;
	}
}
