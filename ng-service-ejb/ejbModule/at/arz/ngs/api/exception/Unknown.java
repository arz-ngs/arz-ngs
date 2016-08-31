package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class Unknown extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public Unknown(String action, String object) {
		super("Unknown error occured by exceuting action " + action + " on " + object);
	}

}
