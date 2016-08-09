package at.arz.ngs.serviceinstance.commands.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "perform-action")
@XmlAccessorType(XmlAccessType.FIELD)
public class PerformAction {

	@XmlElement(required = true)
	private String performAction;

	public String getPerformAction() {
		return performAction;
	}

	public void setPerformAction(String performAction) {
		this.performAction = performAction;
	}
}
