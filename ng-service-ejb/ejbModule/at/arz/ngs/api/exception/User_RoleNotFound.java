package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.RoleName;
import at.arz.ngs.api.UserName;

@ApplicationException(rollback = true)
public class User_RoleNotFound
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public User_RoleNotFound(UserName userName, RoleName rolename) {
		super(userName.toString() + rolename.toString());
	}
}
