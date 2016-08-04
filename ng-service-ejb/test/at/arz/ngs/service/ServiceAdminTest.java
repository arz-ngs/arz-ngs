package at.arz.ngs.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import at.arz.ngs.ServiceRepository;

@RunWith(MockitoJUnitRunner.class)

public class ServiceAdminTest {

	@Mock
	private ServiceRepository repository;

	@InjectMocks
	private ServiceAdmin admin;

	@Test
	public void test() {

	}

}
