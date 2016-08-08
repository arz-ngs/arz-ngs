package at.arz.ngs.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import at.arz.ngs.Service;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.ServiceName;

@RunWith(MockitoJUnitRunner.class)

public class ServiceAdminTest {

	@Mock
	private ServiceRepository repository;

	@InjectMocks
	private ServiceAdmin admin;

	@Test
	public void test() {
		ServiceName oldName = new ServiceName("oldName");
		ServiceName newName = new ServiceName("newName");
		Service service = new Service(oldName);
		when(repository.getService(oldName)).thenReturn(service);
		admin.renameService(oldName, newName);
		assertThat(service.renameService(), is(newName));
	}

}
