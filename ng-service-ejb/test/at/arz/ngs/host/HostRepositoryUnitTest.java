package at.arz.ngs.host;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.exception.HostNotFoundException;
import at.arz.ngs.host.jpa.JPAHostRepository;

public class HostRepositoryUnitTest
		extends AbstractJpaIT {

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
		assertEquals(hostName1, repository.getHost(hostName1));
		assertEquals(2, repository.getAllHosts().size());
	}

	@Test
	public void removeHosts() {
		repository.addHost(hostName1);
		repository.addHost(hostName2);
		Host host2 = new Host(hostName2);
		repository.removeHost(host2);
		assertEquals(1, repository.getAllHosts().size());
		try {
			repository.getHost(hostName2);
		} catch (HostNotFoundException e) {
			
		}
	}

	@Test
	public void updateHosts() {
		repository.addHost(hostName1);
		Host host1 = repository.getHost(hostName1);
		repository.updateHost(host1, hostName2);
		Host host1updated = repository.getHost(hostName1);
		assertEquals(host1.getOid(), host1updated.getOid());
		assertNotEquals(host1.getHostName(), host1updated.getHostName());
	}

}
