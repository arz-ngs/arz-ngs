package at.arz.ngs.ui.controllers;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.serviceinstance.ServiceInstanceAdmin;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;

@RequestScoped
@Named("deleteServiceInstance")
public class DeleteServiceInstanceController
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	ServiceInstanceAdmin admin;

	@Inject
	private ServiceInstanceController serviceInstanceController;

	private String service;
	private String environment;
	private String host;
	private String instance;

	private ErrorCollection errorCollection;

	public String deleteServiceInstance(String instance, String service, String environment, String host) {
		errorCollection = new ErrorCollection();
		try {
			admin.removeServiceInstance(service, environment, host, instance);
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("detailview.xhtml?instance=" + instance
						+ "&service=" + service + "&env=" + environment + "&host=" + host);
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
			return "";
		}

		serviceInstanceController.formSubmit();
		return "overview";
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

	public ErrorCollection getErrorCollection() {
		return errorCollection;
	}

	public void setErrorCollection(ErrorCollection errorCollection) {
		this.errorCollection = errorCollection;
	}

}
