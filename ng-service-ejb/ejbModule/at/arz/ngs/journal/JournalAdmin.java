package at.arz.ngs.journal;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.api.UserName;
import at.arz.ngs.api.exception.EmptyField;
import at.arz.ngs.journal.commands.get.JournalResponse;
import at.arz.ngs.search.PaginationCondition;
import at.arz.ngs.security.commands.Actor;

@Stateless
public class JournalAdmin {

	@Inject
	private JournalRepository journalRepositoy;

	@Resource
	private SessionContext context;

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
	public JournalAdmin(SessionContext context, JournalRepository journalRepository) {
		this.journalRepositoy = journalRepository;
		this.context = context;
	}

	public List<JournalResponse> getAllJournalEntries() {
		List<JournalEntry> entries = journalRepositoy.getAllJournalEntries();

		return convert(entries);
	}

	public List<JournalResponse> getJournalEntries(PaginationCondition paginationCondition) {
		List<JournalEntry> entries = journalRepositoy.getJournalEntries(getElementsPerPage(paginationCondition),
				getFirstElement(paginationCondition));

		return convert(entries);
	}

	private List<JournalResponse> convert(List<JournalEntry> entries) {
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

	private int getElementsPerPage(PaginationCondition condition) {
		if (condition.getElementsPerPage() < 1) {
			condition.setElementsPerPage(50); // default show 50
		}
		return condition.getElementsPerPage();
	}

	private int getFirstElement(PaginationCondition condition) {
		if (condition.getCurrentPage() < 1) {
			condition.setCurrentPage(1); // default page 1
		}
		int currentFirstElement = (condition.getCurrentPage() - 1) * condition.getElementsPerPage();
		return currentFirstElement;
	}

	public void addJournalEntry(Class<?> targetObject_class, long targetObject_oid, String targetObject_uniqueKey,
			String action) {

		Actor actor = new Actor(context.getCallerPrincipal().getName());
		//TODO for automatic script execution -> try if user can be eligable 
		//		new Actor("automatic script execution")

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

	public int getJournalEntryCount() {
		return journalRepositoy.getJournalEntryCount();
	}

	/**
	 * Only for JUnit Tests!!!
	 * 
	 * @param context
	 */
	public void setContext(SessionContext context) {
		this.context = context;
	}
}
