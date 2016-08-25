package at.arz.ngs.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import at.arz.ngs.security.user.commands.UserData;

public class LDAPConnector {

	public LDAPConnector() {
		try {
			FileInputStream baseIS = new FileInputStream(System.getProperty("ldapbaseDIR"));
			FileInputStream configIS = new FileInputStream(System.getProperty("ldapconfigDIR"));

			//TODO more elements
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public UserData getUserData(String userID) {
		return null;
	}
}
