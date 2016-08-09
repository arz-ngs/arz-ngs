package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class EmptyField
		extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String object;

	public EmptyField(String object) {
		super("Leerer" + object + "String wurde übergeben!");
		this.object = object;
	}

	public String getReason() {
		return ("Leerer " + object + "String wurde übergeben!");
	}

}
