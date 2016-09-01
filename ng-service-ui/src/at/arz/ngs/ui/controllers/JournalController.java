package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.journal.JournalAdmin;
import at.arz.ngs.journal.JournalResponse;

@RequestScoped
@Named("journal")
public class JournalController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private JournalAdmin admin;
	
	private List<JournalResponse> response;
	
	@PostConstruct
	public void init() {
		response = admin.getAllJournalEntries();
	}

	public List<JournalResponse> getResponse() {
		return response;
	}
	
	public String goToJournal() {
		return "journal";
	}

}
