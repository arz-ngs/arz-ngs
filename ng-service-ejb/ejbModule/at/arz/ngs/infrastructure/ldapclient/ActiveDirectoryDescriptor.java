package at.arz.ngs.infrastructure.ldapclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;

public class ActiveDirectoryDescriptor
		implements LdapDescriptor {

	/**
	 * der Suchfilter fuer Benutzer nach Attribut ({@value} )
	 */
	private static final String USER_BY_ATTRIBUTE_FILTER = "(&(objectClass=user)({0}={1}))";

	private static final String GROUP_ATTRIBUTE_NAME = "memberOf";

	static {
		Map<Attribut, String> names = new HashMap<Attribut, String>();
		names.put(Attribut.VORNAME, "givenName");
		names.put(Attribut.NACHNAME, "sn");
		names.put(Attribut.TELEFON, "telephoneNumber");
		names.put(Attribut.EMAIL, "mail");
		names.put(Attribut.ABTEILUNG, "department");
		names.put(Attribut.USERID, "sAMAccountName");
		names.put(Attribut.GRUPPE, "memberOf");
		LDAP_ATTRIBUTE_NAMES = Collections.unmodifiableMap(names);
	}

	/**
	 * das {@link Attribut}-Mapping
	 */
	private static final Map<Attribut, String> LDAP_ATTRIBUTE_NAMES;


	@Override
	public String getUserByAttributeFilter() {
		return USER_BY_ATTRIBUTE_FILTER;
	}

	@Override
	public Map<Attribut, String> getLdapAttributeNames() {
		return LDAP_ATTRIBUTE_NAMES;
	}

	/**
	 * @see LdapDescriptor#getGroupAttributeName()
	 */
	@Override
	public String getGroupAttributeName() {
		return GROUP_ATTRIBUTE_NAME;
	}

	@Override
	public List<Gruppe> parseGruppen(Attribute groupMembership) throws NamingException {
		List<Gruppe> gruppen = new ArrayList<Gruppe>();
		if (groupMembership != null) {
			NamingEnumeration<?> groupAttributes = groupMembership.getAll();
			while (groupAttributes.hasMore()) {
				String groupName = groupAttributes.next().toString();
				gruppen.add(new Gruppe(groupName.substring(groupName.indexOf('=') + 1, groupName.indexOf(','))));
			}
		}
		return gruppen;
	}
}