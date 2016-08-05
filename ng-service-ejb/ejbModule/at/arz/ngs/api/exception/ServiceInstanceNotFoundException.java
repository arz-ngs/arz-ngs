package at.arz.ngs.api.exception;


public class ServiceInstanceNotFoundException
		extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceInstanceNotFoundException() {
		super("Service Instance wurde nicht gefunden!");
	}
}
