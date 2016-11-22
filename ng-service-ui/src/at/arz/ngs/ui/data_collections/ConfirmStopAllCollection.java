package at.arz.ngs.ui.data_collections;

import java.util.Set;

import at.arz.ngs.api.ServiceInstanceLocation;

public class ConfirmStopAllCollection {
	
	private String message;
	private boolean showPopup;
	
	private Environment_Service es;
	private Set<ServiceInstanceLocation> sil;
	
	public ConfirmStopAllCollection() {
		message = "";
	}
	
	public void addMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isShowPopup() {
		return showPopup;
	}

	public void setShowPopup(boolean showPopup) {
		this.showPopup = showPopup;
	}

	public Environment_Service getEs() {
		return es;
	}

	public void setEs(Environment_Service es) {
		this.es = es;
	}

	public Set<ServiceInstanceLocation> getSil() {
		return sil;
	}

	public void setSil(Set<ServiceInstanceLocation> sil) {
		this.sil = sil;
	}

	
}
