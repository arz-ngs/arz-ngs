package at.arz.ngs.ui.data_collections;

public class LoginCollection {
	private String errorMessage;
	private boolean errorRendered;

	public LoginCollection(String errorMessage, boolean errorRendered) {
		super();
		this.errorMessage = errorMessage;
		this.errorRendered = errorRendered;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isErrorRendered() {
		return errorRendered;
	}

	public void setErrorRendered(boolean errorRendered) {
		this.errorRendered = errorRendered;
	}

}
