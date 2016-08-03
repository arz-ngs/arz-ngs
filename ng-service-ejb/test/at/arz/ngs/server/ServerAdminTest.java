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
import at.arz.ngs.api.ServerName;
import at.arz.ngs.api.ServerNotFound;

@RunWith(MockitoJUnitRunner.class)
public class ServerAdminTest {

	@Mock
	private HostRepository repository;

	@InjectMocks
	private ServerAdmin admin;

	@Test
	public void test() {
		ServerName serverName = new ServerName("sample");
		Host server = new Host(serverName);

		Mockito.when(repository.findServer(serverName)).thenThrow(new ServerNotFound());

		admin.deleteServer(serverName);
		verify(repository).findServer(serverName);
		verifyNoMoreInteractions(repository);

	}

}
