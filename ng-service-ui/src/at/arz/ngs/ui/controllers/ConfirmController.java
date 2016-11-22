package at.arz.ngs.ui.controllers;

import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.api.Action;
import at.arz.ngs.api.JobId;
import at.arz.ngs.api.ServiceInstanceLocation;
import at.arz.ngs.job.JobScheduler;
import at.arz.ngs.ui.data_collections.ConfirmStopAllCollection;
import at.arz.ngs.ui.data_collections.Environment_Service;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;

@RequestScoped
@Named("confirmController")
public class ConfirmController {
	private ErrorCollection errorCollection;
	
	private Map<Environment_Service, Set<ServiceInstanceLocation>> agg;
	
	@Inject
	JobScheduler jobScheduler;

	public boolean scheduleJobs() {
		errorCollection = new ErrorCollection();
		// for each element of the map get the job id and afterward schedule the action
		for (Environment_Service es : agg.keySet()) {
			try {
				
				JobId scheduledID = jobScheduler.scheduleAction(Action.stop, es.getServiceName(), es.getEnvironmentName(),
						agg.get(es));

				//start the JobID asynchronously
				jobScheduler.startJob(scheduledID);
			}
			catch (RuntimeException e) {
				errorCollection.addError(new Error(e));
				errorCollection.setShowPopup(true);
				return false;
			}
		}

		return true;
	}

	public Map<Environment_Service, Set<ServiceInstanceLocation>> getAgg() {
		return agg;
	}

	public void setAgg(Map<Environment_Service, Set<ServiceInstanceLocation>> agg) {
		this.agg = agg;
	}
	
	
}
