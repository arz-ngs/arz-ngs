package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class RoleAlreadyHasPermission
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RoleAlreadyHasPermission(String role, String permission) {
		super(role + " hat bereits die Berechtigung " + permission);
	}
}
