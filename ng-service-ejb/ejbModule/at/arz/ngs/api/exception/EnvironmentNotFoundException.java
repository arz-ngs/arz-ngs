package at.arz.ngs.api.exception;


public class EnvironmentNotFoundException
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EnvironmentNotFoundException() {
		super("Environment wurde nicht gefunden!");
	}

}
