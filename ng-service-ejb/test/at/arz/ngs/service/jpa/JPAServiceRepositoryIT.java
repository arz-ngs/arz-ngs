package at.arz.ngs.service.jpa;

import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;


public class JPAServiceRepositoryIT
		extends AbstractJpaIT {

	@Test
	public void testGetService() {
		EntityManager entityManager = super.getEntityManager();
		JPAServiceRepository repository = new JPAServiceRepository(entityManager);
		repository.getAllServices();
	}

	@Test
	public void testGetAllServices() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddService() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveService() {
		fail("Not yet implemented");
	}

}
