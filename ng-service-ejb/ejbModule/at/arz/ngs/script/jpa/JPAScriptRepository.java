package at.arz.ngs.script.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.Script;
import at.arz.ngs.ScriptRepository;
import at.arz.ngs.api.PathRestart;
import at.arz.ngs.api.PathStart;
import at.arz.ngs.api.PathStatus;
import at.arz.ngs.api.PathStop;
import at.arz.ngs.api.ScriptName;
import at.arz.ngs.api.exception.ScriptNotFound;

@Stateless
@Local(ScriptRepository.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class JPAScriptRepository
		implements ScriptRepository {

	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	public JPAScriptRepository() {
		// ejb constructor
	}

	/**
	 * Only for JUnit-Tests to use!!
	 * 
	 * @param entityManager
	 */
	public JPAScriptRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Script getScript(ScriptName scriptName) {
		try {
			TypedQuery<Script> getScript = entityManager.createNamedQuery(Script.QUERY_BY_SCRIPTNAME, Script.class);
			getScript.setParameter("scname", scriptName);
			return getScript.getSingleResult();

		} catch (RuntimeException e) {
			throw new ScriptNotFound(scriptName);
		}
	}

	@Override
	public List<Script> getAllScripts() {
			TypedQuery<Script> getAllScripts = entityManager.createNamedQuery("getAllScripts", Script.class);
		return getAllScripts.getResultList();
	}

	@Override
	public void addScript(	ScriptName scriptName,
							PathStart pathStart,
							PathStop pathStop,
							PathRestart pathRestart,
							PathStatus pathStatus) {

			Script script = new Script(scriptName, pathStart, pathStop, pathRestart, pathStatus);
			entityManager.persist(script);
	}

	@Override
	public void removeScript(Script script) {
			entityManager.remove(script);
	}
}
