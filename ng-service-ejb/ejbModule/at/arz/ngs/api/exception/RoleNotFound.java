package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.RoleName;

@ApplicationException(rollback = true)
public class RoleNotFound extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public RoleNotFound(RoleName roleName) {
		super(roleName.toString());
	}
	
	public RoleNotFound(long oid) {
		super("Role with object ID " + oid + " couldn't be found!");
	}

}
