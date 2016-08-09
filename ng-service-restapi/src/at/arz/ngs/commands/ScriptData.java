package at.arz.ngs.commands;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "script")
public class ScriptData {

	@XmlElement(required = false)
	private String pathStart;

	@XmlElement(required = false)
	private String pathStop;

	@XmlElement(required = false)
	private String pathRestart;

	@XmlElement(required = false)
	private String pathStatus;

	public String getPathStart() {
		return pathStart;
	}

	public void setPathStart(String pathStart) {
		this.pathStart = pathStart;
	}

	public String getPathStop() {
		return pathStop;
	}

	public void setPathStop(String pathStop) {
		this.pathStop = pathStop;
	}

	public String getPathRestart() {
		return pathRestart;
	}

	public void setPathRestart(String pathRestart) {
		this.pathRestart = pathRestart;
	}

	public String getPathStatus() {
		return pathStatus;
	}

	public void setPathStatus(String pathStatus) {
		this.pathStatus = pathStatus;
	}


}