package at.arz.ngs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ ServerFinderTest.class, ServiceRepositoryTest.class })
public class AllTestsSuite {

}
