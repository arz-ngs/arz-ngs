package at.arz.ngs.infrastructure.ldapclient;

/**
 * <code>LdapConnectionException</code> wird geweorfen, wenn ein technischer Fehler beim lesen gegen das Ldap auftritt.
 * 
 * @author Martin Gruschi
 */
public class LdapConnectionException
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
	public LdapConnectionException(String message, Throwable cause) {
		super(message, cause);
	}
}
