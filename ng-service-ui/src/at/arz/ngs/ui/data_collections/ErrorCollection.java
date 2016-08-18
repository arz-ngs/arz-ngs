package at.arz.ngs.ui.data_collections;

import java.util.ArrayList;
import java.util.List;

public class ErrorCollection {

	private String headerInformation;
	private List<Error> errors;
	private String messages;
	private int count;
	private boolean showPopup;

	public ErrorCollection() {
		errors = new ArrayList<Error>();
		messages = "";
	}

	public String getHeaderInformation() {
		if (count == 1) {
			return "1 Error ist aufgetreten!";
		} else {
			return count + " Errors sind aufgetreten!";
		}
	}

	public void setHeaderInformation(String headerInformation) {
		this.headerInformation = headerInformation;
	}


	public void addError(Error ec) {
		errors.add(ec);
		messages += ec.getError() + ":" + ec.getMessage() + "<br/><br/>";
		count++;
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public String getMessages() {
		for (Error e : errors) {
			messages += e.getError() + ": " + e.getStackTrace() + "<br/><br/>";
		}
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public boolean isShowPopup() {
		return showPopup;
	}

	public void setShowPopup(boolean showPopup) {
		this.showPopup = showPopup;
	}
	

}
