package at.arz.ngs.journal;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "service-instance")
@XmlAccessorType(XmlAccessType.FIELD)
public class JournalResponse {

	@XmlElement(required = true)
	private Date timestamp;

	@XmlElement(required = true)
	private String userName;

	@XmlElement(required = true)
	private String targetObject_class;

	@XmlElement(required = true)
	private String targetObject;

	@XmlElement(required = true)
	private String action;

	public Date getTimestamp() {
		return timestamp;
	}

	public String getUserName() {
		return userName;
	}

	public String getTargetObject_class() {
		return targetObject_class;
	}

	public String getTargetObject() {
		return targetObject;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setTargetObject_class(String targetObject_class) {
		this.targetObject_class = targetObject_class;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	
}
