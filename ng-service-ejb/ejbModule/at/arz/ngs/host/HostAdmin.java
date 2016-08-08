package at.arz.ngs.host;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.arz.ngs.HostRepository;
import at.arz.ngs.api.HostName;

@Stateless
public class HostAdmin {

	private static final Logger log = Logger.getLogger(HostAdmin.class.getName());

	@Inject
	private HostRepository repository;

	public void removeHost(HostName hostName) {
	}
}
