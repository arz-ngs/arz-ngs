package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.UserName;

@ApplicationException(rollback = true)
public class UserNotFound extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public UserNotFound(UserName userName) {
		super(userName.toString());
	}
}
