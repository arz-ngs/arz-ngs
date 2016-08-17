package at.arz.ngs.ui.data_collections;

import java.util.ArrayList;
import java.util.List;

public class ErrorList {

	private String headerInformation;
	private List<ErrorCollection> errors;
	private String messages;
	private int count;

	public ErrorList() {
		errors = new ArrayList<ErrorCollection>();
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


	public void addError(ErrorCollection ec) {
		errors.add(ec);
		messages += ec.getError() + ":" + ec.getMessage() + "<br/><br/>";
		count++;
	}

	public List<ErrorCollection> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorCollection> errors) {
		this.errors = errors;
	}

	public String getMessages() {
		System.out.println(messages);
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

}
