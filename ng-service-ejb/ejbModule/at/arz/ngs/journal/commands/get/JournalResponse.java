package at.arz.ngs.journal.commands.get;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "service-instance")
@XmlAccessorType(XmlAccessType.FIELD)
public class JournalResponse {

	@XmlElement(required = true)
	private Date time;

	@XmlElement(required = true)
	private String userName;

	@XmlElement(required = true)
	private String targetObject_class;

	@XmlElement(required = true)
	private String targetObject_uniqueKey;

	@XmlElement(required = true)
	private String action;

	public Date getTime() {
		return time;
	}

	public String getUserName() {
		return userName;
	}

	public String getTargetObject_class() {
		return targetObject_class;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setTargetObject_class(String targetObject_class) {
		this.targetObject_class = targetObject_class;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTargetObject_uniqueKey() {
		return targetObject_uniqueKey;
	}

	public void setTargetObject_uniqueKey(String targetObject_uniqueKey) {
		this.targetObject_uniqueKey = targetObject_uniqueKey;
	}

	@Override
	public String toString() {
		return time.toString() + " - " + userName + " - " + targetObject_class + " - " + targetObject_uniqueKey + " - "
				+ action;
	}
}
