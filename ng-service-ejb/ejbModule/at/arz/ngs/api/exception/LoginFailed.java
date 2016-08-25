package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class LoginFailed
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LoginFailed(String reason) {
		super(reason);
	}
}
