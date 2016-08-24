package at.arz.ngs.infrastructure.ldapclient;

/**
 * <code>AmbiguousResultException</code> wird geworfen, wenn ein Ergebnis nicht eindeutig ist.
 * 
 * @author Martin Gruschi
 */
public class AmbiguousResultException
		extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * delegiert 1:1 auf super durch
	 * 
	 * @param message message
	 * @param cause cause
	 * 
	 * @see Exception#Exception(String, Throwable)
	 */
	public AmbiguousResultException(String message, Throwable cause) {
		super(message, cause);
	}
}
