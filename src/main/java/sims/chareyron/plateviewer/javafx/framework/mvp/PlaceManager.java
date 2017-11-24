package sims.chareyron.plateviewer.javafx.framework.mvp;

import javafx.stage.Stage;

public interface PlaceManager {

	void init();

	void revealPlace(String token);

	void revealDefaultPlace();

	Stage getStage();

	void setStage(Stage stage);
}
