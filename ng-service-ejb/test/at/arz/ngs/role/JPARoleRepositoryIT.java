package at.arz.ngs.role;

import static org.junit.Assert.assertEquals;

import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.api.RoleName;
import at.arz.ngs.security.Role;
import at.arz.ngs.security.RoleRepository;
import at.arz.ngs.security.role.jpa.JPARoleRepository;

public class JPARoleRepositoryIT extends AbstractJpaIT{

	private RoleRepository role;

	@Before
	public void setUpBefore() {
		role = new JPARoleRepository(getEntityManager());

	}

	@Test
	public void addRole() {
		RoleName role1 = new RoleName("entwickler");
		role.addRole(role1);
		Role role2 = role.getRole(role1);
		assertEquals("entwickler", role2.getRoleName().toString());
	}
	
	@Test
	public void removeRole() {
		RoleName role1 = new RoleName("test");
		role.addRole(role1);
		Role role2 = role.getRole(new RoleName("test"));
		assertEquals(1, role.getAllRoles().size());
		role.removeRole(role2);
		assertEquals(0, role.getAllRoles().size());
		
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
