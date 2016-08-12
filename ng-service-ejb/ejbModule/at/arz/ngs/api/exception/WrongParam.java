package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

/**
 * This exception will be thrown, if a wrong parameter is entered.
 * 
 * @author rpci334
 *
 */
@ApplicationException
public class WrongParam
		extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String param;

	public WrongParam(String param) {
		super(param);
		this.param = param;
	}

	public String getReason() {
		return this.param;
	}

}