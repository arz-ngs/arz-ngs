package at.arz.ngs;

import at.arz.ngs.api.HostName;

public interface HostRepository {

	Host findServer(HostName serverName);

	void remove(Host server);


}
