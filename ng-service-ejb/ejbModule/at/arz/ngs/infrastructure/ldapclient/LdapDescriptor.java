package at.arz.ngs.infrastructure.ldapclient;

import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;

/**
 * <code>LdapDescriptor</code> beschreibt eine fachliche Instanz eines Ldap-Verzeichnisses.
 * 
 * @author Martin Gruschi
 */
public interface LdapDescriptor {

	/**
	 * @return der Suchfilter in {@link java.text.MessageFormat}-Notation, wobei Platzhalter 0 der Name des Attributs
	 *         ist und
	 *         Platzhalter 1 der Wert, der uebereinstimmen soll
	 */
	String getUserByAttributeFilter();

	/**
	 * @return das Mapping aller {@link Attribut}e auf den jeweiligen Namen des Attributs im Ldap
	 */
	Map<Attribut, String> getLdapAttributeNames();

	/**
	 * @return den Attributnamen, um die Gruppenzugehörigkeit aus einem {@link javax.naming.directory.SearchResult}
	 *         abzufragen
	 */
	String getGroupAttributeName();

	/**
	 * Parst die Gruppen aus dem groupMembership LDAP Attribut und gibt sie als Liste zurück.
	 * 
	 * @param groupMembership Das Attribut, das zur Ermittlung der Gruppen verwendet wird.
	 * @return Eine Liste aller Gruppen, die in dem angegeben Attribut gefunden wurden.
	 * @throws NamingException Wird geworfen, wenn ein Fehler im {@link javax.naming.directory.DirContext} auftritt.
	 */
	List<Gruppe> parseGruppen(Attribute groupMembership) throws NamingException;
}