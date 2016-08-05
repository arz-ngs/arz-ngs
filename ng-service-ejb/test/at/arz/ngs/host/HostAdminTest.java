package at.arz.ngs.host;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import at.arz.ngs.HostRepository;
import at.arz.ngs.api.HostName;

@RunWith(MockitoJUnitRunner.class)
public class HostAdminTest {

	@Mock
	private HostRepository repository;

	@InjectMocks
	private HostAdmin admin;

	@Test
	public void testDeleteHost() {
		HostName serverName = new HostName("sample");
		// Host host = new Host(serverName);

		// Mockito.when(repository.getHost(serverName)).thenThrow(new HostNotFoundException());

		// Mockito.when(repository.getHost(serverName)).thenThrow(new HostNotFoundException());


		admin.deleteHost(serverName);

		verify(repository).getHost(serverName);
		verifyNoMoreInteractions(repository);

	}

}
