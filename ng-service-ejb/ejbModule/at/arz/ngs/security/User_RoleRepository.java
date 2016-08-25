package at.arz.ngs.security;

import java.util.List;

public interface User_RoleRepository {

	void addUser_Role(User user, Role role, boolean handover);

	User_Role getUser_Role(User user, Role role);

	void removeUser_Role(User_Role user_Role);

	List<User_Role> getUser_RoleByUser(User u);
}
