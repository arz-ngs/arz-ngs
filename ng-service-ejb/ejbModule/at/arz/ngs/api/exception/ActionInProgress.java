package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

/**
 * This exception will be thrown, if somebody want to perform an action of a serviceInstance,
 * which is already starting or stopping
 * 
 * @author rpci334
 *
 */
@ApplicationException
public class ActionInProgress
		extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String serviceInstance;

	public ActionInProgress(String serviceInstance) {
		super(serviceInstance);
	}
}
