package at.arz.ngs.time.onstartup;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import at.arz.ngs.ServiceInstance;
import at.arz.ngs.ServiceInstanceRepository;
import at.arz.ngs.api.PathStatus;
import at.arz.ngs.api.Status;
import at.arz.ngs.infrastructure.executors.NGSExecutor;

@Singleton
@Startup
public class StartupService {

	private static boolean fetchInProgress = false;

	private static boolean destroyInProgress = false;

	@Inject
	private NGSExecutor executor;

	@Inject
	private ServiceInstanceRepository serviceInstanceRepository;

	@PostConstruct
	void onStartup() {
		setupStatusFetcher();
	}

	@PreDestroy
	void onDestroy() {
		destroyInProgress = true;
	}

	private void setupStatusFetcher() {
		executor.executeWithoutTransaction(getFetcherLogic());
	}

	/**
	 * Returns the logic for asynchronously fetching all status for all
	 * ServiceInstance.
	 */
	private Runnable getFetcherLogic() {
		return new Runnable() {

			@Override
			public void run() {
				while (!destroyInProgress) { //never ending until destroy in progress is true
					try {
						int lookForDestroySeconds = 10;
						int interval = getInterval() * 60 / lookForDestroySeconds;

						for (int i = 0; i < interval; i++) { //server shutdown can take up to $lookForDestroySeconds seconds
							TimeUnit.SECONDS.sleep(lookForDestroySeconds); //TODO ask to set Timeout of transaction to a higher value in order to throwing an exception with TX timeouts

							if (destroyInProgress) {
								return;
							}
						}

						while (fetchInProgress) {
							TimeUnit.SECONDS.sleep(5);
						}

						if (destroyInProgress) {
							return;
						}

						fetchInProgress = true;

						executor.execute(getFetcher());
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
	}

	/**
	 * Sequential fetching serviceInstance status!
	 * 
	 * @return
	 */
	private Runnable getFetcher() {
		return new Runnable() {
			@Override
			public void run() {
				System.out
						.println("Start fetching all services at: " + new Date(System.currentTimeMillis()).toString());

				List<ServiceInstance> allInstances = serviceInstanceRepository.getAllInstances();
				for (ServiceInstance si : allInstances) {
					if (destroyInProgress) {
						break;
					}

					PathStatus pathStatus = si.getScript().getPathStatus();
					if (pathStatus == null || pathStatus.getPath() == null || pathStatus.getPath().trim().equals("")) {
						//here can status.unknown be set if wanted. Currently not...
						continue;
					}

					String[] command = pathStatus.getPath().split(" ");
					ProcessBuilder processBuilder = new ProcessBuilder(command);
					Process process;

					try {
						process = processBuilder.start();
						InputStream inputStream = process.getInputStream(); //TODO link streams
						InputStream errorStream = process.getErrorStream();

						int errorCode = process.waitFor();

						if (errorCode == 0) {
							si.setStatus(Status.active);
						}
						else if (errorCode == 3 || errorCode == 17) {
							si.setStatus(Status.not_active);
						}
						else if (errorCode == 8) {
							si.setStatus(Status.is_starting);
						}
						else {
							si.setStatus(Status.unknown);
						}
					}
					catch (Exception e) { //catch exception and proceed with loop -> no rollback
						e.printStackTrace();
					}
				}
				fetchInProgress = false;
			}
		};
	}

	private final int getInterval() {
		String config_dir = System.getProperty("jboss.server.config.dir").replace(";", "");
		try {
			FileInputStream ngs_props = new FileInputStream(config_dir + "/ngs-application.properties");
			Properties p = new Properties();
			p.load(ngs_props);
			String property = p.getProperty("fetch_interval-minutes");
			return new Integer(property);
		}
		catch (Exception e) {
		}

		return 5; //default is 5
	}
}
