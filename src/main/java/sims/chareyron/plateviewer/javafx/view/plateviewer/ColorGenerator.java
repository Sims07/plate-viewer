package sims.chareyron.plateviewer.javafx.view.plateviewer;

import javafx.scene.paint.Color;

public interface ColorGenerator {
	Color getNextColor();

	Color getNextColorReverse();

	void restartColor();
}
