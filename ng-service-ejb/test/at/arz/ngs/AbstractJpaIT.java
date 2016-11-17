package at.arz.ngs;

import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractJpaIT {

	private static final String JPA_UNIT_NAME = "ng-service-model";

	private EntityManagerFactory emf;
	private EntityManager em;
	private EntityTransaction tx;

	@Before
	public void before() {
		emf = createEntityManagerFactory();
		em = emf.createEntityManager();
		tx = em.getTransaction();
		tx.begin();
	}

	private EntityManagerFactory createEntityManagerFactory() {
		try {
			Properties connectionProperties = new Properties();
			connectionProperties.load(ClassLoader.getSystemResourceAsStream("at/arz/ngs/connection.properties"));
			EmbeddedDataSource dataSource = new EmbeddedDataSource();
			dataSource.setDatabaseName("memory:ngs-service-db");
			dataSource.setCreateDatabase("create");
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(JPA_UNIT_NAME,
					connectionProperties);
			return entityManagerFactory;
		}
		catch (IOException e) {
			throw new RuntimeException("could not load create entity manager factory", e);
		}
	}

	@After
	public void after() {
		Query d1 = em.createNativeQuery("DROP TABLE SERVICEINSTANCE");
		d1.executeUpdate();
		Query d2 = em.createNativeQuery("DROP TABLE SERVICE");
		d2.executeUpdate();
		Query d3 = em.createNativeQuery("DROP TABLE HOST");
		d3.executeUpdate();
		Query d4 = em.createNativeQuery("DROP TABLE ENVIRONMENT");
		d4.executeUpdate();
		Query d5 = em.createNativeQuery("DROP TABLE SCRIPT");
		d5.executeUpdate();
		Query d7 = em.createNativeQuery("DROP TABLE USER_ROLE");
		d7.executeUpdate();
		Query d75 = em.createNativeQuery("DROP TABLE JOB");
		d75.executeUpdate();
		Query d8 = em.createNativeQuery("DROP TABLE USER_");
		d8.executeUpdate();
		Query d10 = em.createNativeQuery("DROP TABLE PERMISSION_ROLE"); // jpa generated table
		d10.executeUpdate();
		Query d9 = em.createNativeQuery("DROP TABLE ROLE");
		d9.executeUpdate();
		Query d6 = em.createNativeQuery("DROP TABLE PERMISSION");
		d6.executeUpdate();
		Query d11 = em.createNativeQuery("DROP TABLE JOURNALENTRY");
		d11.executeUpdate();

		if (tx != null && tx.isActive() && tx.getRollbackOnly() == false) {
			tx.commit();
		}
		if (em != null) {
			em.close();
		}
		if (emf != null) {
			emf.close();
		}
	}

	public final EntityManager getEntityManager() {
		return em;
	}

	public final void newTx() {
		em.flush();
		tx.commit();
		em.clear();
		tx.begin();
	}

	public final void commitTx() {
		tx.commit();
	}
}
