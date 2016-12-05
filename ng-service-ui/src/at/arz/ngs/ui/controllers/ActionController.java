package at.arz.ngs.ui.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import at.arz.ngs.api.Action;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.JobId;
import at.arz.ngs.api.ServiceInstanceLocation;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.job.JobScheduler;
import at.arz.ngs.serviceinstance.commands.action.PerformAction;
import at.arz.ngs.serviceinstance.commands.find.ServiceInstanceOverview;
import at.arz.ngs.ui.data_collections.ConfirmStopAllCollection;
import at.arz.ngs.ui.data_collections.Environment_Service;
import at.arz.ngs.ui.data_collections.Error;
import at.arz.ngs.ui.data_collections.ErrorCollection;
import at.arz.ngs.ui.data_collections.OverviewCollection;

@RequestScoped
@Named("actionController")
public class ActionController {

	@Inject
	private ServiceInstanceController serviceInstanceController;

	@Inject
	private JobScheduler jobScheduler;

	private ErrorCollection errorCollection;
	
	private ConfirmStopAllCollection confirmCollection;
	
	@Inject
	ConfirmController confirmController;

	@PostConstruct
	public void init() {
	}

	public String start(List<OverviewCollection> overviewList) {
		PerformAction action = new PerformAction();
		action.setPerformAction("start");

		performAction(action, overviewList);

		serviceInstanceController.formSubmit();
		return "overview";
	}

	public String stop(List<OverviewCollection> overviewList) {
		PerformAction action = new PerformAction();
		action.setPerformAction("stop");

		performAction(action, overviewList);

		serviceInstanceController.formSubmit();
		return "overview";
	}

	public String restart(List<OverviewCollection> overviewList) {
		PerformAction action = new PerformAction();
		action.setPerformAction("restart");

		performAction(action, overviewList);

		serviceInstanceController.formSubmit();
		return "overview";
	}

	public String status(List<OverviewCollection> overviewList) {
		PerformAction action = new PerformAction();
		action.setPerformAction("status");

		performAction(action, overviewList);

		serviceInstanceController.formSubmit();
		return "overview";
	}

	private void performAction(PerformAction action, List<OverviewCollection> overviewList) {
		Map<Environment_Service, Set<ServiceInstanceLocation>> agg = new HashMap<>();

		for (OverviewCollection oc : overviewList) {
			if (oc.isChecked()) {
				ServiceInstanceOverview serviceInstance = oc.getServiceInstance();
				//				System.out.println(action + " service instance: " + serviceInstance.toString());
				// Aggregation: Environment and Service
				Environment_Service envS = new Environment_Service(
						new EnvironmentName(serviceInstance.getEnvironmentName()),
						new ServiceName(serviceInstance.getServiceName()));

				ServiceInstanceLocation siL = new ServiceInstanceLocation(new HostName(serviceInstance.getHostName()),
						new ServiceInstanceName(serviceInstance.getInstanceName()));

				if (agg.containsKey(envS)) {
					agg.get(envS).add(siL);
				}
				else {
					Set<ServiceInstanceLocation> lsiL = new HashSet<>();
					lsiL.add(siL);
					agg.put(envS, lsiL);
				}

				//	old one				admin.performAction(serviceInstance.getServiceName(), serviceInstance.getEnvironmentName(),
				//							serviceInstance.getHostName(), serviceInstance.getInstanceName(), action);
			}
			oc.setChecked(false); // set default, no checkbox checked
		}
		scheduleJobs(Action.valueOf(action.getPerformAction()), agg);
	}
	
	private boolean scheduleJobs(Action action, Map<Environment_Service, Set<ServiceInstanceLocation>> agg) {
		errorCollection = new ErrorCollection();

		for (Environment_Service es : agg.keySet()) {
			if(!jobScheduler.checkMultiStop(es.getServiceName(), es.getEnvironmentName(), agg.get(es)) && action.equals(Action.stop)) {
				confirmCollection = new ConfirmStopAllCollection();
				confirmCollection.addMessage("Are you sure to stop all instances of service " + es.getServiceName() + " in environment "+ es.getEnvironmentName() + "?");
				confirmCollection.setShowPopup(true);
				confirmController.setAgg(agg);
				return false; //remove this and stopping instances works
			}
		}
		
		
		// for each element of the map get the job id and afterward schedule the action
		for (Environment_Service es : agg.keySet()) {
			try {
				
				JobId scheduledID = jobScheduler.scheduleAction(action, es.getServiceName(), es.getEnvironmentName(),
						agg.get(es));
				/**
				 * Check der gestoppten Instanzen hier nicht möglich. Was wäre wenn erst beim Xten Job ein Problem auftaucht
				 * dann sind vorherige Instanzen schon in Queue.
				 */

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
	
	public void stopMulti(Environment_Service es, Set<ServiceInstanceLocation> sil) {
		JobId scheduledID = jobScheduler.scheduleAction(Action.valueOf("stop"), es.getServiceName(), es.getEnvironmentName(),
				sil);
		jobScheduler.startJob(scheduledID);

	}

	public void startSingle(String service, String environment, String host, String instance) {
		PerformAction action = new PerformAction();
		action.setPerformAction("start");

		if (!performSingleAction(action, service, environment, host, instance)) {
			return;
		}

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("detailview.xhtml?instance=" + instance
					+ "&service=" + service + "&env=" + environment + "&host=" + host);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopSingle(String service, String environment, String host, String instance) {
		PerformAction action = new PerformAction();
		action.setPerformAction("stop");

		if (!performSingleAction(action, service, environment, host, instance)) {
			return;
		}

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("detailview.xhtml?instance=" + instance
					+ "&service=" + service + "&env=" + environment + "&host=" + host);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void restartSingle(String service, String environment, String host, String instance) {
		PerformAction action = new PerformAction();
		action.setPerformAction("restart");

		if (!performSingleAction(action, service, environment, host, instance)) {
			return;
		}

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("detailview.xhtml?instance=" + instance
					+ "&service=" + service + "&env=" + environment + "&host=" + host);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void statusSingle(String service, String environment, String host, String instance) {
		PerformAction action = new PerformAction();
		action.setPerformAction("status");

		if (!performSingleAction(action, service, environment, host, instance)) {
			return;
		}

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("detailview.xhtml?instance=" + instance
					+ "&service=" + service + "&env=" + environment + "&host=" + host);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ErrorCollection getErrorCollection() {
		return errorCollection;
	}

	public void setErrorCollection(ErrorCollection errorCollection) {
		this.errorCollection = errorCollection;
	}

	public ConfirmStopAllCollection getConfirmCollection() {
		return confirmCollection;
	}

	public void setConfirmCollection(ConfirmStopAllCollection confirmCollection) {
		this.confirmCollection = confirmCollection;
	}

	private boolean performSingleAction(PerformAction action, String service, String environment, String host,
			String instance) {
		errorCollection = new ErrorCollection();
		try {
			Map<Environment_Service, Set<ServiceInstanceLocation>> agg = new HashMap<>();

			Environment_Service envS = new Environment_Service(new EnvironmentName(environment),
					new ServiceName(service));
			Set<ServiceInstanceLocation> siL = new HashSet<>();
			siL.add(new ServiceInstanceLocation(new HostName(host), new ServiceInstanceName(instance)));

			agg.put(envS, siL);

			return scheduleJobs(Action.valueOf(action.getPerformAction()), agg);
		}
		catch (RuntimeException e) {
			errorCollection.addError(new Error(e));
			errorCollection.setShowPopup(true);
			return false;
		}
		//		System.out.println(action.getPerformAction()+ ": "
		//				+ instance
		//				+ "/"
		//				+ service
		//				+ "/"
		//				+ environment
		//				+ "/"
		//				+ host);
	}
}
