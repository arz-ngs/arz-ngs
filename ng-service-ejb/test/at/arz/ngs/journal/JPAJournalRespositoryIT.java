package at.arz.ngs.journal;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import at.arz.ngs.AbstractJpaIT;
import at.arz.ngs.api.UserName;
import at.arz.ngs.journal.jpa.JPAJournalRepository;

public class JPAJournalRespositoryIT extends AbstractJpaIT {

	private JournalRepository repository;

	@Before
	public void setUpBefore() {
		repository = new JPAJournalRepository(getEntityManager());
		repository.addJournalEntry(new JournalEntry(new UserName("Daniel"), "ServiceInstance", 51, "test", "Stop"));
	}

	@Test
	public void addJournalEntry() {
		assertEquals(1, repository.getAllJournalEntries().size());
		repository.addJournalEntry(new JournalEntry(new UserName("Daniel"), "ServiceInstance", 51, "test", "Start"));
		assertEquals(2, repository.getAllJournalEntries().size());
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		repository.addJournalEntry(new JournalEntry(new UserName("Daniel"), "ServiceInstance", 51, "test", "Start")); //should work because other timestamp
		assertEquals(3, repository.getAllJournalEntries().size());
	}
}
