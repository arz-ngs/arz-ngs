package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

/**
 * If correct business logic implemented, this exception should not be thrown.
 * 
 * @author rpri333
 *
 */
@ApplicationException(rollback = true)
public class JPAException
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String reason;

	public JPAException(String failureString) {
		super(failureString);
		this.reason = failureString;

	}

	@Override
	public String toString() {
		return super.toString();
	}

	public String getReason() {
		return this.reason;
	}
}
