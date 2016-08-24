package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class UserAlreadyHasRole
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserAlreadyHasRole(String user, String role) {
		super(user + " already has " + role);
	}

}
