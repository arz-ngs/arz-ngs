package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.RoleName;

@ApplicationException(rollback = true)
public class RoleNotFound extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public RoleNotFound(RoleName roleName) {
		super(roleName.toString());
	}

}
