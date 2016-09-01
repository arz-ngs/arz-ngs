package at.arz.ngs.security.role.jpa;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.management.relation.RoleNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.api.RoleName;
import at.arz.ngs.api.exception.RoleNotFound;
import at.arz.ngs.security.Role;
import at.arz.ngs.security.RoleRepository;

@Dependent
public class JPARoleRepository
		implements RoleRepository {

	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	public JPARoleRepository() {

	}

	/**
	 * Only for JUnit-Tests to use!!
	 * 
	 * @param entityManager
	 */
	public JPARoleRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Role getRole(RoleName roleName) {
		try {
			TypedQuery<Role> getRole = entityManager.createNamedQuery(Role.QUERY_BY_ROLENAME, Role.class);

			getRole.setParameter("rname", roleName);

			return getRole.getSingleResult();

		}
		catch (NoResultException e) {
			throw new RoleNotFound(roleName);
		}
	}

	@Override
	public List<Role> getAllRoles() {
		TypedQuery<Role> allRoles = entityManager.createNamedQuery(Role.QUERY_ALL, Role.class);
		return allRoles.getResultList();
	}

	@Override
	public void addRole(RoleName name) {
		Role role = new Role(name);
		entityManager.persist(role);
	}

	@Override
	public void removeRole(Role role) {
		entityManager.remove(role);
	}

	@Override
	public Role getRoleByOid(long oid) {
		try {
			TypedQuery<Role> role = entityManager.createNamedQuery(Role.QUERY_BY_OID, Role.class);
			role.setParameter("oid", oid);
			return role.getSingleResult();
		} catch(NoResultException e) {
			throw new RoleNotFound(oid);
		}
	}

}
