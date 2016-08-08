package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.ScriptName;

@ApplicationException(rollback = true)
public class ScriptNotFound
		extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private ScriptName scriptName;

	public ScriptNotFound(ScriptName scriptName) {
		super(scriptName.toString());
		this.scriptName = scriptName;
	}

	public ScriptName getscriptName() {
		return scriptName;
	}

}
