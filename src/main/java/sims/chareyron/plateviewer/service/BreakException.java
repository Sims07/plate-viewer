package sims.chareyron.plateviewer.service;

public class BreakException extends RuntimeException {
	private PlateLoadingException plEx;

	public BreakException(PlateLoadingException cause) {
		super(cause);
		this.plEx = cause;
	}

	public PlateLoadingException getPlEx() {
		return plEx;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BreakException() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
