package at.arz.ngs.security;

import at.arz.ngs.api.Action;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.ServiceName;

public interface PermissionRepository {

	Permission getPermission(EnvironmentName environmentName, ServiceName serviceName, Action action);

	/**
	 * No more get-methods needed, because the list of Permission can be get out
	 * of the ManyToMany List in the Roles.
	 */
	void addPermission(EnvironmentName environmentName, ServiceName serviceName, Action action);

	void removePermission(Permission permission);
}
