package at.arz.ngs.journal;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import at.arz.ngs.api.UserName;
import at.arz.ngs.api.exception.EmptyField;
import at.arz.ngs.journal.commands.get.JournalResponse;
import at.arz.ngs.security.commands.Actor;

public class JournalAdmin {

	@Inject
	private JournalRepository journalRepositoy;

	protected JournalAdmin() {
		//ejb constructor
	}

	/**
	 * Only for JUnit Test!!
	 * 
	 * @param journalRepository
	 * @param serviceInstanceRepository
	 * @param roleRepository
	 */
	public JournalAdmin(JournalRepository journalRepository) {
		this.journalRepositoy = journalRepository;
	}

	public List<JournalResponse> getAllJournalEntries() {
		List<JournalEntry> entries = journalRepositoy.getAllJournalEntries();
		List<JournalResponse> response = new LinkedList<>();
		for (JournalEntry entry : entries) {
			JournalResponse res = new JournalResponse();
			res.setTime(entry.getTime());
			res.setUserName(entry.getUserName().getName());
			res.setTargetObject_class(entry.getTargetObject_class());
			res.setAction(entry.getAction());
			res.setTargetObject_uniqueKey(entry.getTargetObject_uniqueKey());

			response.add(res);
		}
		return response;
	}

	public void addJournalEntry(Actor actor, Class<?> targetObject_class, long targetObject_oid,
			String targetObject_uniqueKey, String action) {
		if (actor == null || actor.getUserName().trim().equals("")) {
			throw new EmptyField("To add an journal entry an actor must be set!");
		}
		if (targetObject_class == null) {
			throw new EmptyField("To add an journal entry a targetObject-class must be set!");
		}
		if (targetObject_uniqueKey == null || targetObject_uniqueKey.trim().equals("")) {
			throw new EmptyField("To add an journal entry a targetObject-uniqueKey must be set!");
		}
		if (action == null || action.trim().equals("")) {
			throw new EmptyField("To add an journal entry an action must be set!");
		}

		String clazz = targetObject_class.getSimpleName(); //can be changed to persist full name

		JournalEntry entry = new JournalEntry(new UserName(actor.getUserName()), clazz, targetObject_oid,
				targetObject_uniqueKey, action);
		journalRepositoy.addJournalEntry(entry);
	}
}
