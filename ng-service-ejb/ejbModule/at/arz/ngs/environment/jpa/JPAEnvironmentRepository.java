package at.arz.ngs.environment.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.Environment;
import at.arz.ngs.EnvironmentRepository;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.exception.EnvironmentNotFound;

@Stateless
@Local(EnvironmentRepository.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
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
}
