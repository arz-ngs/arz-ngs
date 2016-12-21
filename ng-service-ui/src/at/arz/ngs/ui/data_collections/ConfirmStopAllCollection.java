package at.arz.ngs.ui.data_collections;

import java.util.Map;
import java.util.Set;

import at.arz.ngs.api.ServiceInstanceLocation;

public class ConfirmStopAllCollection {

	private String message;
	private boolean showPopup;

	private Environment_Service es;
	private Set<ServiceInstanceLocation> sil;

	private Map<Environment_Service, Set<ServiceInstanceLocation>> action_agg;

	private String service;
	private String environment;
	private String host;
	private String instance;

	public ConfirmStopAllCollection() {
		message = "";
	}

	public void dispose() {
		sil = null;
		action_agg = null;
		message = null;
		es = null;
		showPopup = false;
		service = null;
		environment = null;
		host = null;
		instance = null;
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

	public Map<Environment_Service, Set<ServiceInstanceLocation>> getAction_agg() {
		return action_agg;
	}

	public void setAction_agg(Map<Environment_Service, Set<ServiceInstanceLocation>> action_agg) {
		this.action_agg = action_agg;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

}
