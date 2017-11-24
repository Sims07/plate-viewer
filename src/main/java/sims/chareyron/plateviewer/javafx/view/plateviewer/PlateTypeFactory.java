package sims.chareyron.plateviewer.javafx.view.plateviewer;

public class PlateTypeFactory {

	private static final PlateType plate24Wells = new PlateType(6, 4);
	private static final PlateType plate6Wells = new PlateType(3, 2);

	PlateType buildPlateType(int nbWells) {
		PlateType plt = null;
		if (plate24Wells(nbWells)) {
			plt = plate24Wells;
		} else {
			plt = plate6Wells;
		}
		return plt;
	}

	private boolean plate24Wells(int nbWells) {
		return nbWells == 24;
	}
}
