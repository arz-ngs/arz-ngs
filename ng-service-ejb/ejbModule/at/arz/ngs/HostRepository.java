package at.arz.ngs;

import at.arz.ngs.api.ServerName;

public interface HostRepository {

	Host findServer(ServerName serverName);

	void remove(Host server);


}
