package at.arz.ngs.jpa.server;

import java.util.LinkedList;
import java.util.List;

import at.arz.ngs.Service;
import at.arz.ngs.api.ServerName;

public class JPAServer {

	private long oid;
	private ServerName serverName;
	private List<Service> services;

	public JPAServer(long oid, ServerName serverName) {
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
