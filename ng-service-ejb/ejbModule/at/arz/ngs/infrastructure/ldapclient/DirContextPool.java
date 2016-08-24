package at.arz.ngs.infrastructure.ldapclient;

import java.util.logging.Logger;

import javax.naming.directory.InitialDirContext;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * Stellt das Connection-Pooling für LDAP Verbindungen bereit.
 * 
 * @author Hendrik Maurer
 * 
 */
public class DirContextPool
		extends GenericObjectPool<InitialDirContext> {

	private static final Logger LOGGER = Logger.getLogger(DirContextPool.class.getName());

	private String uri;

	/**
	 * Er zeugt einen neuen Connectionpool.
	 * 
	 * @param factory Die {@link PoolableObjectFactory}, die zum Erzeugen des Pools verwendet werden soll.
	 * @param uri Die URI, die für die Verbindungen im Pool verwendet soll.
	 * @param maxActive Die Zahl der maximal aktiven Verbindungen.
	 * @param maxIdle Die Zahl der maximalen Idle-Verbindungen.
	 * @param minIdle Die minimale Zahl der Idle-Verbindungen.
	 * @param whenExhaustedAction -
	 * @param maxWait -
	 * @param timeBetweenEvictionRuns -
	 * @param minEvictableIdleTime -
	 */
	public DirContextPool(	PoolableObjectFactory<InitialDirContext> factory,
							String uri,
							int maxActive,
							int maxIdle,
							int minIdle,
							byte whenExhaustedAction,
							int maxWait,
							long timeBetweenEvictionRuns,
							long minEvictableIdleTime) {
		super(factory);
		setMaxActive(maxActive);
		setMaxIdle(maxIdle);
		setMinIdle(minIdle);
		setWhenExhaustedAction(whenExhaustedAction);
		setMaxWait(maxWait);
		setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRuns);
		setMinEvictableIdleTimeMillis(minEvictableIdleTime);
		setTestOnBorrow(false);
		setTestOnReturn(true);
		setTestWhileIdle(false);

		this.uri = uri;
	}

	/**
	 * @return Die URI, die für die Verbindungen im Pool verwendet wird.
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @return Einen {@link InitialDirContext}, der für Verbindungen mit dem LDAP Server verwendet werden soll.
	 * @throws ConnectionInitException Wird geworfen, wenn das Erzeugen des {@link InitialDirContext} fehlschlägt.
	 */
	public InitialDirContext getDirContext() throws ConnectionInitException {
		LOGGER.finest("Called 'getDirContext()'");
		try {
			return borrowObject();
		} catch (Exception e) {
			throw new ConnectionInitException(e);
		}
	}

	public void returnToPool(InitialDirContext context) {
		LOGGER.finest("Called 'returnToPool()'");
		try {
			returnObject(context);
		} catch (Exception e) {
			// Nothing to do
		}
	}

	public void disposeObject(InitialDirContext context) {
		LOGGER.finest("Called 'disposeObject()'");
		try {
			invalidateObject(context);
		} catch (Exception e) {
			// Nothing to do
		}
	}
}