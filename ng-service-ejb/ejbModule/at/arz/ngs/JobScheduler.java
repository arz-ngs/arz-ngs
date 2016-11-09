package at.arz.ngs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.api.Action;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.JobId;
import at.arz.ngs.api.ServiceInstanceLocation;
import at.arz.ngs.api.ServiceName;

@Stateless
public class JobScheduler {

	@Resource
	private SessionContext context;

	@Inject
	private ServiceInstanceRepository serviceInstanceRepo;

	//TODO UUID in JobId packen.
	public JobId scheduleAction(Action action, ServiceName service, EnvironmentName env,
			ServiceInstanceLocation... locations) {
		return scheduleAction(action, service, env, new HashSet<>(Arrays.asList(locations)));
	}

	public JobId scheduleAction(Action action, ServiceName service, EnvironmentName env,
			Set<ServiceInstanceLocation> locations) {
		if (locations.isEmpty()) {
			throw new IllegalArgumentException("At least a single service location is required");
		}

		List<ServiceInstance> instances = serviceInstanceRepo.getServiceInstances(service, env);
		List<ServiceInstance> filtered = new LinkedList<>();
		for (ServiceInstance instance : instances) {
			if (locations.contains(
					new ServiceInstanceLocation(instance.getHost().getHostName(), instance.getServiceInstanceName()))) {
				if (instance.getJob() == null) {
					filtered.add(instance);
					continue;
				}
				throw new IllegalStateException("Job already scheduled!");
			}
		}

		// TODO User ermitteln und an Job übergeben
		// TODO Rechte prüfen
		JobId id = new JobId();
		Job job = new Job(id, action, filtered);

		// TODO JobRepository anlegen
		// TODO Job persistieren
		// Job asynchron ausführen
		return id;
	}

	public void notifyActionCompleted(JobId jobId, siUNI...){
		
	}

}
