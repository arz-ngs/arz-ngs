package at.arz.ngs.journal;

import java.util.List;

public interface JournalRepository {

	void addJournalEntry(JournalEntry journalEntry);
	List<JournalEntry> getAllJournalEntries();
}
