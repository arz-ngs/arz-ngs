package at.arz.ngs.user;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.User;
import at.arz.ngs.UserRepository;
import at.arz.ngs.api.UserName;
import at.arz.ngs.api.exception.UserNotFound;

public class JPAUserRepository implements UserRepository{
	
	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	public JPAUserRepository() {
		
	}
	
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

}
