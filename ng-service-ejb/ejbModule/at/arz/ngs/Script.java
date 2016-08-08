package at.arz.ngs;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import at.arz.ngs.api.PathRestart;
import at.arz.ngs.api.PathStart;
import at.arz.ngs.api.PathStatus;
import at.arz.ngs.api.PathStop;
import at.arz.ngs.api.ScriptName;
import at.arz.ngs.converter.jpa.PathRestartConverter;
import at.arz.ngs.converter.jpa.PathStartConverter;
import at.arz.ngs.converter.jpa.PathStatusConverter;
import at.arz.ngs.converter.jpa.PathStopConverter;

@Entity
@NamedQueries({	@NamedQuery(name = Script.QUERY_ALL, query = "SELECT sc FROM Script sc"),
				@NamedQuery(name = Script.QUERY_BY_SCRIPTNAME,
							query = "SELECT sc FROM Script sc WHERE sc.scriptName = :scname") })
public class Script {

	public static final String QUERY_ALL = "Script.getAll";
	public static final String QUERY_BY_SCRIPTNAME = "Script.findByUniqueKey";

	@Id
	@GeneratedValue(generator = "ngs.script", strategy = GenerationType.TABLE)
	private long oid;

	@Column(name = "SCRIPT_NAME", unique = true)
	private ScriptName scriptName;

	@Column(name = "PATH_START")
	@Convert(converter = PathStartConverter.class)
	private PathStart pathStart;

	@Column(name = "PATH_STOP")
	@Convert(converter = PathStopConverter.class)
	private PathStop pathStop;

	@Column(name = "PATH_RESTART")
	@Convert(converter = PathRestartConverter.class)
	private PathRestart pathRestart;

	@Column(name = "PATH_STATUS")
	@Convert(converter = PathStatusConverter.class)
	private PathStatus pathStatus;

	protected Script() {
		// jpa constructor
	}

	public Script(	ScriptName scriptName,
					PathStart pathStart,
					PathStop pathStop,
					PathRestart pathRestart,
					PathStatus pathStatus) {
		this.scriptName = scriptName;
		this.pathStart = pathStart;
		this.pathStop = pathStop;
		this.pathRestart = pathRestart;
		this.pathStatus = pathStatus;
	}

	public long getOid() {
		return oid;
	}

	public ScriptName getScriptName() {
		return scriptName;
	}

	public PathStart getPathStart() {
		return pathStart;
	}

	public PathStop getPathStop() {
		return pathStop;
	}

	public PathStatus getPathStatus() {
		return pathStatus;
	}

	public PathRestart getPathRestart() {
		return pathRestart;
	}

	public void setScriptName(ScriptName scriptName) {
		this.scriptName = scriptName;
	}

	public void setPathStart(PathStart pathStart) {
		this.pathStart = pathStart;
	}

	public void setPathStop(PathStop pathStop) {
		this.pathStop = pathStop;
	}

	public void setPathRestart(PathRestart pathRestart) {
		this.pathRestart = pathRestart;
	}

	public void setPathStatus(PathStatus pathStatus) {
		this.pathStatus = pathStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pathRestart == null) ? 0 : pathRestart.hashCode());
		result = prime * result + ((pathStart == null) ? 0 : pathStart.hashCode());
		result = prime * result + ((pathStatus == null) ? 0 : pathStatus.hashCode());
		result = prime * result + ((pathStop == null) ? 0 : pathStop.hashCode());
		result = prime * result + ((scriptName == null) ? 0 : scriptName.hashCode());
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
		Script other = (Script) obj;
		if (pathRestart == null) {
			if (other.pathRestart != null)
				return false;
		} else if (!pathRestart.equals(other.pathRestart))
			return false;
		if (pathStart == null) {
			if (other.pathStart != null)
				return false;
		} else if (!pathStart.equals(other.pathStart))
			return false;
		if (pathStatus == null) {
			if (other.pathStatus != null)
				return false;
		} else if (!pathStatus.equals(other.pathStatus))
			return false;
		if (pathStop == null) {
			if (other.pathStop != null)
				return false;
		} else if (!pathStop.equals(other.pathStop))
			return false;
		if (scriptName == null) {
			if (other.scriptName != null)
				return false;
		} else if (!scriptName.equals(other.scriptName))
			return false;
		return true;
	}
}
