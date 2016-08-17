package at.arz.ngs.ui.data_collections;


public class OrderImgCollection {

	public static final String ASC_disabled = "/images/ASC_disabled.png";
	public static final String ASC_enabled = "/images/ASC_enabled.png";
	public static final String DESC_enabled = "/images/DESC_enabled.png";

	private String serviceOrderSRC;
	private String envOrderSRC;
	private String hostOrderSRC;
	private String instanceOrderSRC;
	private String statusOrderSRC;

	private String lastSortedBy = "service";
	private boolean lastSortedASC = true; // true if ASC was last, false if descending

	public String getLastSortedBy() {
		return lastSortedBy;
	}

	public void setLastSortedBy(String lastSortedBy) {
		this.lastSortedBy = lastSortedBy;
	}

	public boolean isLastSortedASC() {
		return lastSortedASC;
	}

	public void setLastSortedASC(boolean lastSortedASC) {
		this.lastSortedASC = lastSortedASC;
	}

	public String getServiceOrderSRC() {
		return serviceOrderSRC;
	}

	public void setServiceOrderSRC(String serviceOrderSRC) {
		this.serviceOrderSRC = serviceOrderSRC;
	}

	public String getEnvOrderSRC() {
		return envOrderSRC;
	}

	public void setEnvOrderSRC(String envOrderSRC) {
		this.envOrderSRC = envOrderSRC;
	}

	public String getHostOrderSRC() {
		return hostOrderSRC;
	}

	public void setHostOrderSRC(String hostOrderSRC) {
		this.hostOrderSRC = hostOrderSRC;
	}

	public String getInstanceOrderSRC() {
		return instanceOrderSRC;
	}

	public void setInstanceOrderSRC(String instanceOrderSRC) {
		this.instanceOrderSRC = instanceOrderSRC;
	}

	public String getStatusOrderSRC() {
		return statusOrderSRC;
	}

	public void setStatusOrderSRC(String statusOrderSRC) {
		this.statusOrderSRC = statusOrderSRC;
	}

}
