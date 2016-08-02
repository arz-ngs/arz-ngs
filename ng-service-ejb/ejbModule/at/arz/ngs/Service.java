package at.arz.ngs;

import java.util.LinkedList;
import java.util.List;

import at.arz.ngs.api.PathRestart;
import at.arz.ngs.api.PathStart;
import at.arz.ngs.api.PathStatus;
import at.arz.ngs.api.PathStop;
import at.arz.ngs.api.ServiceName;
import at.arz.ngs.api.Status;

public class Service {

	private long oid;
	private ServiceName serviceName;
	private PathStart pathStart;
	private PathStop pathStop;
	private PathRestart pathRestart;
	private PathStatus pathStatus;
	private Status status;
	private List<Server> server;

	public Service(	long oid,
					ServiceName serviceName,
					PathStart pathStart,
					PathStop pathStop,
					PathRestart pathRestart,
					PathStatus pathStatus,
					Status status) {
		this.oid = oid;
		this.serviceName = serviceName;
		this.pathStart = pathStart;
		this.pathStop = pathStop;
		this.pathRestart = pathRestart;
		this.pathStatus = pathStatus;
		this.status = status;
		server = new LinkedList<Server>();
	}
}
