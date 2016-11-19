package at.arz.ngs.job;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.Executor;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.Job;
import at.arz.ngs.JobRepository;
import at.arz.ngs.Script;
import at.arz.ngs.ServiceInstance;
import at.arz.ngs.api.JobId;
import at.arz.ngs.api.Path;
import at.arz.ngs.api.ServiceInstanceLocation;
import at.arz.ngs.api.Status;
import at.arz.ngs.api.exception.EmptyField;
import at.arz.ngs.journal.JournalAdmin;

@Stateless
public class JobExecutor {

	@Inject
	private Executor executor;

	@Inject
	private JournalAdmin journalAdmin;

	@Inject
	private JobRepository jobRepository;

	protected JobExecutor() {
	}

	public void executeJob(JobId jobId) {

		if (jobId == null || jobId.getValue() == null || jobId.getValue().trim().equals("")) {
			throw new EmptyField("JobId must be set.");
		}

		Runnable command = new Runnable() {

			@Override
			public void run() {
				Job job = jobRepository.getJob(jobId);

				while (job.hasNextInstance()) {
					ServiceInstance si = job.nextInstance();

					si.setStatus(Status.is_starting);
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

					boolean success = executeScript(si, path, job.getAction().name());

					//detach job
					job.actionPerformed(
							new ServiceInstanceLocation(si.getHost().getHostName(), si.getServiceInstanceName()));

					if (!success) {
						//if a error occured
						//TODO log this error

						detachAllJobsOnError(job);

						journalAdmin.addJournalEntry(ServiceInstance.class, si.getOid(), si.toString(),
								job.getAction().name() + " of " + si.toString() + " failed");

						break;
					}
				}

				jobRepository.removeJob(job);
			}
		};
		executor.execute(command);
	}

	private void detachAllJobsOnError(Job job) {
		while (job.hasNextInstance()) {
			ServiceInstance si = job.nextInstance();
			job.actionPerformed(new ServiceInstanceLocation(si.getHost().getHostName(), si.getServiceInstanceName()));
		}
	}

	/**
	 * This method runs NOT asynchronously!
	 */
	private boolean executeScript(ServiceInstance serviceInstance, String pathToExecute, String action) {
		if (pathToExecute == null || pathToExecute.equals("")) {
			throw new EmptyField("Empty path");
		}

		String[] command = pathToExecute.split(" ");
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		Process process;
		try {
			process = processBuilder.start();
			InputStream stdout = process.getInputStream(); //TODO link streams
			InputStream stderr = process.getErrorStream();

			int errorCode = process.waitFor();

			System.out.println("Script stdout:");
			printStream(stdout); //TODO send them to ui
			System.out.println("Script stderr:");
			printStream(stderr);

			if (!action.equals("status")) {
				switch (errorCode) {
					case 0:
						if (action.equals("start")) {
							serviceInstance.setStatus(Status.active);
						}
						else if (action.equals("stop")) {
							serviceInstance.setStatus(Status.not_active);
						}
						else if (action.equals("restart")) {
							serviceInstance.setStatus(Status.active);
						}
						return true;
					case 1:
						serviceInstance.setStatus(Status.failed);
						//		TODO log failure						throw new ExecuteAction(action, serviceInstance.toString());
						return false;
					case 2:
						serviceInstance.setStatus(Status.failed);
						//								throw new UnknownAction(action);
						return false;
					default:
						serviceInstance.setStatus(Status.failed);
						return false;
					//								throw new Unknown(action, serviceInstance.toString());
				}
			}
			else {
				if (errorCode == 0) {
					serviceInstance.setStatus(Status.active);
				}
				else if (errorCode == 3 || errorCode == 17) {
					serviceInstance.setStatus(Status.not_active);
				}
				else if (errorCode == 8) {
					serviceInstance.setStatus(Status.is_starting);
				}
				else {
					serviceInstance.setStatus(Status.unknown);
				}
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		//TODO log failure
		serviceInstance.setStatus(Status.unknown);
		return false;
	}

	private void printStream(InputStream is) {
		Scanner c = new Scanner(is);
		while (c.hasNextLine()) {
			System.out.println(c.nextLine());
		}
		c.close();
	}

	//TODO ask if neccesary or write into the log
	@SuppressWarnings("unused")
	private void saveStreamInJournal(InputStream is, ServiceInstance serviceInstance, String action) {
		Scanner c = new Scanner(is);
		String error = "";
		while (c.hasNextLine()) {
			error += c.nextLine() + " -- ";
		}
		int toErr = error.length() > 4 ? error.length() - 4 : error.length();
		error = error.substring(0, toErr);
		c.close();

		journalAdmin.addJournalEntry(ServiceInstance.class, serviceInstance.getOid(), serviceInstance.toString(),
				action + " of " + serviceInstance.toString() + " failed with error " + error);
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
