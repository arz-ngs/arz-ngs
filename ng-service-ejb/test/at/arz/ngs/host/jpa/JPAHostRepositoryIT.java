package at.arz.ngs.host.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.exception.HostNotFound;

public class JPAHostRepositoryIT extends AbstractJpaIT {

	private HostRepository repository;

	private HostName hostName1 = new HostName("host1");
	private HostName hostName2 = new HostName("host2");

	@Before
	public void setUpBefore() {
		repository = new JPAHostRepository(getEntityManager());
	}

	@Test
	public void addHosts() {
		repository.addHost(hostName1);
		repository.addHost(hostName2);
		assertNotNull(repository.getHost(hostName1));
		assertNotNull(repository.getHost(hostName2));
		assertEquals(hostName1, repository.getHost(hostName1).getHostName());
		assertEquals(2, repository.getAllHosts().size());
	}

	@Test
	public void removeHosts() {
		repository.addHost(hostName1);
		repository.addHost(hostName2);
		Host host2 = repository.getHost(hostName2);
		repository.removeHost(host2);
		assertEquals(1, repository.getAllHosts().size());
		try {
			repository.getHost(hostName2);
			fail();
		}
		catch (HostNotFound e) {

		}
	}
}
