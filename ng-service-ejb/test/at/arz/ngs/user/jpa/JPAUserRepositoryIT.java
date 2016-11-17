package at.arz.ngs.user.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.api.Email;
import at.arz.ngs.api.FirstName;
import at.arz.ngs.api.LastName;
import at.arz.ngs.api.UserName;
import at.arz.ngs.security.User;
import at.arz.ngs.security.UserRepository;
import at.arz.ngs.security.user.jpa.JPAUserRepository;

public class JPAUserRepositoryIT extends AbstractJpaIT {

	private UserRepository repository;

	@Before
	public void setUpBefore() {
		repository = new JPAUserRepository(getEntityManager());
	}

	@Test
	public void addUser() {
		UserName user1 = new UserName("daniel");
		repository.addUser(user1, new FirstName("d"), new LastName("t"), new Email("test@email.at"));
		User user2 = repository.getUser(user1);
		assertEquals("daniel", user2.getUserName().getName());
		assertEquals("d", user2.getFirstName().getName());
		assertEquals("t", user2.getLastName().getName());
		assertEquals("test@email.at", user2.getEmail().getEmail());
	}
}
