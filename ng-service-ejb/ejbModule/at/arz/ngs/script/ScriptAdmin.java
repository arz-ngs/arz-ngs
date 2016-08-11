package at.arz.ngs.script;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ScriptAdmin {

	@PersistenceContext
	private EntityManager entityManager;
}
