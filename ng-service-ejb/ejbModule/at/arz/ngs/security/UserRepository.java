package at.arz.ngs.security;

import java.util.List;

import at.arz.ngs.api.Email;
import at.arz.ngs.api.FirstName;
import at.arz.ngs.api.LastName;
import at.arz.ngs.api.UserName;

public interface UserRepository {
	User getUser(UserName userName);
	
	List<User> getAllUsers();
	
	void addUser(UserName user, FirstName firstName, LastName lastName, Email email);

	void removeUser(User user);
}
