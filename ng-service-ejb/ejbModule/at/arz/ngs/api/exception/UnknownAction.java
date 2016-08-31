package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class UnknownAction extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public UnknownAction(String action) {
		super(action);
	}
}
