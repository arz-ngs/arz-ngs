package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.ScriptName;

/**
 * This exception will be thrown, if an expected Script wasn't found in the database.
 * 
 * @author rpci334
 *
 */
@ApplicationException(rollback = true)
public class ScriptNotFound
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ScriptNotFound(ScriptName scriptName) {
		super(scriptName.toString());
	}


}
