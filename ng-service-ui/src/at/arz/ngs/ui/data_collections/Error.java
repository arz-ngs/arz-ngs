package at.arz.ngs.ui.data_collections;


public class Error {

	private String error;
	private String message = "";
	private String stackTrace = "";
	private boolean showPopup;

	public Error(String error, String message, StackTraceElement[] stackTrace) {
		this.error = error;
		this.message = message;
		for (StackTraceElement s : stackTrace) {
			this.stackTrace += s.toString() + "<br/>";
		}
	}
	
	public Error(String error, String message) {
		this.error = error;
		this.message = message;
	}
	
	public Error(Exception e) {
		this(e.getClass().getSimpleName(), e.getMessage(), e.getStackTrace());
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public boolean isShowPopup() {
		return showPopup;
	}

	public void setShowPopup(boolean showPopup) {
		this.showPopup = showPopup;
	}

}
