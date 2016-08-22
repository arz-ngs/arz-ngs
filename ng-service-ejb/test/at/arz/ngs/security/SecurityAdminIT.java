package at.arz.ngs.security;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.security.permission.jpa.JPAPermissionRepository;
import at.arz.ngs.security.role.jpa.JPARoleRepository;
import at.arz.ngs.security.user.jpa.JPAUserRepository;

public class SecurityAdminIT
		extends AbstractJpaIT {

	private SecurityAdmin admin;
	private PermissionRepository permissionRepository;
	private RoleRepository roleRepository;
	private UserRepository userRepository;

	@Before
	public void setUpBeforeClass() {
		permissionRepository = new JPAPermissionRepository(getEntityManager());
		roleRepository = new JPARoleRepository(getEntityManager());
		userRepository = new JPAUserRepository(getEntityManager());
		admin = new SecurityAdmin(permissionRepository,
									roleRepository,
									userRepository);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
