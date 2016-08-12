package at.arz.ngs.environment.jpa;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import at.arz.ngs.Environment;
import at.arz.ngs.EnvironmentRepository;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.exception.EnvironmentNotFound;

@Dependent
public class JPAEnvironmentRepository
		implements EnvironmentRepository {

	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	public JPAEnvironmentRepository() {
		// ejb constructor
	}

	/**
	 * Only for JUnit-Tests to use!!
	 * 
	 * @param entityManager
	 */
	public JPAEnvironmentRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Environment getEnvironment(EnvironmentName environmentName) {
		try {
			TypedQuery<Environment> getEnv = entityManager.createNamedQuery(Environment.QUERY_BY_ENVIRONMENTNAME,
																			Environment.class);
			getEnv.setParameter("ename", environmentName);

			return getEnv.getSingleResult();

		} catch (NoResultException e) {
			throw new EnvironmentNotFound(environmentName);
		}
	}

	@Override
	public List<Environment> getAllEnvironments() {
		TypedQuery<Environment> getEnv = entityManager.createNamedQuery(Environment.QUERY_ALL, Environment.class);
		return getEnv.getResultList();
	}

	@Override
	public void addEnvironment(EnvironmentName environmentName) {
			Environment environment = new Environment(environmentName);
			entityManager.persist(environment);
	}

	@Override
	public void removeEnvironment(Environment environment) {
			entityManager.remove(environment);
	}

	@Override
	public void removeUnusedEnvironments() {
		Query query =
					entityManager.createQuery("DELETE FROM Environment e WHERE e NOT IN (SELECT i.environment FROM ServiceInstance i)");
		int count = query.executeUpdate();
		System.out.println(count + " unused environments removed");
	}
}
