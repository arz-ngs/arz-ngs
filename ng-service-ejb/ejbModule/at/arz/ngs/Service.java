package at.arz.ngs;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

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

	@ManyToMany(mappedBy = "services", fetch = FetchType.LAZY)
	private List<Host> server;

	public Service(	ServiceName serviceName,
					PathStart pathStart,
					PathStop pathStop,
					PathRestart pathRestart,
					PathStatus pathStatus,
					Status status) {
		this.serviceName = serviceName;
		this.pathStart = pathStart;
		this.pathStop = pathStop;
		this.pathRestart = pathRestart;
		this.pathStatus = pathStatus;
		this.status = status;
		server = new LinkedList<Host>();
	}
}
