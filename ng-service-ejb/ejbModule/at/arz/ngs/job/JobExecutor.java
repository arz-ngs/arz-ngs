package at.arz.ngs.job;

import java.io.InputStream;
import java.util.Scanner;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.Environment;
import at.arz.ngs.EnvironmentRepository;
import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.Job;
import at.arz.ngs.JobRepository;
import at.arz.ngs.Script;
import at.arz.ngs.Service;
import at.arz.ngs.ServiceInstance;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.Action;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.JobId;
import at.arz.ngs.api.Path;
import at.arz.ngs.api.ServiceInstanceLocation;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.Status;
import at.arz.ngs.api.exception.EmptyField;
import at.arz.ngs.api.exception.JobNotFound;
import at.arz.ngs.infrastructure.executors.NGSExecutor;
import at.arz.ngs.journal.JournalAdmin;

@Stateless
public class JobExecutor {

	@Inject
	private NGSExecutor executor;

	@Inject
	private JournalAdmin journalAdmin;

	@Inject
	private JobRepository jobRepository;

	@Inject
	private ServiceInstanceRepository serviceInstanceRepository;

	@Inject
	private ServiceRepository serviceRepository;

	@Inject
	private HostRepository hostRepository;

	@Inject
	private EnvironmentRepository environmentRepository;

	protected JobExecutor() {
	}

	public void executeJob(JobId jobId) {

		if (jobId == null || jobId.getValue() == null || jobId.getValue().trim().equals("")) {
			throw new EmptyField("JobId must be set.");
		}

		Job job = jobRepository.getJob(jobId);

		if (job.getAction() == Action.restart) { //sequential execute Job
			executor.executeWithoutTransaction(executeSequential(jobId, job.getAction()));
		}
		else { //parallel execute job
			executor.execute(executeParallel(jobId));
		}
	}

	/**
	 * This method should NOT run in a transaction, due to long running actions.
	 * 
	 * @param jobId
	 * @param action
	 * @return
	 */
	private Runnable executeSequential(JobId jobId, Action action) {
		return new Runnable() {

			@Override
			public void run() {
				SIDataWrapper wrapper = new SIDataWrapper();

				executor.executeNotAsynchronously(determineNextSI(jobId, wrapper));
				//TODO
				/*
				 * Here is a problem: If we get (over the wrapper) the data for
				 * the next service instance to execute it after than, it works
				 * properly by having one aggregation. But if we have more than
				 * one, instances cannot be found in the job instance list. I
				 * think this i caused when the execution thread changes (by
				 * parallel working thread) and so the wrapper data is
				 * overwritten. Also in restarts of more than one aggregation
				 * the instances are restarted more often than one time.
				 * 
				 */

				while (!(wrapper.getNextEnvName() == null || wrapper.getNextServiceName() == null
						|| wrapper.getNextHostName() == null || wrapper.getNextServiceInstanceName() == null
						|| wrapper.getNextPathToExecute() == null)) {
					//at this point the next Names should be set
					executor.executeNotAsynchronouslyWithoutTransaction(executeScript(wrapper.getNextServiceName(),
							wrapper.getNextEnvName(), wrapper.getNextHostName(), wrapper.getNextServiceInstanceName(),
							action.name(), wrapper.getNextPathToExecute(), jobId)); //this can take several minutes

					executor.executeNotAsynchronously(determineNextSI(jobId, wrapper)); //load next SI
				}

				executor.execute(cancelJob(jobId)); //finished
			}
		};
	}

	/**
	 * Needs a transaction.
	 * 
	 * @param jobId
	 * @return
	 */
	private Runnable determineNextSI(JobId jobId, SIDataWrapper wrapper) {
		return new Runnable() {

			@Override
			public void run() {
				Job job = null;

				try {
					job = jobRepository.getJob(jobId);

					if (job.hasNextInstance()) {
						ServiceInstance nextInstance = job.nextInstance();

						wrapper.setNextEnvName(nextInstance.getEnvironment().getEnvironmentName());
						wrapper.setNextServiceName(nextInstance.getService().getServiceName());
						wrapper.setNextHostName(nextInstance.getHost().getHostName());
						wrapper.setNextServiceInstanceName(nextInstance.getServiceInstanceName());

						Script script = nextInstance.getScript();

						switch (job.getAction()) {
							case start:
								wrapper.setNextPathToExecute(resolvePath(script.getPathStart()));
								break;
							case stop:
								wrapper.setNextPathToExecute(resolvePath(script.getPathStop()));
								break;
							case restart:
								wrapper.setNextPathToExecute(resolvePath(script.getPathRestart()));
								break;
							case status:
								wrapper.setNextPathToExecute(resolvePath(script.getPathStatus()));
								break;
							default:
								wrapper.setNextPathToExecute(null);
						}
					}
					else {
						wrapper.setNextEnvName(null);
						wrapper.setNextServiceName(null);
						wrapper.setNextHostName(null);
						wrapper.setNextServiceInstanceName(null);
						wrapper.setNextPathToExecute(null);
					}
				}
				catch (JobNotFound | IllegalStateException e) {
					wrapper.setNextEnvName(null);
					wrapper.setNextServiceName(null);
					wrapper.setNextHostName(null);
					wrapper.setNextServiceInstanceName(null);
					wrapper.setNextPathToExecute(null);
					return;
				}
			}
		};
	}

	/**
	 * Needs a transaction, because starting asynch Script-execution do not take
	 * a lot of time.
	 * 
	 * @param jobId
	 * @return
	 */
	private Runnable executeParallel(JobId jobId) {
		return new Runnable() {

			@Override
			public void run() {
				Job job = jobRepository.getJob(jobId);

				for (ServiceInstance si : job.getInstances()) {
					Script script = si.getScript();

					String path = "";

					switch (job.getAction()) {
						case start:
							path = resolvePath(script.getPathStart());
							break;
						case stop:
							path = resolvePath(script.getPathStop());
							break;
						case restart:
							path = resolvePath(script.getPathRestart());
							break;
						case status:
							path = resolvePath(script.getPathStatus());
							break;
						default:
							return; //TODO log no path for SI
					}

					executor.executeWithoutTransaction(executeScript(si.getService().getServiceName(),
							si.getEnvironment().getEnvironmentName(), si.getHost().getHostName(),
							si.getServiceInstanceName(), job.getAction().name(), path, jobId));

				}
			}
		};
	}

	/**
	 * This method should NOT run in a transaction.
	 * 
	 * @return
	 */
	private Runnable executeScript(ServiceName serviceName, EnvironmentName environmentName, HostName hostName,
			ServiceInstanceName serviceInstanceName, String action, String pathToExecute, JobId jobId) {
		return new Runnable() {

			@Override
			public void run() {
				if (pathToExecute == null || pathToExecute.equals("")) {
					throw new EmptyField("Empty path");
				}

				Status toSet = Status.unknown; //this status should be set at the end of the method
				boolean cancelJob = false; //if true cancel job, if false detach only this SI from job

				String[] command = pathToExecute.split(" ");
				ProcessBuilder processBuilder = new ProcessBuilder(command);
				Process process;
				try {
					process = processBuilder.start();
					InputStream stdout = process.getInputStream(); //TODO link or log streams
					InputStream stderr = process.getErrorStream();

					int errorCode = process.waitFor();

					System.out.println("Script stdout:");
					printStream(stdout);
					System.out.println("Script stderr:");
					printStream(stderr);

					if (!action.equals("status")) {
						switch (errorCode) {
							case 0:
								if (action.equals("start")) {
									toSet = Status.active;
								}
								else if (action.equals("stop")) {
									toSet = Status.not_active;
								}
								else if (action.equals("restart")) {
									toSet = Status.active;
								}
								break;
							case 1:
								toSet = Status.failed;
								//		TODO log failure						throw new ExecuteAction(action, serviceInstance.toString());
								break;
							case 2:
								toSet = Status.failed;
								//								throw new UnknownAction(action);
								break;
							default:
								toSet = Status.failed;
						}
					}
					else {
						switch (errorCode) {
							case 0:
								toSet = Status.active;
								break;
							case 3:
								toSet = Status.not_active;
								break;
							case 17:
								toSet = Status.not_active;
								break;
							case 8:
								toSet = Status.is_starting;
								break;
							default:
								toSet = Status.unknown;
						}
					}

					if (action.equals("restart") && errorCode != 0) {
						cancelJob = true;
					}
				}
				catch (Exception e) {
					//TODO log failure stack
					e.printStackTrace();
					toSet = Status.unknown;
					executor.execute(addJournalEntry(serviceName, environmentName, hostName, serviceInstanceName,
							action + " of " + serviceName.getName() + "/" + environmentName.getName() + "/"
									+ hostName.getName() + "/" + serviceInstanceName.getName()
									+ " with server-side failure"));
				}

				if (toSet == Status.failed) {
					executor.execute(addJournalEntry(serviceName, environmentName, hostName, serviceInstanceName,
							action + " of " + serviceName.getName() + "/" + environmentName.getName() + "/"
									+ hostName.getName() + "/" + serviceInstanceName.getName() + " action failed"));
				}

				executor.execute(writeStatusToServiceInstance(serviceName, environmentName, hostName,
						serviceInstanceName, toSet)); //has to be asynch and with a new transaction

				if (cancelJob) {
					executor.execute(cancelJob(jobId));
				}
				else {
					executor.execute(removeServiceInstanceFromJob(hostName, serviceInstanceName, jobId));
				}
			}
		};
	}

	/**
	 * Needs a transaction.
	 * 
	 * @param toAdd
	 * @return
	 */
	private Runnable addJournalEntry(ServiceName serviceName, EnvironmentName environmentName, HostName hostName,
			ServiceInstanceName serviceInstanceName, String toAdd) {
		return new Runnable() {

			@Override
			public void run() {
				ServiceInstance si = getServiceInstance(serviceName, environmentName, hostName, serviceInstanceName);

				journalAdmin.addJournalEntry(ServiceInstance.class, si.getOid(), si.toString(), toAdd);
			}
		};
	}

	private ServiceInstance getServiceInstance(ServiceName serviceName, EnvironmentName environmentName,
			HostName hostName, ServiceInstanceName serviceInstanceName) {

		Host host = hostRepository.getHost(hostName);
		Environment environment = environmentRepository.getEnvironment(environmentName);
		Service service = serviceRepository.getService(serviceName);
		return serviceInstanceRepository.getServiceInstance(serviceInstanceName, service, host, environment);
	}

	/**
	 * Needs a transaction.
	 * 
	 * @param hostName
	 * @param serviceInstanceName
	 * @param jobId
	 * @return
	 */
	private Runnable removeServiceInstanceFromJob(HostName hostName, ServiceInstanceName serviceInstanceName,
			JobId jobId) {
		return new Runnable() {

			@Override
			public void run() {
				Job job = jobRepository.getJob(jobId);

				job.actionPerformed(new ServiceInstanceLocation(hostName, serviceInstanceName));

				if (!job.hasNextInstance()) { //job finished
					jobRepository.removeJob(job);
				}
			}
		};
	}

	/**
	 * Needs a transaction.
	 * 
	 * @param jobId
	 * @return
	 */
	private Runnable cancelJob(JobId jobId) {
		return new Runnable() {

			@Override
			public void run() {
				try {
					Job job = jobRepository.getJob(jobId);

					while (job.hasNextInstance()) {
						ServiceInstance si = job.nextInstance();
						job.actionPerformed(
								new ServiceInstanceLocation(si.getHost().getHostName(), si.getServiceInstanceName()));
					}

					jobRepository.removeJob(job);
				}
				catch (JobNotFound e) {
					return;
				}
			}
		};
	}

	/**
	 * Needs a transaction.
	 * 
	 * @return
	 */
	private Runnable writeStatusToServiceInstance(ServiceName serviceName, EnvironmentName environmentName,
			HostName hostName, ServiceInstanceName serviceInstanceName, Status status) {
		return new Runnable() {

			@Override
			public void run() {
				ServiceInstance serviceInstance = getServiceInstance(serviceName, environmentName, hostName,
						serviceInstanceName);

				serviceInstance.setStatus(status);
			}
		};
	}

	private void printStream(InputStream is) {
		Scanner c = new Scanner(is);
		while (c.hasNextLine()) {
			System.out.println(c.nextLine());
		}
		c.close();
	}

	private String resolvePath(Path path) {
		String p = getPath(path);
		if (p == null) {
			throw new EmptyField("To perform an action a valid path must be set!");
		}
		return p;
	}

	private String getPath(Path path) {
		if (path == null) {
			return null;
		}

		return path.getPath();
	}
}
