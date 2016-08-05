package at.arz.ngs.api.exception;


public class ServiceNotFoundException
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceNotFoundException() {
		super("Service wurde nicht gefunden!");
	}


}
