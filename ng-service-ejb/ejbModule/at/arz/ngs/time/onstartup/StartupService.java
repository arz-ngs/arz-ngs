package at.arz.ngs.time.onstartup;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
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

@Singleton
@Startup
public class StartupService {

	private static boolean fetchInProgress = false;

	private static boolean destroyInProgress = false;

	@Inject
	private Executor executor;

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
		executor.execute(getFetcherLogic());
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
						int interval = getInterval();
						for (int i = 0; i < interval; i++) { //server shutdown can take up to one minute
							TimeUnit.MINUTES.sleep(1);

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

	private Runnable getFetcher() {
		return new Runnable() {
			@Override
			public void run() {
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

					List<String> command = new ArrayList<>();
					command.add(pathStatus.getPath());
					ProcessBuilder processBuilder = new ProcessBuilder(command);
					Process process;
					try {
						process = processBuilder.start();
						InputStream inputStream = process.getInputStream(); //TODO link streams
						InputStream errorStream = process.getErrorStream();

						int errorCode = process.waitFor();

						System.out.println(errorCode);

						//TODO set real status dependent from returning status-code
						System.out.println("status retrieved");
						si.setStatus(Status.active);
					}
					catch (Exception e) {
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
