package at.arz.ngs.permission;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.api.Action;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.exception.PermissionNotFound;
import at.arz.ngs.security.Permission;
import at.arz.ngs.security.PermissionRepository;
import at.arz.ngs.security.permission.jpa.JPAPermissionRepository;

public class JPAPermissionRepositoryIT
		extends AbstractJpaIT {

	private PermissionRepository repository;

	@Before
	public void setUpBefore() {
		repository = new JPAPermissionRepository(getEntityManager());

		repository.addPermission(new EnvironmentName("env1"), new ServiceName("serv1"), Action.restart);
	}

	@Test
	public void addPermissionTest() {
		repository.addPermission(new EnvironmentName("env2"), new ServiceName("serv2"), Action.all);
	}

	@Test
	public void getPermissionTest() {
		Permission permission = repository.getPermission(	new EnvironmentName("env1"),
															new ServiceName("serv1"),
															Action.restart);

		assertEquals(Action.restart, permission.getAction());
		assertEquals(new EnvironmentName("env1"), permission.getEnvironmentName());
		assertEquals(new ServiceName("serv1"), permission.getServiceName());
	}

	@Test
	public void removePermissionTest() {
		System.err.println("before removal");
		Permission toRemove = repository.getPermission(	new EnvironmentName("env1"),
														new ServiceName("serv1"),
														Action.restart);
		repository.removePermission(toRemove);

		try {
			repository.getPermission(new EnvironmentName("env1"), new ServiceName("serv1"), Action.restart);
			fail();
		} catch (PermissionNotFound e) {
			// ok
		}
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
