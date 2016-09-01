package at.arz.ngs.ui.data_collections;

public class CommandButtonCollection {
	public static final String DISABLED_CSS_CLASS = "commandButtonDisabled";

	private String startCSSClass = "";
	private boolean startDisabled;
	private String stopCSSClass = "";
	private boolean stopDisabled;
	private String restartCSSClass = "";
	private boolean restartDisabled;
	private String statusCSSClass = "";
	private boolean statusDisabled;

	public CommandButtonCollection() {
	}

	public String getStartCSSClass() {
		return startCSSClass;
	}

	public void setStartCSSClass(String startCSSClass) {
		this.startCSSClass = startCSSClass;
	}

	public boolean isStartDisabled() {
		return startDisabled;
	}

	public void setStartDisabled(boolean startDisabled) {
		this.startDisabled = startDisabled;
	}

	public String getStopCSSClass() {
		return stopCSSClass;
	}

	public void setStopCSSClass(String stopCSSClass) {
		this.stopCSSClass = stopCSSClass;
	}

	public boolean isStopDisabled() {
		return stopDisabled;
	}

	public void setStopDisabled(boolean stopDisabled) {
		this.stopDisabled = stopDisabled;
	}

	public String getRestartCSSClass() {
		return restartCSSClass;
	}

	public void setRestartCSSClass(String restartCSSClass) {
		this.restartCSSClass = restartCSSClass;
	}

	public boolean isRestartDisabled() {
		return restartDisabled;
	}

	public void setRestartDisabled(boolean restartDisabled) {
		this.restartDisabled = restartDisabled;
	}

	public String getStatusCSSClass() {
		return statusCSSClass;
	}

	public void setStatusCSSClass(String statusCSSClass) {
		this.statusCSSClass = statusCSSClass;
	}

	public boolean isStatusDisabled() {
		return statusDisabled;
	}

	public void setStatusDisabled(boolean statusDisabled) {
		this.statusDisabled = statusDisabled;
	}
}
