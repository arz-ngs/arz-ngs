package at.arz.ngs.serviceinstance.commands;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "script")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScriptData {

	@XmlElement(required = false)
	private String scriptName;

	@XmlElement(required = false)
	private String pathStart;

	@XmlElement(required = false)
	private String pathStop;

	@XmlElement(required = false)
	private String pathRestart;

	@XmlElement(required = false)
	private String pathStatus;

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

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