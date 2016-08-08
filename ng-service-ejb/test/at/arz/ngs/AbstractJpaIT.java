package at.arz.ngs;

import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(	JPA_UNIT_NAME,
																								connectionProperties);
			return entityManagerFactory;
		} catch (IOException e) {
			throw new RuntimeException("could not load create entity manager factory", e);
		}
	}

	@After
	public void after() {
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
