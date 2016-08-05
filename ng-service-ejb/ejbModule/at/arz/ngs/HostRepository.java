package at.arz.ngs;

import java.util.List;

import at.arz.ngs.api.HostName;

public interface HostRepository {

	Host getHost(HostName hostName);

	List<Host> getAllHosts();

	void removeHost(Host host);

	void addHost(HostName hostName);

	void updateHost(Host oldHost, HostName newHostName);
}
