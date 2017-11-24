package sims.chareyron.plateviewer.javafx.view.plateviewer;

public class PlatePreferenceUI {

	private double padding, offset;

	public PlatePreferenceUI(double padding, double offset) {
		super();
		this.padding = padding;
		this.offset = offset;
	}

	public double getPadding() {
		return padding;
	}

	public void setPadding(double padding) {
		this.padding = padding;
	}

	public double getOffset() {
		return offset;
	}

	public void setOffset(double offset) {
		this.offset = offset;
	}

}
