package at.arz.ngs.script;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.Environment;
import at.arz.ngs.EnvironmentRepository;
import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.Service;
import at.arz.ngs.ServiceInstance;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.ServiceRepository;
import at.arz.ngs.api.EnvironmentName;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.ServiceInstanceName;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.Status;
import at.arz.ngs.api.exception.EmptyField;

/**
* Follows the design patter of http://www.adam-bien.com/roller/abien/entry/conveniently_transactionally_and_legally_starting
*/
@Stateless
public class ScriptExecutor {

	@Inject
	private Executor executor;

	@Inject
	private ServiceInstanceRepository serviceInstanceRepository;

	@Inject
	private ServiceRepository serviceRepository;

	@Inject
	private HostRepository hostRepository;

	@Inject
	private EnvironmentRepository environmentRepository;

	protected ScriptExecutor() {
	}

	public void executeScript(ServiceName serviceName, EnvironmentName environmentName, HostName hostName,
			ServiceInstanceName serviceInstanceName, String path) {

		Runnable command = new Runnable() {

			@Override
			public void run() {
				if (path == null || path.equals("")) {
					throw new EmptyField("Empty path");
				}

				/*
				 * We need to fetch an new ServiceInstance here, because if we
				 * had passed over a SI via constructor we have the problem that
				 * the passed over SI is not in transactional Scope anymore.
				 * This is because the original method (in ServiceInstanceAdmin)
				 * has already ended and the transaction is committed and thus
				 * we loose all the transactional functions on this certain SI,
				 * like fetching FetchType.LAZY attributes and saving changes to
				 * DB. To prevent that, we have to load the SI again from the
				 * PersitenceContext and now we have our (new) transaction
				 * again. If we change something from the new SI instance, this
				 * changes are monitored and thus, saved to DB like we want the
				 * appliction to.
				 */
				Host host = hostRepository.getHost(hostName);
				Environment environment = environmentRepository.getEnvironment(environmentName);
				Service service = serviceRepository.getService(serviceName);
				ServiceInstance serviceInstance = serviceInstanceRepository.getServiceInstance(serviceInstanceName,
						service, host, environment);

				List<String> command = new ArrayList<>();
				command.add(path);
				ProcessBuilder processBuilder = new ProcessBuilder(command);
				Process process;
				try {
					process = processBuilder.start();
					int errorCode = process.waitFor();
					System.out.println(errorCode);

					//TODO set real status dependent from returning status-code
					serviceInstance.setStatus(Status.active);
					return;
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}

				//TODO set real status dependent from returning status-code
				serviceInstance.setStatus(Status.failed);
			}
		};
		executor.execute(command);
	}
}
