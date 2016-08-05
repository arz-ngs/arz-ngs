package at.arz.ngs.api.exception;


public class HostNotFoundException
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HostNotFoundException() {
		super("Host wurde nicht gefunden!");
	}

}
