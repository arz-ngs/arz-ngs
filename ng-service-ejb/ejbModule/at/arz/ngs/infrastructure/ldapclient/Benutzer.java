package at.arz.ngs.infrastructure.ldapclient;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <code>Benutzer</code> kapselt die Daten zu einem Benutzer.
 * 
 * @author Martin Gruschi
 */
public class Benutzer {

	/**
	 * Constructor mit allen Attributen des Benutzers
	 * 
	 * @param attribute die Attribute des Benutzers als Attribut-Value-Paaren
	 * 
	 * @param gruppen die Gruppen, denen der Benutzer angehört
	 */
	public Benutzer(Map<Attribut, String> attribute, List<Gruppe> gruppen) {
		Iterator<Attribut> attrIter = attribute.keySet().iterator();
		while (attrIter.hasNext()) {
			Attribut attribut = attrIter.next();
			if (!attribut.isUsedFor(this.getClass())) {
				throw new IllegalArgumentException("attribut " + attribut.name()
													+ " is not allowed for "
													+ this.getClass());
			}
		}
		this.attribute = attribute;
		this.gruppen = gruppen;
	}

	/**
	 * die Benutzer-Attribute(Werte)
	 */
	private Map<Attribut, String> attribute;

	/**
	 * die Gruppen, denen der Benutzer angehört
	 */
	private List<Gruppe> gruppen;

	/**
	 * liefert den Wert zu einem Attribut
	 * 
	 * @param attribut das Attribut
	 * @return der Wert
	 */
	public String getAttribut(Attribut attribut) {
		return attribute.get(attribut);
	}

	/**
	 * @return Gibt alle Gruppen zurück, denen der Benutzer angehört.
	 */
	public List<Gruppe> getGruppen() {
		return gruppen;
	}

	/**
	 * Prüft, ob der Benutzer in der angegeben Gruppe ist.
	 * 
	 * @param simpleGruppenName Der Name der Gruppe.
	 * @return True, wenn der Benutzer in der Gruppe ist, False, wenn nicht.
	 */
	public boolean isInGruppe(String simpleGruppenName) {
		for (Gruppe gruppe : gruppen) {
			if (gruppe.getName().equals(simpleGruppenName)) {
				return true;
			}
		}
		return false;
	}
}