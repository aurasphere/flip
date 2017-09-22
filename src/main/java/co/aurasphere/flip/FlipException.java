package co.aurasphere.flip;

public class FlipException extends Exception {

	private static final long serialVersionUID = 1L;

	public FlipException() {
		super();
	}

	public FlipException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FlipException(String message, Throwable cause) {
		super(message, cause);
	}

	public FlipException(String message) {
		super(message);
	}

	public FlipException(Throwable cause) {
		super(cause);
	}

}
