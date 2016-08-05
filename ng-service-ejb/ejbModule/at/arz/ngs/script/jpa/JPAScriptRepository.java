package at.arz.ngs.script.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.arz.ngs.Script;
import at.arz.ngs.ScriptRepository;
import at.arz.ngs.api.PathRestart;
import at.arz.ngs.api.PathStart;
import at.arz.ngs.api.PathStatus;
import at.arz.ngs.api.PathStop;
import at.arz.ngs.api.ScriptName;
import at.arz.ngs.api.exception.JPAException;

public class JPAScriptRepository
		implements ScriptRepository {

	private EntityManager entityManager;

	public JPAScriptRepository(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public Script getScript(ScriptName scriptName) {
		entityManager.getTransaction().begin();
		try {
			TypedQuery<Script> getScript = entityManager.createNamedQuery("getScript", Script.class);
			getScript.setParameter("scname", scriptName);
			Script result = getScript.getSingleResult();

			entityManager.getTransaction().commit();

			return result;
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public List<Script> getAllScripts() {
		entityManager.getTransaction().begin();
		try {
			TypedQuery<Script> getAllScripts = entityManager.createNamedQuery("getAllScripts", Script.class);
			List<Script> resultList = getAllScripts.getResultList();

			entityManager.getTransaction().commit();

			return resultList;
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void addScript(	ScriptName scriptName,
							PathStart pathStart,
							PathStop pathStop,
							PathRestart pathRestart,
							PathStatus pathStatus) {

		entityManager.getTransaction().begin();
		try {
			Script script = new Script(scriptName, pathStart, pathStop, pathRestart, pathStatus);
			entityManager.persist(script);

			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void removeScript(Script script) {
		entityManager.getTransaction().begin();
		try {
			entityManager.remove(script);

			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}

	@Override
	public void updateScript(	Script oldScript,
								ScriptName newScriptName,
								PathStart newPathStart,
								PathStop newPathStop,
								PathRestart newPathRestart,
								PathStatus newPathStatus) {

		entityManager.getTransaction().begin();
		try {
			oldScript.setScriptName(newScriptName);
			oldScript.setPathStart(newPathStart);
			oldScript.setPathStop(newPathStop);
			oldScript.setPathRestart(newPathRestart);
			oldScript.setPathStatus(newPathStatus);

			entityManager.getTransaction().commit();
		} catch (RuntimeException e) {
			entityManager.getTransaction().rollback();
			throw new JPAException(e.getMessage());
		}
	}
}
