package sims.chareyron.plateviewer.javafx.view.plateviewer;

public class PlateType {

	int nbWellX;
	int bbWellY;

	public PlateType(int nbWellX, int bbWellY) {
		super();
		this.nbWellX = nbWellX;
		this.bbWellY = bbWellY;
	}

	public int getNbWellX() {
		return nbWellX;
	}

	public void setNbWellX(int nbWellX) {
		this.nbWellX = nbWellX;
	}

	public int getNbWellY() {
		return bbWellY;
	}

	public void setBbWellY(int bbWellY) {
		this.bbWellY = bbWellY;
	}

}
