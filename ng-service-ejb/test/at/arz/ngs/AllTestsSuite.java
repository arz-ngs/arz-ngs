package at.arz.ngs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import at.arz.ngs.host.HostRepositoryUnitTest;
import at.arz.ngs.serviceinstance.ServiceInstanceRepositoryUnitTest;

@RunWith(Suite.class)
@SuiteClasses({	HostRepositoryUnitTest.class,
				ServiceInstanceRepositoryUnitTest.class,
				ServiceInstanceRepositoryUnitTest.class })
public class AllTestsSuite {

}
