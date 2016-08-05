package at.arz.ngs.api.exception;


public class NoPermissionException
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoPermissionException() {
		super("Keine Berechtigung für diese Aktion!");
	}

}
