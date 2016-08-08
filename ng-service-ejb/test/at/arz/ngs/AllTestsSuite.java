package at.arz.ngs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import at.arz.ngs.host.jpa.JPAHostRepositoryIT;
import at.arz.ngs.service.jpa.JPAServiceRepositoryIT;
import at.arz.ngs.serviceinstance.jpa.JPAServiceInstanceRepositoryIT;

@RunWith(Suite.class)
@SuiteClasses({	JPAHostRepositoryIT.class,
				JPAServiceInstanceRepositoryIT.class,
				JPAServiceRepositoryIT.class })
public class AllTestsSuite {

}
