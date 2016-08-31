package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = false)
public class ExecuteAction extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExecuteAction(String action, String object) {
		super("Error by executing" + action + " on " + object);
	}
}
