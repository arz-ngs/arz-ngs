package at.arz.ngs.security.user.jpa;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.api.UserName;
import at.arz.ngs.api.exception.UserNotFound;
import at.arz.ngs.security.User;
import at.arz.ngs.security.UserRepository;

@Dependent
public class JPAUserRepository
		implements UserRepository {

	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	public JPAUserRepository() {

	}

	/**
	 * Only for JUnit-Tests to use!!
	 * 
	 * @param entityManager
	 */
	public JPAUserRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public User getUser(UserName userName) {
		try {
			TypedQuery<User> getUser = entityManager.createNamedQuery(User.QUERY_BY_USERNAME, User.class);

			getUser.setParameter("uname", userName);

			return getUser.getSingleResult();
		} catch (NoResultException e) {
			throw new UserNotFound(userName);
		}
	}

	@Override
	public List<User> getAllUsers() {
		TypedQuery<User> allUsers = entityManager.createNamedQuery(User.QUERY_ALL, User.class);
		return allUsers.getResultList();
	}

	@Override
	public void addUser(UserName name) {
		User user = new User(name);
		entityManager.persist(user);
	}

	@Override
	public void removeUser(User user) {
		entityManager.remove(user);
	}
}
