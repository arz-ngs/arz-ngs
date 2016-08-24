package at.arz.ngs.infrastructure.ldapclient;

import java.util.Hashtable;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.pool.PoolableObjectFactory;

/**
 * Stellt eine {@link PoolableObjectFactory} zum Erstellen von {@link DirContextPool} Objekten bereit.
 * 
 * @author Hendrik Maurer
 * 
 */
public class DirContextPoolObjectFactory
		implements PoolableObjectFactory<InitialDirContext> {

	private static final Logger LOGGER = Logger.getLogger(DirContextPoolObjectFactory.class.getName());

	/**
	 * Der Propertyname für die Eigenschaft, die den Connection Timeout für die LDAP Verbindung angibt.
	 */
	public static final String CONNECT_TIMEOUT_ENVIRONMENT_PROPERTY_NAME = "com.sun.jndi.ldap.connect.timeout";

	/**
	 * Der Propertyname für die Eigenschaft, die den Read Timeout für die LDAP Verbindung angibt.
	 */
	public static final String READ_TIMEOUT_ENVIRONMENT_PROPERTY_NAME = "com.sun.jndi.ldap.read.timeout";

	private static final String INITIAL_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

	private Hashtable<String, Object> contextEnvironment;

	/**
	 * Erstellt eine neue {@link DirContextPoolObjectFactory}.
	 * 
	 * @param url Die Url, die für die Verbindungen verwendet wird.
	 * @param authenticationType Der verwendete Authentifikationstyp.
	 * @param securityPrincipal Der verwendete Principal.
	 * @param securityCredentials Die verwendeten Credentials.
	 * @param connectTimeout Der Connection Timeout.
	 * @param readTimeout Der Read Timeout.
	 */
	public DirContextPoolObjectFactory(	String url,
										String authenticationType,
										String securityPrincipal,
										String securityCredentials,
										String connectTimeout,
										String readTimeout) {
		contextEnvironment = new Hashtable<String, Object>();
		contextEnvironment.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		contextEnvironment.put(Context.PROVIDER_URL, url);
		contextEnvironment.put(Context.SECURITY_AUTHENTICATION, authenticationType);
		contextEnvironment.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
		contextEnvironment.put(Context.SECURITY_CREDENTIALS, securityCredentials);
		contextEnvironment.put(CONNECT_TIMEOUT_ENVIRONMENT_PROPERTY_NAME, connectTimeout);
		contextEnvironment.put(READ_TIMEOUT_ENVIRONMENT_PROPERTY_NAME, readTimeout);
	}

	/**
	 * {@link PoolableObjectFactory#activateObject(Object)}
	 */
	@Override
	public void activateObject(InitialDirContext context) {
		LOGGER.finest("Called 'activateObject()'");
		// Not used
	}

	/**
	 * {@link PoolableObjectFactory#destroyObject(Object)}
	 */
	@Override
	public void destroyObject(InitialDirContext context) throws NamingException {
		LOGGER.finest("Called 'destroyObject()'");
		// Not used
	}

	/**
	 * {@link PoolableObjectFactory#makeObject()}
	 */
	@Override
	public InitialDirContext makeObject() throws NamingException {
		LOGGER.finest("Called 'makeObject()'");
		return new InitialDirContext(contextEnvironment);
	}

	/**
	 * {@link PoolableObjectFactory#passivateObject(Object)}
	 */
	@Override
	public void passivateObject(InitialDirContext context) {
		LOGGER.finest("Called 'passivateObject()'");
		// Not used
	}

	/**
	 * {@link PoolableObjectFactory#validateObject(Object)}
	 */
	@Override
	public boolean validateObject(InitialDirContext context) {
		LOGGER.finest("Called 'validateObject()'");
		return true;
	}
}