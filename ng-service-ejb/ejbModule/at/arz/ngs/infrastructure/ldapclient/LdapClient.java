package at.arz.ngs.infrastructure.ldapclient;

import java.net.URI;
import java.security.ProviderException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.pool.PoolableObjectFactory;

/**
 * <code>LdapClient</code> kapselt den Zugriff auf ein Ldap zum Lesen von bspw. Benutzerinformationen.
 * 
 * @author Martin Gruschi
 */
public final class LdapClient {

	private static final long DEFAULT_MIN_EVICTABLE_IDLE_TIME = 1500000l;
	private static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS = 300000l;
	private static final int DEFAULT_MAX_WAIT_WHEN_EXHAUSTED = 2000;
	private static final byte DEFAULT_WHEN_EXHAUSTED_ACTION = Byte.valueOf("1").byteValue();
	private static final int DEFAULT_MIN_IDLE_CONNECTIONS = 1;
	private static final int DEFAULT_MAX_IDLE_CONNECTIONS = 3;
	private static final int DEFAULT_MAX_ACTIVE_CONNECTIONS = 10;
	private static final String DEFAULT_TIMEOUT = "60000";
	private final BenutzerCache benutzerCache;
	private final EnumDeclaredProperties baseProps;
	private DirContextPool lastWorkingPool;

	private static final String INITAL_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

	private static final Logger LOGGER = Logger.getLogger(LdapClient.class.getName());

	private static final String BENUTZER_CACHE_PROPERTY = "benutzerCacheSize";

	/**
	 * <code>LdapInstance</code> definiert die Liste der unterstuetzten Ldap-Verzeichnisse.
	 */
	public static enum LdapInstance {
		ACTIVE_DIRECTORY;
	}

	/**
	 * <code>ConnectionPropertyBase</code> definiert die allgemeinen (bspw. Connect-Timeout,...) Properties fuer den
	 * Zugriff auf 1 Ldap (1 Property-File je Ldap fuer alle Anwendungen) und deren Namen.
	 * 
	 * @see EnumDeclaredProperties#getProperty(Enum, Class)
	 */
	private static enum ConnectionPropertyBase {
		URI,
		AUTHENTICATION_TYPE,
		CONNECT_TIMEOUT,
		READ_TIMEOUT,
		MAX_ACTIVE_CONNECTIONS,
		MIN_IDLE_CONNECTIONS,
		MAX_IDLE_CONNECTIONS,
		WHEN_EXHAUSTED_ACTION,
		MAX_WAIT_WHEN_EXHAUSTED,
		TIME_BETWEEN_EVICTION_RUNS,
		MIN_EVICTABLE_IDLE_TIME,
		MAX_ORPHAN_TIME;
	}

	/**
	 * <code>ConnectionPropertyApplication</code> definiert die anwendungsspezifischen (bspw. Security-Credentials)
	 * Properties fuer den Zugriff auf 1 Ldap (1 Property-File je Ldap je Anwendung) und deren Namen.
	 * 
	 * @see EnumDeclaredProperties#getProperty(Enum, Class)
	 */
	private static enum ConnectionPropertyApplication {
		SECURITY_PRINCIPAL,
		SECURITY_CREDENTIALS;
	}


	/**
	 * das Format des Application-Qualifiers ({@value} )
	 */
	public static final String PATTERN_APPLICATION_QUALIFIER = "[a-zA-Z0-9-_]+";

	/**
	 * die Map mit {@link LdapDescriptor} je {@link LdapInstance}
	 */
	private static final Map<LdapInstance, LdapDescriptor> LDAP_DESCRIPTORS = new HashMap<LdapClient.LdapInstance, LdapDescriptor>();

	/**
	 * die erzeugten {@link LdapClient}-Instanzen
	 */
	private static Map<String, LdapClient> clients = new ConcurrentHashMap<String, LdapClient>();

	static {
		LDAP_DESCRIPTORS.put(LdapInstance.ACTIVE_DIRECTORY, new ActiveDirectoryDescriptor());
	}

	/**
	 * der Verbindungspool
	 */
	private List<DirContextPool> contextPools;

	/**
	 * der {@link LdapDescriptor Descriptor}
	 */
	private LdapDescriptor ldapDescriptor;

	/**
	 * erzeugt eine neue Instanz
	 * 
	 * @param contextPool der Verbindungspool
	 * @param ldapDescriptor der {@link LdapDescriptor Descriptor}
	 */
	private LdapClient(	EnumDeclaredProperties baseProps,
						List<DirContextPool> contextPool,
						LdapDescriptor ldapDescriptor) {
		this.baseProps = baseProps;
		this.contextPools = contextPool;
		this.ldapDescriptor = ldapDescriptor;

		benutzerCache = new BenutzerCache(Integer.parseInt(baseProps.getProperty(BENUTZER_CACHE_PROPERTY, "10")));
	}

	/**
	 * liefert die {@link LdapClient}-Instanz zu einem definierten Ldap fuer die angegebene Anwendung. Die dazu
	 * notwendige Config ist in 2 Dateien im {@link #CONFIG_FACTORY_BASE Config-Verzeichnis} enthalten. Die
	 * {@link ConnectionPropertyBase Basis-Konfigurations-Properties} in '&lt;ldapInstance&gt;.properties' und die
	 * {@link ConnectionPropertyApplication anwendungsbezogenen Properties} in
	 * '&lt;ldapInstance&gt;_&lt;applicationQualifier&gt;.properties, wobei &lt;ldapInstance&gt; der Name der
	 * {@link LdapInstance} lowercase ist und der applicationQualifier unveraendert aus dem uebergebenen Parameter in
	 * den File-Namen eingeht.
	 * 
	 * @param ldapInstance die Ldap-Implementierung (fachlich)
	 * @param applicationQualifier ein Anwendungs-Qualifier
	 * @return die {@link LdapClient}-Instanz
	 * @throws ProviderException für jeden intern auftretenden Fehler
	 */
	@SuppressWarnings("boxing")
	public static LdapClient getInstance(	LdapInstance ldapInstance,
											String applicationQualifier,
											Properties authProperties,
											Properties baseProperties) {
		String connectorKey = ldapInstance.name() + "_" + applicationQualifier;
		LdapClient connector = clients.get(connectorKey);
		if (connector != null) {
			return connector;
		}
		synchronized (LdapClient.class) {
			connector = clients.get(connectorKey);
			if (connector != null) {
				return connector;
			}
			try {
				if (!applicationQualifier.matches(LdapClient.PATTERN_APPLICATION_QUALIFIER)) {
					throw new IllegalArgumentException("applicationQualifier (" + applicationQualifier
														+ ") does not match pattern "
														+ LdapClient.PATTERN_APPLICATION_QUALIFIER);
				}
				EnumDeclaredProperties baseProps = new EnumDeclaredProperties(baseProperties);
				EnumDeclaredProperties authProps = new EnumDeclaredProperties(authProperties);

				String[] uris = baseProps.getProperty(ConnectionPropertyBase.URI, URI.class).toString().split(";");
				List<DirContextPool> contextPools = new ArrayList<DirContextPool>();

				for (String uri : uris) {
					PoolableObjectFactory<InitialDirContext> factory = new DirContextPoolObjectFactory(	uri,
																										baseProps.getProperty(	ConnectionPropertyBase.AUTHENTICATION_TYPE,
																																String.class),
																										authProps.getProperty(	ConnectionPropertyApplication.SECURITY_PRINCIPAL,
																																String.class),
																										authProps.getProperty(	ConnectionPropertyApplication.SECURITY_CREDENTIALS,
																																String.class),
																										baseProps.getProperty(	ConnectionPropertyBase.CONNECT_TIMEOUT,
																																String.class,
																																DEFAULT_TIMEOUT),
																										baseProps.getProperty(	ConnectionPropertyBase.READ_TIMEOUT,
																																String.class,
																																DEFAULT_TIMEOUT));
					DirContextPool contextPool = new DirContextPool(factory,
																	uri,
																	baseProps.getProperty(	ConnectionPropertyBase.MAX_ACTIVE_CONNECTIONS,
																							Integer.class,
																							DEFAULT_MAX_ACTIVE_CONNECTIONS),
																	baseProps.getProperty(	ConnectionPropertyBase.MAX_IDLE_CONNECTIONS,
																							Integer.class,
																							DEFAULT_MAX_IDLE_CONNECTIONS),
																	baseProps.getProperty(	ConnectionPropertyBase.MIN_IDLE_CONNECTIONS,
																							Integer.class,
																							DEFAULT_MIN_IDLE_CONNECTIONS),
																	baseProps.getProperty(	ConnectionPropertyBase.WHEN_EXHAUSTED_ACTION,
																							Byte.class,
																							DEFAULT_WHEN_EXHAUSTED_ACTION),
																	baseProps.getProperty(	ConnectionPropertyBase.MAX_WAIT_WHEN_EXHAUSTED,
																							Integer.class,
																							DEFAULT_MAX_WAIT_WHEN_EXHAUSTED),
																	baseProps.getProperty(	ConnectionPropertyBase.TIME_BETWEEN_EVICTION_RUNS,
																							Long.class,
																							DEFAULT_TIME_BETWEEN_EVICTION_RUNS),
																	baseProps.getProperty(	ConnectionPropertyBase.MIN_EVICTABLE_IDLE_TIME,
																							Long.class,
																							DEFAULT_MIN_EVICTABLE_IDLE_TIME));
					contextPools.add(contextPool);
				}
				connector = new LdapClient(baseProps, contextPools, LDAP_DESCRIPTORS.get(ldapInstance));
			} catch (Exception e) {
				throw new ProviderException(e);
			}
			clients.put(connectorKey, connector);
		}
		return connector;
	}

	protected static LdapClient getTestInstance(String applicationQualifier,
												LdapInstance ldapInstance,
												DirContextPool contextPool,
												Properties properties) {
		try {
			if (!applicationQualifier.matches(LdapClient.PATTERN_APPLICATION_QUALIFIER)) {
				throw new IllegalArgumentException("applicationQualifier (" + applicationQualifier
													+ ") does not match pattern "
													+ LdapClient.PATTERN_APPLICATION_QUALIFIER);
			}
			EnumDeclaredProperties baseProps = new EnumDeclaredProperties(properties);
			List<DirContextPool> contextPools = new ArrayList<DirContextPool>();
			contextPools.add(contextPool);

			return new LdapClient(baseProps, contextPools, LDAP_DESCRIPTORS.get(ldapInstance));
		} catch (Exception e) {
			throw new ProviderException(e);
		}
	}


	/**
	 * sucht einen Benutzer auf Basis der UserId in allen Gruppen
	 * 
	 * @param userId die Host-Id
	 * @return die Benutzerinformation oder <code>null</code>, wenn kein Benutzer gefunden wurde
	 * @throws LdapConnectionException , wenn ein Fehler in der Kommunikation mit dem ldap aufgetreten ist
	 * @throws AmbiguousResultException , wenn mahr als 1 Benutzer gefunden wurde
	 */
	public Benutzer findBenutzerByUserId(String userId) throws LdapConnectionException, AmbiguousResultException {
		Benutzer benutzer = benutzerCache.get(userId);
		if (benutzer != null) {
			return benutzer;
		}
		benutzer = findBenutzerByAttribut(Attribut.USERID, userId);
		if (benutzer != null) {
			benutzerCache.put(userId, benutzer);
		}
		return benutzer;
	}

	/**
	 * Führt einen Login mit den angegeben Credentials durch und gibt das Ergebnis direkt zurück.
	 * 
	 * @param userId Die für den Login verwendete User Id.
	 * @param pwd Das Passwort des Benutzers.
	 * @return True, wenn der Login erfolgreich war, False, wenn nicht.
	 * @throws LdapConnectionException Wird geworfen, wenn weder zum Primärserver, noch zu einem der Fallbackserver eine
	 *             Verbindung aufgebaut werden konnte.
	 * @throws AmbiguousResultException Wird geworfen, wenn das LDAP Suchergebnis, mehrere Ergebnisse lieferte.
	 */
	public boolean loginByUserId(String userId, String pwd)	throws LdapConnectionException,
																	AmbiguousResultException {
		DirContext checkLoginContext = null;
		boolean success = false;

		try {
			SearchResult result = searchBenutzer(Attribut.USERID, userId);
			if (result == null) {
				return false;
			}

			String userDn = result.getName();
			Hashtable<String, String> checkLoginEnvironment = new Hashtable<String, String>();

			checkLoginEnvironment.put(Context.INITIAL_CONTEXT_FACTORY, INITAL_CONTEXT_FACTORY);
			checkLoginEnvironment.put(Context.PROVIDER_URL, lastWorkingPool.getUri());
			checkLoginEnvironment.put(	Context.SECURITY_AUTHENTICATION,
										baseProps.getProperty(ConnectionPropertyBase.AUTHENTICATION_TYPE, String.class));
			checkLoginEnvironment.put(	Context.SECURITY_PRINCIPAL,
										userDn + "," + baseProps.getProperty("userSearchBase"));
			checkLoginEnvironment.put(Context.SECURITY_CREDENTIALS, pwd);
			checkLoginEnvironment.put(	DirContextPoolObjectFactory.CONNECT_TIMEOUT_ENVIRONMENT_PROPERTY_NAME,
										baseProps.getProperty(	ConnectionPropertyBase.CONNECT_TIMEOUT,
																String.class,
																DEFAULT_TIMEOUT));
			checkLoginEnvironment.put(	DirContextPoolObjectFactory.READ_TIMEOUT_ENVIRONMENT_PROPERTY_NAME,
										baseProps.getProperty(	ConnectionPropertyBase.READ_TIMEOUT,
																String.class,
																DEFAULT_TIMEOUT));

			checkLoginContext = new InitialDirContext(checkLoginEnvironment);
			success = true;

			Benutzer benutzer = createBenutzerFromSearchResult(result);
			if (benutzer != null) {
				benutzerCache.put(userId, benutzer);
			}
		} catch (NamingException ce) {
			return false;
		} finally {
			try {
				if (checkLoginContext != null) {
					checkLoginContext.close();
				}
			} catch (NamingException e) {
				// Nothing to do
			}
		}
		return success;
	}

	/**
	 * sucht einen Benutzer auf Basis des ubergebenen Attributs
	 * 
	 * @param searchAttribut das Attribut, das verglichen Wert
	 * @param matchValue der Wert, den das zu vergleichende Attribut haben muss
	 * @return die Benutzerinformation oder <code>null</code>, wenn kein Benutzer gefunden wurde
	 * @throws LdapConnectionException, wenn ein Fehler in der Kommunikation mit dem ldap aufgetreten ist
	 * @throws AmbiguousResultException , wenn mahr als 1 Benutzer gefunden wurde
	 */
	private Benutzer findBenutzerByAttribut(Attribut searchAttribut, String matchValue)	throws LdapConnectionException,
																						AmbiguousResultException {
		try {
			SearchResult result = searchBenutzer(searchAttribut, matchValue);
			return createBenutzerFromSearchResult(result);
		} catch (Exception e) {
			throw new LdapConnectionException("find failed: " + e.getMessage(), e);
		}
	}

	private Benutzer createBenutzerFromSearchResult(SearchResult result) throws NamingException {
		if (result == null) {
			return null;
		}

		Map<Attribut, String> ldapAttributeNames = ldapDescriptor.getLdapAttributeNames();
		Map<Attribut, String> benutzerAttribute = new HashMap<Attribut, String>();

		for (Attribut attribut : Attribut.values()) {
			if (attribut.isUsedFor(Benutzer.class)) {
				String ldapAttributName = ldapAttributeNames.get(attribut);
				if (ldapAttributName != null) {
					Attribute ldapAttribute = result.getAttributes().get(ldapAttributName);
					if (ldapAttribute != null) {
						benutzerAttribute.put(attribut, (String) ldapAttribute.get());
					}
				}
			}
		}

		List<Gruppe> gruppen = ldapDescriptor.parseGruppen(result.getAttributes()
																	.get(ldapDescriptor.getGroupAttributeName()));
		return new Benutzer(benutzerAttribute, gruppen);
	}

	/**
	 * <code>EnumDeclaredProperties</code> erweitert {@link Properties} um einen typisierten getter auf die Values.
	 * 
	 * @author Martin Gruschi
	 */
	public static class EnumDeclaredProperties
			extends Properties {

		private static final long serialVersionUID = 1L;

		/**
		 * Constructor aus Properties
		 * 
		 * @param props die Properties
		 */
		public EnumDeclaredProperties(Properties props) {
			super(props);
		}

		/**
		 * delegiert auf {@link #getProperty(Enum, Class, Object)} mit default = <code>null</code>
		 * 
		 * @param <T> der Typ des Properties
		 * @param en die Enum
		 * @param clazz die Klasse deren Typ geliefert werden soll
		 * @return property der Property-Wert
		 * 
		 * @see #getProperty(String)
		 */
		public <T> T getProperty(Enum<?> en, Class<T> clazz) {
			return getProperty(en, clazz, null);
		}

		/**
		 * liefert den Propertywert zu einer Enum entsprechend der Konvention: Property-Name ist enum-Name lowercase,
		 * wobei nach jedem '_' ein Grossbuchstabe folgt.
		 * 
		 * @param <T> der Typ des Properties
		 * @param en die Enum
		 * @param clazz die Klasse deren Typ geliefert werden soll
		 * @param def default-Wert, wenn im Property-file nichts gefunden wird
		 * @return der Property-Wert
		 */
		@SuppressWarnings("unchecked")
		public <T> T getProperty(Enum<?> en, Class<T> clazz, T def) {
			String[] parts = en.name().toLowerCase().split("_");
			String propName = parts[0];
			for (int i = 1; i < parts.length; i++) {
				String part = parts[i];
				propName += new StringBuffer(part.length()).append(java.lang.Character.toTitleCase(part.charAt(0)))
															.append(part.substring(1))
															.toString();
			}
			String propValueStr = super.getProperty(propName);
			if (propValueStr == null) {
				return def;
			}
			String clazzName = clazz.getName();
			if (String.class.getName().equals(clazzName)) {
				return (T) propValueStr;
			}
			if (Long.class.getName().equals(clazzName)) {
				return (T) Long.valueOf(propValueStr);
			}
			if (Integer.class.getName().equals(clazzName)) {
				return (T) Integer.valueOf(propValueStr);
			}
			if (Byte.class.getName().equals(clazzName)) {
				return (T) Byte.valueOf(propValueStr);
			}
			if (URI.class.getName().equals(clazzName)) {
				return (T) URI.create(propValueStr);
			}
			throw new IllegalArgumentException("unsupported Type: " + clazzName);
		}
	}

	private SearchResult searchBenutzer(Attribut searchAttribut, String matchValue)	throws AmbiguousResultException,
																					LdapConnectionException {
		InitialDirContext context = null;
		for (DirContextPool pool : contextPools) {
			try {
				context = pool.getDirContext();

				Map<Attribut, String> ldapAttributeNames = ldapDescriptor.getLdapAttributeNames();
				String searchLdapAttribute = ldapAttributeNames.get(searchAttribut);

				if (searchLdapAttribute == null) {
					throw new IllegalArgumentException("no mapping found for " + searchAttribut
														+ " using "
														+ ldapDescriptor.getClass().getName());
				}

				String searchFilter = MessageFormat.format(	ldapDescriptor.getUserByAttributeFilter(),
															searchLdapAttribute,
															matchValue);
				SearchControls searchControls = setupSearchControls(ldapAttributeNames);
				NamingEnumeration<SearchResult> users = context.search(	baseProps.getProperty("userSearchBase"),
																		searchFilter,
																		searchControls);

				SearchResult checkResult = checkResult(pool, searchFilter, users);
				pool.returnToPool(context);
				return checkResult;
			} catch (ConnectionInitException e) {
				pool.disposeObject(context);
				LOGGER.log(Level.WARNING, "Could not connect to ldap server " + pool.getUri() + ".", e);
			} catch (NamingException e) {
				pool.disposeObject(context);
				LOGGER.log(Level.WARNING, "Could not connect to ldap server " + pool.getUri() + ".", e);
			}
		}
		throw new LdapConnectionException(	"Could not connect to ldap server.",
											new Exception("Failed to connect to server."));
	}

	private SearchControls setupSearchControls(Map<Attribut, String> ldapAttributeNames) {
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		List<String> benutzerLdapAttribute = new ArrayList<String>();
		for (Attribut attribut : Attribut.values()) {
			if (attribut.isUsedFor(Benutzer.class)) {
				String value = ldapAttributeNames.get(attribut);
				if (value != null) {
					benutzerLdapAttribute.add(value);
				}
			}
		}
		benutzerLdapAttribute.add(ldapAttributeNames.get(Attribut.GRUPPE));

		searchControls.setReturningAttributes(benutzerLdapAttribute.toArray(new String[benutzerLdapAttribute.size()]));
		searchControls.setCountLimit(2);
		return searchControls;
	}

	private SearchResult checkResult(DirContextPool pool, String searchFilter, NamingEnumeration<SearchResult> users)	throws NamingException,
																														AmbiguousResultException {
		if (!users.hasMore()) {
			return null;
		}
		SearchResult result = users.next();
		if (users.hasMore()) {
			throw new AmbiguousResultException("more than 1 result for " + searchFilter, null);
		}

		lastWorkingPool = pool;
		return result;
	}
}