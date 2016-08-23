package at.arz.ngs.user_role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.api.Email;
import at.arz.ngs.api.FirstName;
import at.arz.ngs.api.LastName;
import at.arz.ngs.api.RoleName;
import at.arz.ngs.api.UserName;
import at.arz.ngs.api.exception.User_RoleNotFound;
import at.arz.ngs.security.Role;
import at.arz.ngs.security.RoleRepository;
import at.arz.ngs.security.User;
import at.arz.ngs.security.UserRepository;
import at.arz.ngs.security.User_Role;
import at.arz.ngs.security.User_RoleRepository;
import at.arz.ngs.security.role.jpa.JPARoleRepository;
import at.arz.ngs.security.user.jpa.JPAUserRepository;
import at.arz.ngs.security.userrole.jpa.JPAUser_RoleRepository;


public class JPAUser_RoleRepositoryIT
		extends AbstractJpaIT {

	private User_RoleRepository userRoleRepository;
	private UserRepository userRepository;
	private RoleRepository roleRepository;

	@Before
	public void setUpBeforeClass() {
		userRoleRepository = new JPAUser_RoleRepository(getEntityManager());
		userRepository = new JPAUserRepository(getEntityManager());
		roleRepository = new JPARoleRepository(getEntityManager());

		UserName userName1 = new UserName("dani");
		userRepository.addUser(userName1, new FirstName(""), new LastName(""), new Email(""));
		RoleName roleName1 = new RoleName("entwickler");
		roleRepository.addRole(roleName1);
		User user1 = userRepository.getUser(userName1);
		Role role1 = roleRepository.getRole(roleName1);
		userRoleRepository.addUser_Role(user1, role1, true);
	}

	@Test
	public void addUserRole() {
		UserName userName1 = new UserName("dani1");
		userRepository.addUser(userName1, new FirstName(""), new LastName(""), new Email(""));
		RoleName roleName1 = new RoleName("entwickler1");
		roleRepository.addRole(roleName1);
		User user1 = userRepository.getUser(userName1);
		Role role1 = roleRepository.getRole(roleName1);
		userRoleRepository.addUser_Role(user1, role1, true);
		
		User_Role userRole1 = userRoleRepository.getUser_Role(user1, role1);
		assertEquals("dani1", userRole1.getUser().getUserName().toString());
		assertEquals("entwickler1", userRole1.getRole().getRoleName().toString());
		assertEquals(true, userRole1.isHandover());
	}

	@Test
	public void deleteUserRole() {
		User user1 = userRepository.getUser(new UserName("dani"));
		Role role1 = roleRepository.getRole(new RoleName("entwickler"));
		User_Role user_Role = userRoleRepository.getUser_Role(user1, role1);
		assertNotNull(user_Role);
		userRoleRepository.removeUser_Role(user_Role);
		try {
			userRoleRepository.getUser_Role(user1, role1);
		} catch (User_RoleNotFound e) {
			//wanted
		}
	}

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
