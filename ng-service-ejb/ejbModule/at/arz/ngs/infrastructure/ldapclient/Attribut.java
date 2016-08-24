package at.arz.ngs.infrastructure.ldapclient;

/**
 * <code>Attribut</code> definiert alle moeglichen Attribute im Ldap inklusive Typ, an dem sie haengen.
 * 
 * @author Martin Gruschi
 */
public enum Attribut {
						VORNAME(Benutzer.class),
						NACHNAME(Benutzer.class),
						TELEFON(Benutzer.class),
						EMAIL(Benutzer.class),
						ABTEILUNG(Benutzer.class),
						USERID(Benutzer.class),
						GRUPPE;

	/**
	 * Liste der Typen, die das jeweilige Attribut enthalten koennen
	 */
	private Class<?>[] usedFor;

	/**
	 * Constructor
	 * 
	 * @param usedFor alle Typen, die das jeweilige Attribut enthalten koennen
	 */
	private Attribut(Class<?>... usedFor) {
		this.usedFor = usedFor;
	}

	/**
	 * prueft, ob das Attribut auf dem uebergebenen Typ verwendet werden kann
	 * 
	 * @param clazz der Typ
	 * @return <code>true</code>, wenn der Typ das Attribute verwenden kann, sonst <code>false</code>
	 */
	public boolean isUsedFor(Class<?> clazz) {
		if (usedFor == null) {
			return false;
		}
		String clazzName = clazz.getName();
		for (Class<?> ufClazz : usedFor) {
			if (ufClazz.getName().equals(clazzName)) {
				return true;
			}
		}
		return false;
	}
}
