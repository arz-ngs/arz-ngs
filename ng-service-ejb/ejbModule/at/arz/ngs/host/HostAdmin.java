package at.arz.ngs.host;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.exception.HostNotFoundException;

@Stateless
public class HostAdmin {

	private static final Logger log = Logger.getLogger(HostAdmin.class.getName());

	@Inject
	private HostRepository repository;

	public void deleteHost(HostName hostName) {
		try {
			Host host = repository.getHost(hostName);
			repository.removeHost(host);
		} catch (HostNotFoundException e) {
			log.info("could not delete host " + hostName + " (not found).");
		}

	}
}
