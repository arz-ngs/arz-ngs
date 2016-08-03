package at.arz.ngs.server;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.api.ServerName;
import at.arz.ngs.api.ServerNotFound;

@Stateless
public class ServerAdmin {

	private static final Logger log = Logger.getLogger(ServerAdmin.class.getName());

	@Inject
	private HostRepository repository;

	public void deleteServer(ServerName serverName) {
		try {
			Host server = repository.findServer(serverName);
			repository.remove(server);
		} catch (ServerNotFound e) {
			log.info("could not delete server " + serverName + " (not found).");
		}

	}
}
