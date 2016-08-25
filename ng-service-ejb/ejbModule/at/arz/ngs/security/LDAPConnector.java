package at.arz.ngs.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import at.arz.ngs.infrastructure.ldapclient.AmbiguousResultException;
import at.arz.ngs.infrastructure.ldapclient.Attribut;
import at.arz.ngs.infrastructure.ldapclient.Benutzer;
import at.arz.ngs.infrastructure.ldapclient.LdapClient;
import at.arz.ngs.infrastructure.ldapclient.LdapClient.LdapInstance;
import at.arz.ngs.infrastructure.ldapclient.LdapConnectionException;
import at.arz.ngs.security.user.commands.UserData;

public class LDAPConnector {

	private LdapClient client_instance;

	public LDAPConnector() {
		try {
			String config_dir = System.getProperty("jboss.server.config.dir").replace(";", "");
			FileInputStream ad_ngsIS = new FileInputStream(config_dir + System.getProperty("ad_ngs_properties"));
			FileInputStream ad_IS = new FileInputStream(config_dir + System.getProperty("ad_properties"));

			Properties prop1 = new Properties();
			prop1.load(ad_ngsIS);
			Properties prop2 = new Properties();
			prop2.load(ad_IS);
			client_instance = LdapClient.getInstance(LdapInstance.ACTIVE_DIRECTORY, "test", prop1, prop2);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public UserData getUserData(String userID) {
		if (client_instance == null) {
			return null;
		}

		try {
			Benutzer benutzer = client_instance.findBenutzerByUserId(userID);

			if (benutzer == null || benutzer.getAttribut(Attribut.USERID) == null) {
				return null;
			}
			
			return new UserData(benutzer.getAttribut(Attribut.USERID),
										benutzer.getAttribut(Attribut.VORNAME),
										benutzer.getAttribut(Attribut.NACHNAME),
										benutzer.getAttribut(Attribut.EMAIL));
		} catch (LdapConnectionException e) {
			e.printStackTrace();
		} catch (AmbiguousResultException e) {
			e.printStackTrace();
		}
		return null;
	}
}
