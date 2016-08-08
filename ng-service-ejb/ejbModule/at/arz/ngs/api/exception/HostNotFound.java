package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.HostName;

@ApplicationException(rollback = true)
public class HostNotFound
		extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private HostName hostName;

	public HostNotFound(HostName hostName) {
		super(hostName.toString());
		this.hostName = hostName;
	}

	public HostName getHostName() {
		return hostName;
	}

}
