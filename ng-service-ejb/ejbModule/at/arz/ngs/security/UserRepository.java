package at.arz.ngs.security;

import java.util.List;

import at.arz.ngs.api.UserName;

public interface UserRepository {
	User getUser(UserName userName);
	
	List<User> getAllUsers();
	
	void addUser(UserName user);
}
