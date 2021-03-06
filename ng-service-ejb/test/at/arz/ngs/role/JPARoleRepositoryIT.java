package at.arz.ngs.role;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.api.RoleName;
import at.arz.ngs.security.Role;
import at.arz.ngs.security.RoleRepository;
import at.arz.ngs.security.role.jpa.JPARoleRepository;

public class JPARoleRepositoryIT extends AbstractJpaIT {

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
}
