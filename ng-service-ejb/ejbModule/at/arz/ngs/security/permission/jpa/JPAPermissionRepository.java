package at.arz.ngs.security.permission.jpa;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.api.Action;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.exception.PermissionNotFound;
import at.arz.ngs.security.Permission;
import at.arz.ngs.security.PermissionRepository;

@Dependent
public class JPAPermissionRepository
		implements PermissionRepository {

	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	public JPAPermissionRepository() {

	}

	/**
	 * Only for JUnit-Tests to use!!
	 * 
	 * @param entityManager
	 */
	public JPAPermissionRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Permission getPermission(EnvironmentName environmentName, ServiceName serviceName, Action action) {
		try {
			TypedQuery<Permission> getPermission = entityManager
					.createNamedQuery(Permission.QUERY_BY_ENVIRONMENTandSERVICEandACTION, Permission.class);

			getPermission.setParameter("ename", environmentName);
			getPermission.setParameter("sname", serviceName);
			getPermission.setParameter("action", action);

			return getPermission.getSingleResult();

		}
		catch (NoResultException e) {
			throw new PermissionNotFound(environmentName, serviceName, action);
		}
	}

	@Override
	public void addPermission(EnvironmentName environmentName, ServiceName serviceName, Action action) {
		Permission permission = new Permission(environmentName, serviceName, action);
		entityManager.persist(permission);
	}

	@Override
	public void removePermission(Permission permission) {
		entityManager.remove(permission);
	}
}
