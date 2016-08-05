package at.arz.ngs.host.jpa;

import java.util.List;

import at.arz.ngs.Host;
import at.arz.ngs.HostRepository;
import at.arz.ngs.api.HostName;

public class JPAHostRepository
		implements HostRepository {

	@Override
	public Host getHost(HostName hostName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Host> getAllHosts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeHost(Host host) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addHost(HostName hostName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateHost(Host oldHost, HostName newHostName) {
		// TODO Auto-generated method stub

	}
}
