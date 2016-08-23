package at.arz.ngs.user.jpa;

import static org.junit.Assert.assertEquals;

import javax.persistence.Query;

import org.junit.After;
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

public class JPAUserRepositoryIT extends AbstractJpaIT{

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

	/**
	 * cleanup table entries
	 */
	@After
	public void cleanup() {
		Query d1 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICEINSTANCE");
		d1.executeUpdate();
		Query d2 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICE");
		d2.executeUpdate();
		Query d3 = super.getEntityManager().createNativeQuery("DROP TABLE HOST");
		d3.executeUpdate();
		Query d4 = super.getEntityManager().createNativeQuery("DROP TABLE ENVIRONMENT");
		d4.executeUpdate();
		Query d5 = super.getEntityManager().createNativeQuery("DROP TABLE SCRIPT");
		d5.executeUpdate();
		Query d7 = super.getEntityManager().createNativeQuery("DROP TABLE USER_ROLE");
		d7.executeUpdate();
		Query d8 = super.getEntityManager().createNativeQuery("DROP TABLE USER_");
		d8.executeUpdate();
		Query d10 = super.getEntityManager().createNativeQuery("DROP TABLE PERMISSION_ROLE"); // jpa generated table
		d10.executeUpdate();
		Query d9 = super.getEntityManager().createNativeQuery("DROP TABLE ROLE");
		d9.executeUpdate();
		Query d6 = super.getEntityManager().createNativeQuery("DROP TABLE PERMISSION");
		d6.executeUpdate();
	}
}
