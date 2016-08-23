package at.arz.ngs.security.userrole.jpa;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.api.exception.User_RoleNotFound;
import at.arz.ngs.security.Role;
import at.arz.ngs.security.User;
import at.arz.ngs.security.User_Role;
import at.arz.ngs.security.User_RoleRepository;

@Dependent
public class JPAUser_RoleRepository
		implements User_RoleRepository
{

	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	public JPAUser_RoleRepository() {
		// jpa constructor
	}

	/**
	 * Only for JUnit-Tests to use!!
	 * 
	 * @param entityManager
	 */
	public JPAUser_RoleRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void addUser_Role(User user, Role role, boolean handover) {
		User_Role userRole = new User_Role(user, role, handover);
		user.addUser_Role(userRole);
		entityManager.persist(userRole);
	}

	@Override
	public User_Role getUser_Role(User user, Role role) {
		try {
			TypedQuery<User_Role> getUserRole = entityManager.createNamedQuery(	User_Role.QUERY_BY_USER_ROLE,
																			User_Role.class);
		getUserRole.setParameter("uruser", user);
		getUserRole.setParameter("urrole", role);
			return getUserRole.getSingleResult();
		} catch (NoResultException e) {
			throw new User_RoleNotFound(user.getUserName(), role.getRoleName());
		}
	}

	@Override
	public void removeUser_Role(User_Role user_Role) {
		entityManager.remove(user_Role);
	}

}
