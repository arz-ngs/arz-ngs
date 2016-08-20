package at.arz.ngs.user.jpa;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.User;
import at.arz.ngs.UserRepository;
import at.arz.ngs.api.UserName;
import at.arz.ngs.user.JPAUserRepository;

public class JPAUserRepositoryIT extends AbstractJpaIT{

	private UserRepository user;
	
	@Before
	public void setUpBefore() {
		user = new JPAUserRepository(getEntityManager());
	}
	
	@Test
	public void addUser() {
		UserName user1 = new UserName("daniel");
		user.addUser(user1);
//		User user2 = user.getUser(user1);
//		assertEquals("daniel", user2.getUserName().toString());
	}

}
