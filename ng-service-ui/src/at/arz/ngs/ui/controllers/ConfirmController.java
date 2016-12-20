package at.arz.ngs.ui.controllers;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.api.Action;
import at.arz.ngs.api.JobId;
import at.arz.ngs.api.ServiceInstanceLocation;
import at.arz.ngs.job.JobScheduler;
import at.arz.ngs.ui.data_collections.Environment_Service;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;

@ViewScoped
@Named("confirmController")
public class ConfirmController implements Serializable {
	private static final long serialVersionUID = 1L;

	private ErrorCollection errorCollection;

	private Map<Environment_Service, Set<ServiceInstanceLocation>> agg;

	@Inject
	private JobScheduler jobScheduler;

	@Inject
	private ServiceInstanceController serviceInstanceController;

	@PostConstruct
	public void init() {
	}

	public boolean scheduleJobs() {
		System.out.println("CONFIRMATIONCONTROLLER");
		errorCollection = new ErrorCollection();
		// for each element of the map get the job id and afterward schedule the
		// action
		for (Environment_Service es : agg.keySet()) {
			try {
				System.out.println("Test" + es.getEnvironmentName().getName() + " " + es.getServiceName().getName());
				JobId scheduledID = jobScheduler.scheduleAction(Action.stop, es.getServiceName(),
						es.getEnvironmentName(), agg.get(es));

				// start the JobID asynchronously
				jobScheduler.startJob(scheduledID);
			} catch (RuntimeException e) {
				errorCollection.addError(new Error(e));
				errorCollection.setShowPopup(true);
				return false;
			}
		}

		return true;
	}

	public String cancel() {
		serviceInstanceController.formSubmit();
		return "overview";
	}

	public Map<Environment_Service, Set<ServiceInstanceLocation>> getAgg() {
		return agg;
	}

	public void setAgg(Map<Environment_Service, Set<ServiceInstanceLocation>> agg) {
		this.agg = agg;
	}

}
