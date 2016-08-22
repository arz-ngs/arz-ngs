package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class RoleAlreadyExist
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RoleAlreadyExist(String roleInfo) {
		super(roleInfo);
	}

}
