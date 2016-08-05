package at.arz.ngs.environment.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.arz.ngs.Environment;
import at.arz.ngs.EnvironmentRepository;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.exception.EnvironmentNotFoundException;
import at.arz.ngs.api.exception.JPAException;

public class JPAEnvironmentRepository
		implements EnvironmentRepository {

	private EntityManager entityManager;

	public JPAEnvironmentRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Environment getEnvironment(EnvironmentName environmentName) {
		entityManager.getTransaction().begin();
		try {
			TypedQuery<Environment> getEnv = entityManager.createNamedQuery("getEnvironment", Environment.class);
			getEnv.setParameter("ename", environmentName);
			Environment result = getEnv.getSingleResult();

			entityManager.getTransaction().commit();

			return result;
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new EnvironmentNotFoundException();
		}
	}

	@Override
	public List<Environment> getAllEnvironments() {
		entityManager.getTransaction().begin();
		try {
			TypedQuery<Environment> getEnv = entityManager.createNamedQuery("getAllEnvironments", Environment.class);
			List<Environment> resultList = getEnv.getResultList();

			entityManager.getTransaction().commit();

			return resultList;
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void addEnvironment(EnvironmentName environmentName) {
		entityManager.getTransaction().begin();
		try {
			Environment environment = new Environment(environmentName);
			entityManager.persist(environment);

			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void removeEnvironment(Environment environment) {
		entityManager.getTransaction().begin();
		try {
			entityManager.remove(environment);

			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void updateEnvironment(Environment oldEnvironment, EnvironmentName newEnvironmentName) {
		entityManager.getTransaction().begin();
		try {
			oldEnvironment.setEnvironmentName(newEnvironmentName);

			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

}
