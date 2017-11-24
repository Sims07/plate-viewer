package sims.chareyron.plateviewer.javafx.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoadingView {
	@FXML
	Label label;

	public void setLoadingMessage(String message) {
		label.setText(message);
	}
}
