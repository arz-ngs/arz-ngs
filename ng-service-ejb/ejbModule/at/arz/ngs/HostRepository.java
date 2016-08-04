package at.arz.ngs;

import at.arz.ngs.api.HostName;

public interface HostRepository {

	Host getHost(HostName hostName);

	void remove(Host host);


}
