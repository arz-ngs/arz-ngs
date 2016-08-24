package at.arz.ngs.infrastructure.ldapclient;

import java.util.LinkedHashMap;

/**
 * Stellt einfaches Caching für @{link Benutzer} Objekte bereit.
 * 
 * @author Hendrik Maurer
 * 
 */
public class BenutzerCache
		extends LinkedHashMap<String, Benutzer> {

	private static final long serialVersionUID = 1L;
	private final int MAX_ENTRIES;

	/**
	 * Initialisiert einen neuen Cache.
	 * 
	 * @param size Die maximale Größe des Caches.
	 */
	public BenutzerCache(int size) {
		MAX_ENTRIES = size;
	}

	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<String, Benutzer> eldest) {
		return this.size() > MAX_ENTRIES;
	}

	@Override
	public synchronized Benutzer put(String userId, Benutzer benutzer) {
		super.remove(userId);
		return super.put(userId, benutzer);
	}

	@Override
	public synchronized Benutzer get(Object userId) {
		return super.get(userId);
	}

}