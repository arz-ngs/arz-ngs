package at.arz.ngs.script;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@EJB
public class ScriptAdmin {

	@PersistenceContext
	private EntityManager entityManager;
}
