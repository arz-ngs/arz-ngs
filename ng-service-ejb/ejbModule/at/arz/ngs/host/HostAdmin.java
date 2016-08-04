package at.arz.ngs.host;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.api.HostName;
import at.arz.ngs.api.HostNotFound;

@Stateless
public class HostAdmin {

	private static final Logger log = Logger.getLogger(HostAdmin.class.getName());

	@Inject
	private HostRepository repository;

	public void deleteServer(HostName serverName) {
		try {
			Host server = repository.findServer(serverName);
			repository.remove(server);
		} catch (HostNotFound e) {
			log.info("could not delete server " + serverName + " (not found).");
		}

	}
}
