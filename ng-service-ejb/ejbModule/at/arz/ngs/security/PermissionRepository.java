package at.arz.ngs.security;

public interface PermissionRepository {

	Permission getPermission(String toFillOUT);

	/**
	 * No more get-methods needed, because the list of Permission can be get out
	 * of the ManyToMany List in the Roles.
	 */
	void addPermission();

	void removePermission(Permission permission);
}
