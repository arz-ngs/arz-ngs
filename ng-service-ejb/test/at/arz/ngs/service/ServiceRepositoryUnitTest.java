package at.arz.ngs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import at.arz.ngs.Service;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.exception.ServiceNotFoundException;
import at.arz.ngs.service.jpa.JPAServiceRepository;

public class ServiceRepositoryUnitTest {

	private ServiceRepository repository;
	private EntityManager em;

	private ServiceName serviceName1;
	private ServiceName serviceName2;
	private ServiceName serviceName3;

	private Service service1;
	private Service service2;
	private Service service3;

	@BeforeClass
	public void createServices() {
		repository = new JPAServiceRepository(em);
		serviceName1 = new ServiceName("service1");
		serviceName2 = new ServiceName("service2");
		serviceName3 = new ServiceName("service3");

		service1 = new Service(serviceName1);
		service2 = new Service(serviceName2);
		service3 = new Service(serviceName3);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void addServices() {
		repository.addService(serviceName1);
		repository.addService(serviceName2);
		repository.addService(serviceName3);
	}

	@Test
	public void deleteServices() {
		repository.removeService(service2);
	}

	@Test
	 public void findServices() {
		assertEquals(serviceName1, repository.getService(serviceName1).getServiceName());
		try {
		service2 = repository.getService(serviceName2);
			fail("Exception should be thrown!");
		} catch (ServiceNotFoundException e) {
			// as expected
			e.getMessage();
		}
		assertEquals(serviceName3, repository.getService(serviceName3).getServiceName());
	 }


}

// private EntityManagerFactory initEMF() {
//
// // edit unitName and databaseName
// String jpaUnitName = "MySampleUnit";
// String databaseName = "MySampleDB";
//
// String tmpDir = System.getProperty( "java.io.tmpdir", "." );
// String databaseDirectory = tmpDir + "/" + databaseName;
//
// try {
// EmbeddedDataSource dataSource = new EmbeddedDataSource();
// dataSource.setCreateDatabase( "create" );
// dataSource.setDatabaseName( databaseDirectory );
// Map<String, Serializable> p = new HashMap<String, Serializable>();
// p.put( "openjpa.TransactionMode", "local" );
// p.put( "openjpa.RuntimeUnenhancedClasses", "supported" );
// p.put( "openjpa.DynamicEnhancementAgent", "true" );
// p.put( "openjpa.ConnectionFactoryMode", "local" );
// p.put( "openjpa.ConnectionFactory", dataSource );
// p.put( "openjpa.ConnectionFactory2", dataSource );
// p.put( "openjpa.jdbc.SynchronizeMappings", "buildSchema(SchemaAction='dropDB,add', ForeignKeys=true)" );
//
// return Persistence.createEntityManagerFactory( jpaUnitName, p );
// } catch ( Exception e ) {
// e.printStackTrace();
// fail( e.getMessage() );
// }
// return null;
// }
