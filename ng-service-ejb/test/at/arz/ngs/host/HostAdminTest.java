package at.arz.ngs.host;

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
import at.arz.ngs.api.exception.HostNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class HostAdminTest {

	@Mock
	private HostRepository repository;

	@InjectMocks
	private HostAdmin admin;

	@Test
	public void test() {
		HostName serverName = new HostName("sample");
		Host server = new Host(serverName);

		Mockito.when(repository.getHost(serverName)).thenThrow(new HostNotFoundException());

		admin.deleteHost(serverName);
		verify(repository).getHost(serverName);
		verifyNoMoreInteractions(repository);

	}

}
