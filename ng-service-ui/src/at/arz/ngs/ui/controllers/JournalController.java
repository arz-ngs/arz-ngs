package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.journal.JournalAdmin;
import at.arz.ngs.journal.commands.get.JournalResponse;
import at.arz.ngs.search.PaginationCondition;
import at.arz.ngs.ui.data_collections.PaginationCollection;

@ViewScoped
@Named("journal")
public class JournalController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private JournalAdmin admin;

	private List<JournalResponse> response;

	private PaginationCondition pagination;
	private PaginationCollection paginationCollection;
	private int numElementsFound;

	@PostConstruct
	public void init() {
		pagination = new PaginationCondition(50, 1); // default is first page with 50 elements
		paginationCollection = new PaginationCollection();

		formSubmit();
	}

	/**
	 * This method should be invoked when someone presses Enter on the overview
	 * (sets the current page to the first one)
	 */
	public void enterFormSubmit() {
		pagination.setCurrentPage(1);
		formSubmit();
	}

	public void formSubmit() {
		response = admin.getJournalEntries(pagination);
		numElementsFound = admin.getJournalEntryCount();

		doPaginationValidation();
	}

	public void performPagination(String newPage) {
		try {
			int page = new Integer(newPage);
			pagination.setCurrentPage(page);
		}
		catch (Exception e) {
			if (newPage.equals("_lt")) {
				pagination.setCurrentPage(pagination.getCurrentPage() - 1);
			}
			else if (newPage.equals("_gt")) {
				pagination.setCurrentPage(pagination.getCurrentPage() + 1);
			}
			else {
				throw e;
			}
		}

		formSubmit();
	}

	/**
	 * Computes the fields in the pagination (ui)
	 */
	private void doPaginationValidation() {
		int overallElementCount = numElementsFound;

		int currentPage = pagination.getCurrentPage();
		int elemPerPage = pagination.getElementsPerPage();
		int lastPage = 0;
		if (overallElementCount % elemPerPage == 0) {
			lastPage = overallElementCount / elemPerPage;
		}
		else {
			lastPage = (overallElementCount / elemPerPage) + 1;
		}

		paginationCollection.setLeftCaretClass(currentPage == 1 ? PaginationCollection.DISABLED : null);
		paginationCollection.setLeftCaretDisabled(currentPage == 1);
		paginationCollection.setRightCaretClass(currentPage == lastPage ? PaginationCollection.DISABLED : null);
		paginationCollection.setRightCaretDisabled(currentPage == lastPage);

		int numPages = lastPage;

		if (numPages == 1 || numPages == 0) {
			paginationCollection.setShowSecondElem(false);
			paginationCollection.setShowThirdElem(false);
			paginationCollection.setShowFourthElem(false);
			paginationCollection.setShowFifthElem(false);

			paginationCollection.setSecondElement("-1"); // not shown here
			paginationCollection.setThirdElement("-1");
			paginationCollection.setFourthElement("-1");
			paginationCollection.setFithElement("-1");
		}
		else if (numPages == 2) {
			paginationCollection.setShowSecondElem(false);
			paginationCollection.setShowThirdElem(false);
			paginationCollection.setShowFourthElem(false);
			paginationCollection.setShowFifthElem(true);

			paginationCollection.setSecondElement("-1");
			paginationCollection.setThirdElement("-1");
			paginationCollection.setFourthElement("-1");
		}
		else if (numPages == 3) {
			paginationCollection.setShowSecondElem(false);
			paginationCollection.setShowThirdElem(false);
			paginationCollection.setShowFourthElem(true);
			paginationCollection.setShowFifthElem(true);

			paginationCollection.setSecondElement("-1");
			paginationCollection.setThirdElement("-1");
			paginationCollection.setFourthElement("2");
		}
		else if (numPages == 4) {
			paginationCollection.setShowSecondElem(false);
			paginationCollection.setShowThirdElem(true);
			paginationCollection.setShowFourthElem(true);
			paginationCollection.setShowFifthElem(true);

			paginationCollection.setSecondElement("-1");
			paginationCollection.setThirdElement("2");
			paginationCollection.setFourthElement("3");
		}
		else if (numPages >= 5) {
			paginationCollection.setShowSecondElem(true);
			paginationCollection.setShowThirdElem(true);
			paginationCollection.setShowFourthElem(true);
			paginationCollection.setShowFifthElem(true);
			// don't hide anything

			if (currentPage == 1 || currentPage == 2) {
				paginationCollection.setSecondElement("2");
				paginationCollection.setThirdElement("3");
				paginationCollection.setFourthElement("4");
			}
			else if (currentPage == lastPage || currentPage == lastPage - 1) {
				paginationCollection.setFourthElement((lastPage - 1) + "");
				paginationCollection.setThirdElement((lastPage - 2) + "");
				paginationCollection.setSecondElement((lastPage - 3) + "");
			}
			else { // if the current page is somewhere in the middle
				paginationCollection.setSecondElement((currentPage - 1) + "");
				paginationCollection.setThirdElement(currentPage + "");
				paginationCollection.setFourthElement((currentPage + 1) + "");
			}
		}

		paginationCollection.setFithElement(lastPage + "");

		// now highlight current page to be active

		if (paginationCollection.getFirstElement().equals(currentPage + "")) {
			paginationCollection.setFirstElementClass(PaginationCollection.ACTIVE);
			paginationCollection.setSecondElementClass(null);
			paginationCollection.setThirdElementClass(null);
			paginationCollection.setFourthElementClass(null);
			paginationCollection.setFifthElementClass(null);
		}
		else if (paginationCollection.getFithElement().equals(currentPage + "")) {
			paginationCollection.setFirstElementClass(null);
			paginationCollection.setSecondElementClass(null);
			paginationCollection.setThirdElementClass(null);
			paginationCollection.setFourthElementClass(null);
			paginationCollection.setFifthElementClass(PaginationCollection.ACTIVE);
		}
		else if (paginationCollection.getSecondElement().equals(currentPage + "")) {
			paginationCollection.setFirstElementClass(null);
			paginationCollection.setSecondElementClass(PaginationCollection.ACTIVE);
			paginationCollection.setThirdElementClass(null);
			paginationCollection.setFourthElementClass(null);
			paginationCollection.setFifthElementClass(null);
		}
		else if (paginationCollection.getThirdElement().equals(currentPage + "")) {
			paginationCollection.setFirstElementClass(null);
			paginationCollection.setSecondElementClass(null);
			paginationCollection.setThirdElementClass(PaginationCollection.ACTIVE);
			paginationCollection.setFourthElementClass(null);
			paginationCollection.setFifthElementClass(null);
		}
		else if (paginationCollection.getFourthElement().equals(currentPage + "")) {
			paginationCollection.setFirstElementClass(null);
			paginationCollection.setSecondElementClass(null);
			paginationCollection.setThirdElementClass(null);
			paginationCollection.setFourthElementClass(PaginationCollection.ACTIVE);
			paginationCollection.setFifthElementClass(null);
		}
	}

	public List<JournalResponse> getResponse() {
		return response;
	}

	public String goToJournal() {
		return "journal";
	}

	public PaginationCollection getPaginationCollection() {
		return paginationCollection;
	}

	public PaginationCondition getPagination() {
		return pagination;
	}

	public void setPagination(PaginationCondition pagination) {
		this.pagination = pagination;
	}

	public int getNumElementsFound() {
		return numElementsFound;
	}

	public void setNumElementsFound(int numElementsFound) {
		this.numElementsFound = numElementsFound;
	}

	public void setResponse(List<JournalResponse> response) {
		this.response = response;
	}

	public void setPaginationCollection(PaginationCollection paginationCollection) {
		this.paginationCollection = paginationCollection;
	}
}
