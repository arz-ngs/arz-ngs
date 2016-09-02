package at.arz.ngs.journal.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.arz.ngs.journal.JournalEntry;
import at.arz.ngs.journal.JournalRepository;

public class JPAJournalRepository
		implements JournalRepository {

	@PersistenceContext(unitName = "ng-service-model")
	private EntityManager entityManager;

	protected JPAJournalRepository() {
		//ejb constructor
	}

	/**
	 * Just for JUnit Tests!
	 * 
	 * @param entityManager
	 */
	public JPAJournalRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void addJournalEntry(JournalEntry journalEntry) {
		entityManager.persist(journalEntry);
	}

	@Override
	public List<JournalEntry> getAllJournalEntries() {
		TypedQuery<JournalEntry> entries = entityManager.createNamedQuery(JournalEntry.QUERY_ALL, JournalEntry.class);
		return entries.getResultList();
	}

}
