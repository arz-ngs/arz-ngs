package at.arz.ngs.ui.data_collections;

import java.util.List;

public class ErrorList {

	private String headerInformation;

	public String getHeaderInformation() {
		return headerInformation;
	}

	public void setHeaderInformation(String headerInformation) {
		this.headerInformation = headerInformation;
	}

	private List<ErrorCollection> errors;

	public void addError(ErrorCollection ec) {
		errors.add(ec);
	}

	public List<ErrorCollection> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorCollection> errors) {
		this.errors = errors;
	}

}
