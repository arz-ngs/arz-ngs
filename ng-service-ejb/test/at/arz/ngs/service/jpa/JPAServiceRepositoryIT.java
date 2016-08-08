package at.arz.ngs.service.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.Service;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.exception.ServiceNotFound;

public class JPAServiceRepositoryIT
		extends AbstractJpaIT {

	private ServiceRepository repository;

	private ServiceName serviceName1;
	private ServiceName serviceName2;
	private ServiceName serviceName3;

	private Service service1;
	// private Service service2;
	// private Service service3;

	@Before
	public void setUpBefore() {
		repository = new JPAServiceRepository(getEntityManager());

		serviceName1 = new ServiceName("service1");
		serviceName2 = new ServiceName("service2");
		serviceName3 = new ServiceName("service3");

		// service1 = new Service(serviceName1);
		// service2 = new Service(serviceName2);
		// service3 = new Service(serviceName3);
	}

	@Test
	public void addServices() {
		repository.addService(serviceName1);
		repository.addService(serviceName2);
		repository.addService(serviceName3);
		assertNotNull(repository.getService(serviceName1));
		assertNotNull(repository.getService(serviceName2));
		assertNotNull(repository.getService(serviceName3));
		assertEquals(3, repository.getAllServices().size());
		assertEquals(serviceName2, repository.getService(serviceName2).getServiceName());
	}

	@Test
	public void removeServices() {
		repository.addService(serviceName1);
		repository.addService(serviceName2);
		assertNotNull(repository.getService(serviceName1));
		service1 = repository.getService(serviceName1);
		repository.removeService(service1);
		assertEquals(1, repository.getAllServices().size());
		try {
			repository.getService(serviceName1);
		} catch (ServiceNotFound e) {

		}
	}

	/**
	 * cleanup table entries
	 */
	@After
	public void cleanup() {
		Query d1 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICEINSTANCE");
		d1.executeUpdate();
		Query d2 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICE");
		d2.executeUpdate();
		Query d3 = super.getEntityManager().createNativeQuery("DROP TABLE HOST");
		d3.executeUpdate();
		Query d4 = super.getEntityManager().createNativeQuery("DROP TABLE ENVIRONMENT");
		d4.executeUpdate();
		Query d5 = super.getEntityManager().createNativeQuery("DROP TABLE SCRIPT");
		d5.executeUpdate();

	}

	// @Test
	// public void updateServices() {
	// repository.addService(serviceName1);
	// Service service1 = repository.getService(serviceName1);
	// repository.updateService(service1, serviceName3);
	// Service service1updated = repository.getService(serviceName3);
	// assertEquals(serviceName3, service1updated.getServiceName());
	// assertNotEquals(serviceName1, service1updated.getServiceName());
	// assertEquals(service1.getOid(), service1updated.getOid());
	// }


	// @Test(expected = ServiceNotFoundException.class)
	// public void findServices() {
	// assertEquals(serviceName1, repository.getService(serviceName1).getServiceName());
	// assertEquals(serviceName3, repository.getService(serviceName3).getServiceName());
	//
	// // Exception should be thrown
	// service2 = repository.getService(serviceName2);
	// }

}
