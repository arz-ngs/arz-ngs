package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class NoPermission
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String reason;

	public NoPermission(String reason) {
		super(reason);
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

}
