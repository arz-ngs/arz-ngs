package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class RoleStillInUse
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RoleStillInUse(String roleInfo) {
		super(roleInfo);
	}

}
