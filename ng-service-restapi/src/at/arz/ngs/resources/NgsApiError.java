package at.arz.ngs.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NgsApiError {

	private String reason;
	private String message;

	protected NgsApiError() {
		// jaxb constructor
	}

	public NgsApiError(String reason, String message) {
		this.reason = reason;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getReason() {
		return reason;
	}

}
