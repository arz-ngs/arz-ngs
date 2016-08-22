package at.arz.ngs.security;


public interface User_RoleRepository {

	void addUser_Role(User user, Role role, boolean handover);

	User_Role getUser_Role(User user, Role role);

	void removeUser_Role(User_Role user_Role);
}
