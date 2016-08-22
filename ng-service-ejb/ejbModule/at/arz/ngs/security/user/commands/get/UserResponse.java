package at.arz.ngs.security.user.commands.get;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import at.arz.ngs.security.user.commands.UserData;

public class UserResponse {

	private List<UserData> users;

	public UserResponse() {
		users = new LinkedList<>();
	}

	public void addUser(UserData user) {
		users.add(user);
	}

	public List<UserData> getUsers() {
		return Collections.unmodifiableList(users);
	}
}
