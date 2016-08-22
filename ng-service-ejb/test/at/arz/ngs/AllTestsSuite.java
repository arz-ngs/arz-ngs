package at.arz.ngs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import at.arz.ngs.host.jpa.JPAHostRepositoryIT;
import at.arz.ngs.permission.JPAPermissionRepositoryIT;
import at.arz.ngs.role.JPARoleRepositoryIT;
import at.arz.ngs.search.SearchEngineIT;
import at.arz.ngs.service.jpa.JPAServiceRepositoryIT;
import at.arz.ngs.serviceinstance.jpa.JPAServiceInstanceRepositoryIT;
import at.arz.ngs.serviceinstance.jpa.ServiceInstanceAdminIT;
import at.arz.ngs.user.jpa.JPAUserRepositoryIT;
import at.arz.ngs.userrole.JPAUser__RoleRepositoryIT;

@RunWith(Suite.class)
@SuiteClasses({	JPAHostRepositoryIT.class,
				JPAServiceInstanceRepositoryIT.class,
				JPAServiceRepositoryIT.class,
				SearchEngineIT.class,
				ServiceInstanceAdminIT.class,
				JPAPermissionRepositoryIT.class,
				JPARoleRepositoryIT.class,
				JPAUserRepositoryIT.class,
				JPAUser__RoleRepositoryIT.class })
public class AllTestsSuite {

}
