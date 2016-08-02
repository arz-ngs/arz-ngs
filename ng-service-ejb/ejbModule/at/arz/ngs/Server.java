package at.arz.ngs;

import java.util.LinkedList;
import java.util.List;

import at.arz.ngs.api.ServerName;

public class Server {

	private long oid;
	private ServerName serverName;
	private List<Service> services;

	public Server(long oid, ServerName serverName) {
		this.oid = oid;
		this.serverName = serverName;
		services = new LinkedList<Service>();
	}

	public long getOid() {
		return oid;
	}

	public ServerName getServerName() {
		return serverName;
	}
}
