package at.arz.ngs.server;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.HostNotFound;
import at.arz.ngs.host.HostAdmin;

@RunWith(MockitoJUnitRunner.class)
public class ServerAdminTest {

	@Mock
	private HostRepository repository;

	@InjectMocks
	private HostAdmin admin;

	@Test
	public void test() {
		HostName serverName = new HostName("sample");
		Host server = new Host(serverName);

		Mockito.when(repository.findServer(serverName)).thenThrow(new HostNotFound());

		admin.deleteServer(serverName);
		verify(repository).findServer(serverName);
		verifyNoMoreInteractions(repository);

	}

}
