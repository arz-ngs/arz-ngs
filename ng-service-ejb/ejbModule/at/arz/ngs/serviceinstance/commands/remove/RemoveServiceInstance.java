package at.arz.ngs.serviceinstance.commands.remove;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RemoveServiceInstance")
@XmlAccessorType(XmlAccessType.FIELD)
public class RemoveServiceInstance {

	@XmlElement(required = true)
	private long version = -1;

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
}
