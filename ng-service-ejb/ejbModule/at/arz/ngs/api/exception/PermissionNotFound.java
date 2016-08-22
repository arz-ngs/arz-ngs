package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.Action;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.ServiceName;

@ApplicationException(rollback = true)
public class PermissionNotFound
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PermissionNotFound(EnvironmentName env, ServiceName service, Action action) {
		super(env + "/" + service + "/" + action.name());
	}
}
