package at.arz.ngs.infrastructure.ldapclient;

/**
 * Wird geworfen, wenn die Erstellung eines {@link javax.naming.directory.InitialDirContext} fehlschlägt.
 * 
 * @author Hendrik Maurer
 * 
 */
public class ConnectionInitException
		extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * delegiert auf super durch
	 * 
	 * @param cause cause
	 */
	public ConnectionInitException(Throwable cause) {
		super(cause);
	}
}