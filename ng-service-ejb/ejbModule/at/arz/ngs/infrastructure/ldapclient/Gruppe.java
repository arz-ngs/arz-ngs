package at.arz.ngs.infrastructure.ldapclient;

/**
 * Repräsentiert eine Gruppe aus dem LDAP Attribut, das durch
 * {@link ldapclient.LdapDescriptor#parseGruppen(javax.naming.directory.Attribute)} zurückgegeben
 * wird.
 * 
 * @author Hendrik Maurer
 * 
 */
public class Gruppe {

	private final String name;

	/**
	 * Erstellt eine neue Gruppe mit dem angegeben Namen.
	 * 
	 * @param name Der Name der neuen Gruppe.
	 */
	public Gruppe(String name) {
		this.name = name;
	}

	/**
	 * @return Den Namen der Gruppe.
	 */
	public String getName() {
		return name;
	}
}