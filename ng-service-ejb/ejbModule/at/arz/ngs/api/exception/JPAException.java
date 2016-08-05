package at.arz.ngs.api.exception;

/**
 * If correct business logic implemented, this exception should not be thrown.
 * 
 * @author rpri333
 *
 */
public class JPAException
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JPAException(String failureString) {
		super(failureString);
	}
}
