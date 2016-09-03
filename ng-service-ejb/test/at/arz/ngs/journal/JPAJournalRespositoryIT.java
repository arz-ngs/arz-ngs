package at.arz.ngs.journal;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import javax.persistence.Query;

import org.junit.After;
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
			TimeUnit.MILLISECONDS.sleep(10);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		repository.addJournalEntry(new JournalEntry(new UserName("Daniel"), "ServiceInstance", 51, "test", "Start")); //should work because other timestamp
		assertEquals(3, repository.getAllJournalEntries().size());
	}

	@After
	public void cleanup() {
		Query d1 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICEINSTANCE");
		d1.executeUpdate();
		Query d2 = super.getEntityManager().createNativeQuery("DROP TABLE SERVICE");
		d2.executeUpdate();
		Query d3 = super.getEntityManager().createNativeQuery("DROP TABLE HOST");
		d3.executeUpdate();
		Query d4 = super.getEntityManager().createNativeQuery("DROP TABLE ENVIRONMENT");
		d4.executeUpdate();
		Query d5 = super.getEntityManager().createNativeQuery("DROP TABLE SCRIPT");
		d5.executeUpdate();
		Query d7 = super.getEntityManager().createNativeQuery("DROP TABLE USER_ROLE");
		d7.executeUpdate();
		Query d8 = super.getEntityManager().createNativeQuery("DROP TABLE USER_");
		d8.executeUpdate();
		Query d10 = super.getEntityManager().createNativeQuery("DROP TABLE PERMISSION_ROLE"); // jpa generated table
		d10.executeUpdate();
		Query d9 = super.getEntityManager().createNativeQuery("DROP TABLE ROLE");
		d9.executeUpdate();
		Query d6 = super.getEntityManager().createNativeQuery("DROP TABLE PERMISSION");
		d6.executeUpdate();
		Query d11 = super.getEntityManager().createNativeQuery("DROP TABLE JOURNALENTRY");
		d11.executeUpdate();
	}

}
